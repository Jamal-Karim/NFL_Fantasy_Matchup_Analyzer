pipeline {
    agent any

    options {
        timeout(time: 10, unit: 'MINUTES')
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling the application...'
                bat 'mvnw.cmd clean compile --batch-mode'
            }
        }

        stage('Test') {
            steps {
                echo 'Running NFL Fantasy Unit Tests & Cucumber Tests...'
                bat 'mvnw.cmd test --batch-mode -Dspring.profiles.active=test,automation -Dsurefire.reportFormat=plain'
            }
        }
    }

    post {
        always {
            echo 'Archiving test results...'
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo '✅ Build Passed! The Engine is stable.'
        }
        failure {
            echo '❌ Build Failed! Check the logic for errors.'
        }
    }
}