// Declarative Jenkinsfile Pipeline for a Hashicorp packer/terraform AWS simple ec2 stack
// (n.b. use of env.BRANCH_NAME to filter stages based on branch means this needs to be part
// of a Multibranch Project in Jenkins - this fits with the model of branches/PR's being
// tested & master being deployed)
pipeline {
  agent any
  environment {
     AWS_DEFAULT_REGION = 'us-east-1'
  }

  stages {
    stage('Validate & lint') {
      parallel {
        stage('packer validate') {
          agent {
            docker {
              image 'simonmcc/hashicorp-pipeline:latest'
              alwaysPull true
            }
          }
          steps {
            checkout scm
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
              sh "packer validate ./base/base.json"
              sh "AMI_BASE=ami-fakefake packer validate app/app.json"
            }
          }
        }
        stage('terraform fmt') {
          agent { docker { image 'simonmcc/hashicorp-pipeline:latest' } }
          steps {
            checkout scm
            sh "terraform fmt -check=true -diff=true"
          }
        }
      }
    }
    stage('build AMIs') {
      agent { docker { image 'simonmcc/hashicorp-pipeline:latest' } }
      steps {
        checkout scm
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                          credentialsId: 'demo-aws-creds',
                          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' ]]) {
          wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
            sh "./scripts/build.sh base base"
            sh "./scripts/build.sh app app"
          }
        }
      }
    }

    stage('build test stack') {
      agent { docker { image 'simonmcc/hashicorp-pipeline:latest' } }
      when {
        expression { env.BRANCH_NAME != 'master' }
      }
      steps {
        checkout scm
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                          credentialsId: 'demo-aws-creds',
                          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' ]]) {
          wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
            sh "./scripts/tf-wrapper.sh -a plan"
            sh "./scripts/tf-wrapper.sh -a apply"
            sh "cat output.json"
            stash name: 'terraform_output', includes: '**/output.json'
          }
        }
      }
      post {
        failure {
          withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                            credentialsId: 'demo-aws-creds',
                            accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                            secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' ]]) {
            wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
              sh "./scripts/tf-wrapper.sh -a destroy"
            }
          }
        }
      }
    }
    stage('test test stack') {
      agent {
        docker {
          image 'chef/inspec:latest'
           args "--entrypoint=''"
        }
      }
      when {
        expression { env.BRANCH_NAME != 'master' }
      }
      steps {
        checkout scm
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                          credentialsId: 'demo-aws-creds',
                          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' ]]) {
          wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
            unstash 'terraform_output'
            sh "cat output.json"
            sh "mkdir aws-security/files || true"
            sh "mkdir test-results || true"
            sh "cp output.json aws-security/files/output.json"
            sh "inspec exec aws-security --reporter=cli junit:test-results/inspec-junit.xml -t aws://us-east-1"
            sh "touch test-results/inspec-junit.xml"
            junit 'test-results/*.xml'
          }
        }
      }
    }
    stage('destroy test stack') {
      agent { docker { image 'simonmcc/hashicorp-pipeline:latest' } }
      when {
        expression { env.BRANCH_NAME != 'master' }
      }
      steps {
        checkout scm
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                          credentialsId: 'demo-aws-creds',
                          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' ]]) {
          wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
            sh "./scripts/tf-wrapper.sh -a destroy"
          }
        }
      }
    }
    stage('terraform plan - master') {
      agent { docker { image 'simonmcc/hashicorp-pipeline:latest' } }
      when {
        expression { env.BRANCH_NAME == 'master' }
      }
      steps {
        checkout scm
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                          credentialsId: 'demo-aws-creds',
                          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' ]]) {
          wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
            sh "./scripts/tf-wrapper.sh -a plan"
            stash name: 'terraform_plan', includes: 'plan/plan.out,.terraform/**'
          }
        }
      }
    }
    stage('Manual Approval') {
      when {
        expression { env.BRANCH_NAME == 'master' }
      }
      steps {
        input 'Do you approve the apply?'
      }
    }
    stage('terraform apply - master') {
      agent { docker { image 'simonmcc/hashicorp-pipeline:latest' } }
      when {
        expression { env.BRANCH_NAME == 'master' }
      }
      steps {
        checkout scm
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                          credentialsId: 'demo-aws-creds',
                          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY' ]]) {
          wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm']) {
            unstash 'terraform_plan'
            sh "./scripts/tf-wrapper.sh -a apply"
          }
        }
      }
    }
  }
}
