pipeline {
  environment {
    // environment variables and credential retrieval can be interspersed
    SOME_VAR = "SOME VALUE"
    // this assumes that "cred1" has been created on Jenkins Credentials
    CRED1 = credentials("cred1")
    INBETWEEN = "Something in between"
    // this assumes that "cred2" has been created in Jenkins Credentials
    CRED2 = credentials("cred2")
    // Env variables can refer to other variables as well
    OTHER_VAR = "${SOME_VAR}"
  }

  agent any

  stages {
    stage("foo") {
      steps {
        // environment variables are not masked
        sh 'echo "SOME_VAR is $SOME_VAR"'
        sh 'echo "INBETWEEN is $INBETWEEN"'
        sh 'echo "OTHER_VAR is $OTHER_VAR"'

        // credential variables will be masked in console log but not in archived file
        sh 'echo $CRED1 > cred1.txt'
        sh 'echo $CRED2 > cred2.txt'
        archive "**/*.txt"
      }
    }
  }
}
