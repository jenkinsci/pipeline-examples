pipeline {
  /*
   * The environment section is used for setting environment variables and will allow for
   * expanding variable and other methods to set those values as long as the return type is a String
   */
  environment {
    FOO = "BAR"
    BUILD_NUM_ENV = currentBuild.getNumber()
    ANOTHER_ENV = "${currentBuild.getNumber()}"
    INHERITED_ENV = "\${BUILD_NUM_ENV} is inherited"
    ACME_FUNC = returnAThing("banana")
  }

  agent any

  stages {
    stage("Environment") {
      steps {
        sh 'echo "FOO is $FOO"'
        sh 'echo "BUILD_NUM_ENV is $BUILD_NUM_ENV"'
        sh 'echo "ANOTHER_ENV is $ANOTHER_ENV"'
        sh 'echo "INHERITED_ENV is $INHERITED_ENV"'
        sh 'echo "ACME_FUNC is $ACME_FUNC"'
      }
    }
  }
}
