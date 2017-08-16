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
    // these steps will run at the end of the pipeline based on the condition.
    // The conditions run in this order regardless of their arrangement
        always {
            echo "I AM ALWAYS first"
        }
        changed {
            echo "CHANGED is run second"
        }
        success {
            echo "SUCCESS or FAILURE run third"
        }
        failure {
            echo "SUCCESS or FAILURE run third"
        }
    }
}
