node {
    stage('checkout') {
        git url: 'https://repository.com/foo/golang-project.git'
    }
    stage("build") {
        writeFile file: "test.txt", text: "test"
        docker.withRegistry("https://my-private-registry.intranet.net", 'docker-registry-credentials') {
            docker.image("my-private-registry.intranet.net/tool/golang").inside("-v /home/jenkins/foo.txt:/foo.txt") { c ->
                sh 'cat /foo.txt' // we can mount any file from host
                sh 'cat test.txt' // we can access files from workspace
                sh 'echo "modified-inside-container" > test.txt' // we can modify files in workspace
                sh 'go build' // we can run command from docker image
                sh 'printenv' // jenkins is passing all envs variables into container
            }
        }
        sh 'cat test.txt' // will be "modified-inside-container" here
    }
}
