# Synopsis

The example shows how to trigger jobs on all Jenkins nodes from Pipeline.

Summary:
* The script uses [NodeLabel Parameter plugin](https://wiki.jenkins-ci.org/display/JENKINS/NodeLabel+Parameter+Plugin) to pass the job name to the payload job.
* Node list retrieval is being performed using Jenkins API, so it will require [script approvals](https://wiki.jenkins-ci.org/display/JENKINS/Script+Security+Plugin) in the Sandbox mode
