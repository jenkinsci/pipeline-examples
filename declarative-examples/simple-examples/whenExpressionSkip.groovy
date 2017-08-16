pipeline {
  agent any
  stages {
    stage("Hello") {
      steps {
        echo "Hello"
      }
    }
    stage("Always Skip") {
      when {
        // skip this stage unless the expression evaluates to 'true'
        expression {
          echo "Should I run?"
          return false
        }
      }
      steps {
        echo "World"
      }
    }
  }
}
