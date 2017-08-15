// allocate a Disk from the Disk Pool defined in the Jenkins global config
def extWorkspace = exwsAllocate 'diskpool1'

// on a node labeled 'linux', perform code checkout and build the project
node('linux') {
    // compute complete workspace path, from current node to the allocated disk
    exws(extWorkspace) {
        // checkout code from repo
        checkout scm
        // build project, but skip running tests
        sh 'mvn clean install -DskipTests'
    }
}

// on a different node, labeled 'test', perform testing using the same workspace as previously
// at the end, if the build have passed, delete the workspace
node('test') {
    // compute complete workspace path, from current node to the allocated disk
    exws(extWorkspace) {
        try {
            // run tests in the same workspace that the project was built
            sh 'mvn test'
        } catch (e) {
            // if any exception occurs, mark the build as failed
            currentBuild.result = 'FAILURE'
            throw e
        } finally {
            // perform workspace cleanup only if the build have passed
            // if the build has failed, the workspace will be kept
            cleanWs cleanWhenFailure: false
        }
    }
}
