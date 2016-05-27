// The script triggers PayloadJob on every node.
// It uses Node and Label Parameter plugin to pass the job name to the payload job.
// The code will require approval of several Jenkins classes in the Script Security mode
def branches = [:]
def names = nodeNames()
for (int i=0; i<names.size(); ++i) {
  def nodeName = names[i];
  // Into each branch we put the pipeline code we want to execute
  branches["node_" + nodeName] = {
    node(nodeName) {
      echo "Triggering on " + nodeName
      build job: 'PayloadJob', parameters: [
              new org.jvnet.jenkins.plugins.nodelabelparameter.NodeParameterValue
                  ("TARGET_NODE", "description", nodeName)
          ]
    }
  }
}

// Now we trigger all branches
parallel branches

// This method collects a list of Node names from the current Jenkins instance
@NonCPS
def nodeNames() {
  return jenkins.model.Jenkins.instance.nodes.collect { node -> node.name }
}
