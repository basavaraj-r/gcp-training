pipeline {
    agent any

    environment {
        PROJECT_ID   = 'mar-basavaraj-25mar-ust'
        REGION       = 'us-central1'
        REPO_NAME    = 'springboot-repo'
        IMAGE_NAME   = 'customer-service'
        TAG          = "v1"
        CLUSTER_NAME = 'spring-gke-cluster'
        ZONE     = 'us-central1-a'
        IMAGE_URI    = "${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPO_NAME}/${IMAGE_NAME}:${TAG}"
        SONAR_AUTH_TOKEN = "squ_34776fc1f4d8fdb989db5f8bfb704c02db89fa95"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/basavaraj-r/gcp-training.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Sonar Scan') {
            steps {
                withSonarQubeEnv('sonar-local') {
                    sh '''
                      mvn sonar:sonar \
                        -Dsonar.projectKey=customer-service \
                        -Dsonar.host.url=http://host.docker.internal:9000 \
                        -Dsonar.token=$SONAR_AUTH_TOKEN
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $IMAGE_URI .'
            }
        }

        stage('Push Image') {
            steps {
                sh '''
                  gcloud auth activate-service-account --key-file=/var/jenkins_home/jenkins-key.json
                  gcloud config set project $PROJECT_ID
                  gcloud auth configure-docker ${REGION}-docker.pkg.dev -q
                  docker push $IMAGE_URI
                '''
            }
        }

        stage('Get GKE Credentials') {
            steps {
                sh '''
                  gcloud auth activate-service-account --key-file=/var/jenkins_home/jenkins-key.json
                  gcloud config set project $PROJECT_ID
                  gcloud container clusters get-credentials $CLUSTER_NAME --zone $ZONE
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                  kubectl set image deployment/customer-service customer-service=$IMAGE_URI || true
                  kubectl apply -f deployment.yaml
                  kubectl apply -f service.yaml
                '''
            }
        }

        stage('Rollout Check') {
            steps {
                sh 'kubectl rollout status deployment/customer-service'
            }
        }
    }

    post {
        failure {
            sh 'kubectl rollout undo deployment/customer-service || true'
        }
    }
}