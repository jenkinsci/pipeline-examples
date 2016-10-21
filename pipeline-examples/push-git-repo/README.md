# Synopsis

This demonstrates how to push a tag (or branch, etc) to a remote Git
repository from within a Pipeline job. The authentication step may vary between projects. This example illustrates injected credentials and also username / password authentication.

# Note

If you inject a credential associated with your Git repo, use the Snippet Generator to select the plain `Git` option and it will return a snippet with this gem:

```java
stage('Checkout') {
       git branch: 'lts-1.532', credentialsId: '82aa2d26-ef4b-4a6a-a05f-2e1090b9ce17', url: 'git@github.com:jenkinsci/maven-plugin.git'
   }
```
This is not ideal - there is an open JIRA,
https://issues.jenkins-ci.org/browse/JENKINS-28335, for getting the GitPublisher Jenkins functionality working with Pipeline.

# Credit

Based on Stackoverflow answer at http://stackoverflow.com/questions/33570075/tag-a-repo-from-a-jenkins-workflow-script
Injected credentials gist at https://gist.github.com/blaisep/eb8aa720b06eff4f095e4b64326961b5#file-jenkins-pipeline-git-cred-md
