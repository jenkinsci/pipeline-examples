pipeline {
  agent any

  // This defines job parameters that are populated before job is run or default is used
  parameters {
    booleanParam(defaultValue: true, description: '', name: 'flag')
    string(defaultValue: '', description: '', name: 'SOME_STRING')
  }

  // Triggers define how the job is triggered.
  // Jobs may still be triggered manually or by webhook as well here.
  triggers {
    cron('@daily')
  }

  // Options covers all other job properties or wrapper functions that apply to entire Pipeline.
  options {
    buildDiscarder(logRotator(numToKeepStr:'1'))
    disableConcurrentBuilds()
    skipDefaultCheckout(true)
    timeout(time: 5, unit: 'MINUTES')
    timestamps()
  }

  stages {
    stage("foo") {
      steps {
        echo "hello"
        sh "test -f Jenkinsfile"
      }
    }
  }
}
