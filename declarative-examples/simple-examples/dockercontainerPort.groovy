pipeline {

    environment {
        IMAGE = "custom-tutum"
    }

    agent 'node-where-docker-commands-can-be-run'

    stages {
        stage('build') {
            steps {
                script {
                    image = docker.build("${IMAGE}")
                    println "Newly generated image, " + image.id
                }
            }
        }
        stage('test') {
            steps {
                script {
                    // https://hub.docker.com/r/tutum/hello-world/
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
                        println "tutum hello world is alive and kicking!"
                        currentBuild.result = "SUCCESS"
                    } else {
                        println "Humans are mortals."
                        currentBuild.result = "FAILURE"
                    }
                }
            }
        }
    }
}
