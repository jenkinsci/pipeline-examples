pipeline {
    // no agent required to run here. All steps run in flyweight executor on Master
    agent none

    stages {
        stage("foo") {
            steps {
                echo "hello"
            }
        }
    }
    post {
    /*
     * These steps will run at the end of the pipeline based on the condition.
     * Post conditions run in order regardless of their place in pipeline
     * 1. always - always run
     * 2. changed - run if something changed from last run
     * 3. aborted, success, unstable or failure - depending on status
     */
        always {
            echo "I AM ALWAYS first"
        }
        changed {
            echo "CHANGED is run second"
        }
        aborted {
          echo "SUCCESS, FAILURE, UNSTABLE, or ABORTED are exclusive of each other"
        }
        success {
            echo "SUCCESS, FAILURE, UNSTABLE, or ABORTED runs last"
        }
        unstable {
          echo "SUCCESS, FAILURE, UNSTABLE, or ABORTED runs last"
        }
        failure {
            echo "SUCCESS, FAILURE, UNSTABLE, or ABORTED runs last"
        }
    }
}
