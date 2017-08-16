pipeline {
  agent any

  environment {
      FOO = "BAR"
  }

  stages {
    stage("Hello") {
      steps {
        echo "Hello"
      }
    }
    stage("Evaluate FOO") {
      when {
        // stage won't be skipped as long as FOO == BAR
        environment name: "FOO", value: "BAR"
      }
      steps {
        echo "World"
        echo "Heal it"
      }
    }
  }
}
