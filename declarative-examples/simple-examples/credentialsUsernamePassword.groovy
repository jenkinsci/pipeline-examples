pipeline {
    environment {
      /*
       * Uses a Jenkins credential called "FOOCredentials" and creates environment variables:
       * "$FOO" will contain string "USR:PSW"
       * "$FOO_USR" will contain string for Username
       * "$FOO_PSW" will contain string for Password
       */
      FOO = credentials("FOOcredentials")
    }

    agent any

    stages {
        stage("foo") {
            steps {
                // all credential values are available for use but will be masked in console log
                sh 'echo "FOO is $FOO"'
                sh 'echo "FOO_USR is $FOO_USR"'
                sh 'echo "FOO_PSW is $FOO_PSW"'

                //Write to file
                dir("combined") {
                    sh 'echo $FOO > foo.txt'
                }
                sh 'echo $FOO_PSW > foo_psw.txt'
                sh 'echo $FOO_USR > foo_usr.txt'
                archive "**/*.txt"
            }
        }
    }
}
