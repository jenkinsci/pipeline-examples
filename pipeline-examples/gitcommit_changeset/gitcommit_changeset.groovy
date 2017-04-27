// This should be performed at the point where you've
// checked out your sources on the agent. A 'git' executable
// must be available.
// Most typical, if you're not cloning into a sub directory
// and invoke this in the context of a directory with .git/
// Along with SHA-1 id of the commit, it will be useful to retrieve changeset associated with that commit
// This command results in output indicating several one of these and the affected files:
// Added (A), Copied (C), Deleted (D), Modified (M), Renamed (R)
commitChangeset = sh(returnStdout: true, script: 'git diff-tree --no-commit-id --name-status -r HEAD').trim()
