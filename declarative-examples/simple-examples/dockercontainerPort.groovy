pipeline {
    environment {
        IMAGE = "docker-repository/custom-tutum"
    }

    // build agent where docker commands can be run
    agent 'docker-node'

    stages {
        stage('build') {
            steps {
                script {
                    // Dockerfile should be stored at the dir level same as that of Jenkinsfile
                    // Contents of Dockerfile,
                    /*
                        FROM tutum/hello-world
                        EXPOSE 80
                    */
                    image = docker.build("${IMAGE}")
                    println "Newly generated image, " + image.id
                }
            }
        }
        stage('test') {
            steps {
                script {
                    // https://hub.docker.com/r/tutum/hello-world/
                    // build agent should have 'curl' installed.
                    def container = image.run('-p 80')
                    def contport = container.port(80)
                    def resp = sh(returnStdout: true,
                                        script: """
                                                set -x
                                                curl -w "%{http_code}" -o /dev/null -s \
                                                http://\"${contport}\"
                                                """
                                        ).trim()
                    if ( resp == "200" ) {
                        println "tutum hello world app is alive and kicking!"
                        currentBuild.result = "SUCCESS"
                    } else {
                        println "tutum hello world app isn't reachable!!!"
                        currentBuild.result = "FAILURE"
                    }
                }
            }
        }
    }
}
