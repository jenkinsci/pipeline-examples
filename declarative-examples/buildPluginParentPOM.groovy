pipeline {
    tools {
        maven "apache-maven-3.1.0"
        jdk "default"
    }

    agent {
        label "master"
    }

    stages {
        stage("build") {
            steps {
                dir("tmp") {
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
            archive "target/**/*"
        }
    }
}
