
Skip to content
This repository

    Pull requests
    Issues
    Gist

    @nimbals1

1
0

    155

nimbals1/pipeline-examples forked from jenkinsci/pipeline-examples
Code 
Pull requests 0
Projects 0
Pulse
Graphs
Settings
pipeline-examples/jenkinsfile-examples/nodejs-build-test-deploy-docker-notify/Jenkinsfile
54c8f61 on 30 Aug
@ArtikUA ArtikUA Update Jenkinsfile
@MikeCaspar
@patoi
@ArtikUA
@andrask
90 lines (59 sloc) 2.43 KB
#!groovy

/*
The MIT License
Copyright (c) 2015-, CloudBees, Inc., and a number of other of contributors
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

node('node') {

    currentBuild.result = "SUCCESS"

    try {

       stage 'Checkout'

            checkout scm

       stage 'Test'

            env.NODE_ENV = "test"

            print "Environment will be : ${env.NODE_ENV}"

            sh 'node -v'
            sh 'npm prune'
            sh 'npm install'
            sh 'npm test'

       stage 'Build Docker'

            sh './dockerBuild.sh'

       stage 'Deploy'

            echo 'Push to Repo'
            sh './dockerPushToRepo.sh'

            echo 'ssh to web server and tell it to pull new image'
            sh 'ssh deploy@xxxxx.xxxxx.com running/xxxxxxx/dockerRun.sh'

       stage 'Cleanup'

            echo 'prune and cleanup'
            sh 'npm prune'
            sh 'rm node_modules -rf'

            mail body: 'project build successful',
                        from: 'xxxx@yyyyy.com',
                        replyTo: 'xxxx@yyyy.com',
                        subject: 'project build successful',
                        to: 'yyyyy@yyyy.com'

        }


    catch (err) {

        currentBuild.result = "FAILURE"

            mail body: "project build error is here: ${env.BUILD_URL}" ,
            from: 'xxxx@yyyy.com',
            replyTo: 'yyyy@yyyy.com',
            subject: 'project build failed',
            to: 'zzzz@yyyyy.com'

        throw err
    }

}

    Contact GitHub API Training Shop Blog About 
    © 2016 GitHub, Inc. Terms Privacy Security Status Help 
