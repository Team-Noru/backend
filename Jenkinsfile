pipeline {
  agent any

  environment {
    APP_HOST = "10.0.2.18"
    APP_DIR  = "/opt/noru"
  }

  stages {
    stage('Deploy to App Server') {
      steps {
        sshagent(credentials: ['aws-private-key']) {
          sh """
            ssh -o StrictHostKeyChecking=no ec2-user@${APP_HOST} '
              set -e
              cd ${APP_DIR}
              docker compose up -d
              docker compose ps
            '
          """
        }
      }
    }
  }
}
