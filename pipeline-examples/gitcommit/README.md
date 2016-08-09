# Synopsis

Demonstrate how to expose the git_commit to a Pipeline job.

# Background

The git plugin exposes some environment variables to a freestyle job that are not currently exposed to a Pipeline job.
Here's how to recover that ability using a git command and Pipeline's [`sh`][pipeline-sh-documentation] step.

[pipeline-sh-documentation]: https://jenkins.io/doc/pipeline/steps/workflow-durable-task-step/#sh-shell-script
