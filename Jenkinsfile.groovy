properties([parameters([extendedChoice(multiSelectDelimiter: ',',
        name: 'PROFILE',
        quoteValue: false,
        saveJSONParameterToFile: false,
        type: 'PT_SINGLE_SELECT',
        value: 'unit-tests,long-unit-tests',
        visibleItemCount: 2)])])

node {
    def mvnHome
    stage('Preparation') {
        mvnHome = tool 'M3'
        git branch: 'main', url: 'https://github.com/MashaM38/restful-web-services'
    }

    stage('Build') {
        withEnv(["MVN_HOME=$mvnHome"]) {
            sh """${MVN_HOME}/bin/mvn clean test surefire-report:report -P${PROFILE}"""
            echo """RESULT: ${currentBuild.currentResult} """
        }
    }
    stage('Results') {
        echo """Build completed. Cuurent build result is: ${currentBuild.result} """
        junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        publishHTML([allowMissing         : true,
                     alwaysLinkToLastBuild: false,
                     keepAll              : true,
                     reportDir            : 'target/site',
                     reportFiles          : 'surefire-report.html',
                     reportName           : 'HTML Report',
                     reportTitles         : 'Test Results',
                     keppAll              : true])
    }
    try {
        findText(textFinders: [textFinder(alsoCheckConsoleOutput: true, regexp: 'There are test failures')])
    } catch (e) {
        currentBuild.result = "FAILURE"
        echo """Build run result is: ${currentBuild.currentResult} """
    } finally {
        sendMail()
    }
    cleanWs()
}

def sendMail() {
    def recipient = 'XXX@testEmail.com'

    echo "Sending email notification"
    emailext (to: recipient,
            subject: "RESULT build: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
            body: """<p>STATUS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
            <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
            recipientProviders: [[$class: 'DevelopersRecipientProvider']]
    )
}