pipeline {
  /*
   * The environment section is used for setting environment variables and will allow for
   * expanding variable and other methods to set those values as long as the return type is a String.
   * String escaping in Groovy can affect the behavior here. Please refer here for detailed explainations
   * http://docs.groovy-lang.org/latest/html/documentation/#all-strings
   * The 'readMavenPom()' method is provided by the Pipeline Utility Steps plugin
   */
  environment {
    FOO = "BAR"
    BUILD_NUM_ENV = currentBuild.getNumber()
    ANOTHER_ENV = "${currentBuild.getNumber()}"
    INHERITED_ENV = "\${BUILD_NUM_ENV} is inherited"
    ACME_FUNC = readMavenPom().getArtifactId()
  }

  agent any

  stages {
    stage("Environment") {
      steps {
        sh 'echo "FOO is $FOO"'
        // returns 'FOO is BAR'

        sh 'echo "BUILD_NUM_ENV is $BUILD_NUM_ENV"'
        // returns 'BUILD_NUM_ENV is 4' depending on the build number

        sh 'echo "ANOTHER_ENV is $ANOTHER_ENV"'
        // returns 'ANOTHER_ENV is 4' like the previous depending on the build number

        sh 'echo "INHERITED_ENV is $INHERITED_ENV"'
        // returns 'INHERITED_ENV is ${BUILD_NUM_ENV} is inherited'
        // The \ escapes the $ so the variable is not expanded but becomes a literal

        sh 'echo "ACME_FUNC is $ACME_FUNC"'
        // returns 'ACME_FUNC is spring-petclinic' or the name of the artifact in the pom.xml
      }
    }
  }
}
