pipeline {
  agent none
  stages {
    stage("foo") {
      steps {
        /*
         * Any Pipeline steps and wrappers can be used within the "steps" section
         * of a Pipeline and they can be nested.
         * Refer to the Pipeline Syntax Snippet Generator for the correct syntax of any step or wrapper
         */
        timeout(time: 5, unit: "SECONDS") {
          retry(5) {
            echo "hello"
          }
        }
      }
    }
  }
}
