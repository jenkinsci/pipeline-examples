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
node('test') {
    // compute complete workspace path, from current node to the allocated disk
    exws(extWorkspace) {
        // run tests in the same workspace that the project was built
        sh 'mvn test'
    }
}
