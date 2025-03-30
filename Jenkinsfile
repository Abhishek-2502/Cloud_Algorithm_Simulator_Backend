pipeline {
    agent { label 'cloudsimBackend }

    environment {
        DOCKER_IMAGE = 'cloudsim-springboot-app'
        DOCKER_TAG = 'latest'
        CONTAINER_NAME = 'cloudsim-springboot-container'
        SSH_CREDENTIALS_ID = 'cloudsim-frontend-slave' // Match Jenkins credentials ID
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo 'Checking out backend code using SSH...'
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'main']],
                        userRemoteConfigs: [[
                            url: 'git@github.com:Abhishek-2502/Cloud_Algorithm_Simulator_Backend.git',
                            credentialsId: "${SSH_CREDENTIALS_ID}"
                        ]]
                    ])
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }

        stage('Stop and Remove Existing Container') {
            steps {
                script {
                    sh "docker ps -a -q --filter 'name=${CONTAINER_NAME}' | xargs -r docker stop"
                    sh "docker ps -a -q --filter 'name=${CONTAINER_NAME}' | xargs -r docker rm"
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                sh "docker run -d -p 8080:8080 --name ${CONTAINER_NAME} ${DOCKER_IMAGE}:${DOCKER_TAG}"
            }
        }
    }

    post {
        success {
            echo 'Spring Boot backend deployed successfully!'
        }
        failure {
            echo 'Deployment failed. Please check the logs.'
        }
    }
}
