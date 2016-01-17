#!groovy

// Loads the standardBuild function/step from workflowLibs.git/vars/standardBuild.groovy
// and invokes it.
standardBuild {
    environment = 'golang:1.5.0'
    mainScript = '''
go version
go build -v hello-world.go
'''
    postScript = '''
ls -l
./hello-world
'''
}
