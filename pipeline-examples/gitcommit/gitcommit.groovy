// These should all be performed at the point where you've
// checked out your sources on the slave. A 'git' executable
// must be available.
// Most typical, if you're not cloning into a sub directory
gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
// short SHA, possibly better for chat notifications, etc.
shortCommit = gitCommit.take(6)
