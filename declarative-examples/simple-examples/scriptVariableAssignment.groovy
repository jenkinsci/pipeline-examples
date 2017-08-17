pipeline {
  agent {
    // executes on an executor with the label 'some-label' or 'docker'
    label "some-label || docker"
  }

  stages {
    stage("foo") {
      steps {
        // variable assignment (other than environment variables) can only be done in a script block
        // complex global variables (with properties or methods) can only be run in a script block
        // env variables can also be set within a script block
        script {
          foo = docker.image('ubuntu')
          env.bar = "${foo.imageName()}"
          echo "foo: ${foo.imageName()}"
        }
      }
    }
    stage("bar") {
      steps{
        echo "bar: ${env.bar}"
        echo "foo: ${foo.imageName()}"
      }
    }
  }
}
