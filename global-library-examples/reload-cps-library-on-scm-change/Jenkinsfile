#!groovy

node('master') {
  stage "checkout"
  checkout scm
  stage "push to jenkins"
  sh "git push jenkins@localhost:workflowLibs.git '+refs/remotes/origin/*:refs/heads/*'"
}
