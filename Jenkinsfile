pipeline {
    agent any

    tools {
        maven 'Maven3'   // Name must match a Maven install configured in Jenkins Global Tool Config
        jdk 'JDK17'      // Name must match a JDK install configured in Jenkins Global Tool Config
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B -q clean compile test-compile'
            }
        }

        stage('Run Selenium Tests') {
            steps {
                // headless=true so Chrome runs without a display on the Jenkins agent
                sh 'mvn -B test -Dheadless=true'
            }
        }
    }

    post {
        always {
            junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
        }
        failure {
            echo 'Build failed — check target/surefire-reports for details.'
        }
    }
}
