pipeline {
    agent any

    parameters {
        // create a job parameter for this pipeline with a default
        booleanParam(defaultValue: false, description: 'Simulate the promotion', name: 'SIMUL')
    }

    stages {
        stage('promote') {
            when {
                expression {
                    // only run when the current build number is odd
                    currentBuild.getNumber() % 2 == 1
                }
            }
            steps {
                // call the current job and pass the SIMUL parameter value
                build job: currentBuild.getProjectName(), parameters: [
                    booleanParam(name: 'SIMUL', value: params.SIMUL)
                ]
            }
        }
    }
}
