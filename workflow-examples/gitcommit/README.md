# Synopsis
Demonstrate how to expose the git_commit to a workflow job.

# Background

The git plugin exposes some environment variables to a freestyle job that are not currently exposed to a workflow job.
Here's how to recover that ability using a git command and workflow's readFile() function.

