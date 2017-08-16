pipeline {
  agent any
  stages {
    stage("foo") {
      // Tools configured in Jenkins can be included at the Pipeline level or at the Stage Level.
      tools {
        maven "apache-maven-3.0.1"
      }
      steps {
        echo "hello"
      }
    }
  }
}
