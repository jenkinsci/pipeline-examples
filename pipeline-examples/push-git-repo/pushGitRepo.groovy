// This is currently the best way to push a tag (or a branch, etc) from a
// Pipeline job. It's not ideal - https://issues.jenkins-ci.org/browse/JENKINS-28335
// is an open JIRA for getting the GitPublisher Jenkins functionality working
// with Pipeline.

// credentialsId here is the credentials you have set up in Jenkins for pushing
// to that repository using username and password.
withCredentials([usernamePassword(credentialsId: 'git-pass-credentials-ID', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
    sh("git tag -a some_tag -m 'Jenkins'")
    sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@<REPO> --tags')
}

// For SSH private key authentication, try the sshagent step from the SSH Agent plugin.
sshagent (credentials: ['git-ssh-credentials-ID']) {
    sh("git tag -a some_tag -m 'Jenkins'")
    sh('git push <REPO> --tags')
}

// For SSH private key authentication and Jenkins Blue Ocean, you can take
// advantage of the auto generated SSH key...

sshagent (credentials: ['jenkins-generated-ssh-key']) {
  sh 'git config core.sshCommand "ssh -v -o StrictHostKeyChecking=no"'
  sh 'git checkout ${GIT_BRANCH}'
  sh 'git pull'
  sh "git tag ${GIT_TAG}"
  sh 'git push origin ${GIT_BRANCH} --tags'
}
