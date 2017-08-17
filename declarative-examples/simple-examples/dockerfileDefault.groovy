pipeline {
  agent {
    dockerfile {
      /*
       * This assumes that a "Dockerfile" is in the current workspace
       * A new container will be build with the args below and the pipeline will run inside that container.
       */
      args "-v /tmp:/tmp -p 8000:8000"
    }
  }
  stages {
    stage("foo") {
      steps {
        sh 'cat /hi-there'
        sh 'echo "The answer is 42"'
      }
    }
  }
}
