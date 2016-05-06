#!groovy
/*
   Instead of duplicating a lot of build related code in each repo include the common one from this file using the command below:

   Don't forget to put configure GITHUB_TOKEN inside Jenkins as it is a very bad idea to include it inside your code.
*/

apply from: 'https://raw.githubusercontent.com/org-name/repo-name/master/subfolder/Jenkinsfile?token=${env.GITHUB_TOKEN}'
