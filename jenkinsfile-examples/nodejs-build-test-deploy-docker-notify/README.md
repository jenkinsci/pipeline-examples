# Synopsis
Demonstrate an example of a Jenkinsfile to build/test/deploy and notify based on results

# Background

A try/catch block is useful to perform specific actions based on the result of a complex pipeline. This example Jenkinsfile shows.
- Pull from Git
- Install nodejs
- Build
- Test
- Build and push a docker container to private dockerhub
- SSH to a server to tell it to use the new image
- Notification by email of positive or negative results

# A more detailed groovy example
The inspiration and help on how to do this came from (https://github.com/freebsd/freebsd-ci/blob/master/scripts/build/build-test.groovy)



 

