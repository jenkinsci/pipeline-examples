pipeline {
  agent any

  stages {
    stage("One") {
      steps {
        echo "Hello"
      }
    }
    stage("Two") {
      when {
        expression {
          // "expression" can be any Groovy expression
          return false
        }
      }
      steps {
        echo "World"
      }
    }
    stage("Three") {
      // This will show what a skipped stage followed by an unskipped stage looks like in Blue Ocean
      steps {
        echo "Other World"
      }
    }
  }
}
