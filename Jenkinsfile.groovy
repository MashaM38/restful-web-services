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

    try {
        stage('Build') {
            withEnv(["MVN_HOME=$mvnHome"]){
                sh """${MVN_HOME}/bin/mvn clean test surefire-report:report -P${PROFILE}"""
                echo """RESULT: ${currentBuild.currentResult} """
            }
        }
    } catch(e) {
        currentBuild.result = "FAILURE"
        echo """Build run result is: ${currentBuild.currentResult} """
    }
    finally {
        stage('Results') {
            findText(textFinders: [textFinder(alsoCheckConsoleOutput: true, regexp: 'There are test failures')])
            echo """Build completed. Cuurent build result is: ${currentBuild.result} """
            junit allowEmptyResults:true, testResults:'target/surefire-reports/*.xml'
            publishHTML([allowMissing:true,
                         alwaysLinkToLastBuild:false,
                         keepAll:true,
                         reportDir:'target/site',
                         reportFiles:'surefire-report.html',
                         reportName:'HTML Report',
                         reportTitles:'Test Results',
                         keppAll:true])
        }
    }
    cleanWs()
}