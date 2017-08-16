pipeline {
  // use the 'tools' section to use specific tool versions already defined in Jenkins config 
  tools {
    maven "apache-maven-3.1.0"
    jdk "default"
  }

  // run on any available agent
  agent any

  stages {
    stage("build") {
      steps {
        // create a directory called "tmp" and cd into that directory
        dir("tmp") {
          // use git to retrieve the plugin-pom
          git changelog: false, poll: false, url: 'git://github.com/jenkinsci/plugin-pom.git', branch: 'master'
          sh 'echo "M2_HOME: ${M2_HOME}"'
          sh 'echo "JAVA_HOME: ${JAVA_HOME}"'
          sh 'mvn clean verify -Dmaven.test.failure.ignore=true'
        }
      }
    }
  }

  post {
    always {
      // always archive the target when stages are done
      archive "target/**/*"
    }
  }
}
