pipeline {
  agent any

  stages {
    stage("Hello") {
      steps {
        echo "Hello"
      }
    }
    stage("Branch Test") {
      when {
        // skip this stage unless branch is NOT master
        not {
          branch "master"
        }
      }
      steps {
        echo "World"
        echo "Heal it"
      }
    }
  }
}
