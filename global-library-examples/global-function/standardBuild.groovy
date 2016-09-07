// See https://github.com/jenkinsci/workflow-cps-global-lib-plugin

// The call(body) method in any file in workflowLibs.git/vars is exposed as a
// method with the same name as the file.
def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    stage 'checkout'
    node {
        checkout scm
        stage 'main'
        docker.image(config.environment).inside {
            sh config.mainScript
        }
        stage 'post'
        sh config.postScript
    }
}
