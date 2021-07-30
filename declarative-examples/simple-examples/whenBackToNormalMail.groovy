pipeline {
  /*
   * This example shows how you can send "back to normal" mails.
   * The post section checks if the build was failed (failure).
   * If the build was failed an email is sent.
   * If the build was successfull AND the previous build was failed,
   * a "back to normal" mail is sent.
   */

  agent any

  stages {
    stage("Hello") {
      steps {
        sh 'echo "Hello"'
      }
    }
  }

  post {
    failure {
      mail to: 'user@mail.com',
      subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
      body: "Build failed: ${env.BUILD_URL}"
    }

    success {
      script {
        if (currentBuild.previousBuild != null && currentBuild.previousBuild.result != 'SUCCESS') {
          mail to: 'user@mail.com',
          subject: "Pipeline Success: ${currentBuild.fullDisplayName}",
          body: "Build is back to normal (success): ${env.BUILD_URL}" 
        }
      }           
    }
  }
}
