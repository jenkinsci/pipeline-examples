// These should all be performed at the point where you've
// checked out your sources on the agent. A 'git' executable
// must be available.
// Most typical, if you're not cloning into a sub directory
shortCommit = sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
