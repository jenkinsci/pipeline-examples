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
          // "expression can be any Groovy expression"
          echo "Should I run?"
          return false
        }
      }
      steps {
        echo "World"
        echo "Heal it"
      }
    }
    stage("Three") {
      when {
        expression {
          echo "I always run"
          return true
        }
      }
      steps {
        echo "I'm running anyway"
      }
    }
    stage("Four") {
      // This will show what a skipped stage looks like in Blue Ocean
      steps {
        echo "And I run last of all"
      }
    }
  }
}
