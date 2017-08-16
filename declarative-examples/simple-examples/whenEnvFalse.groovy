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
    stage("Always Skip") {
      when {
        // skip this stage unless FOO == "SOME_OTHER_VALUE"
        environment name: "FOO", value: "SOME_OTHER_VALUE"
      }
      steps {
        echo "World"
        echo "Heal it"
      }
    }
  }
}
