pipeline {
  agent {
    dockerfile {
      /*
        * The Default is "Dockerfile" but this can be changed.
        * This will build a new container based on the contents of "Dockerfile.alternate"
        * and run the pipline inside this container
        */
      filename "Dockerfile.alternate"
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
