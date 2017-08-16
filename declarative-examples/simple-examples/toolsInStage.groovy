pipeline {
  agent none
  stages {
    stage("foo") {

      agent any

      /*
       * Tools configured in Jenkins can be included at the Pipeline level or at the Stage Level.
       * This allows you to use different tools on different agents.
       * In this case the pipeline does not have a agent assigned, 'agent none', but
       * the stage does have an agent, 'agent any'. If I use a different agent in a
       * subsequent stage I can use a different version of the tool or different tools.
       */
      tools {
        maven "apache-maven-3.0.1"
      }
      steps {
        echo "hello"
      }
    }
  }
}
