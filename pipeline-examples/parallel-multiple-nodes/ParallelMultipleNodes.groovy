def labels = ['precise', 'trusty'] // labels for Jenkins node types we will build on
def builders = [:]
for (x in labels) {
    def label = x
    // Create a map to pass in to the 'parallel' step so we can fire all the builds at once
    builders[label] = {
      node(label) {
	// build steps that should happen on all nodes go here
      }
    }
}

parallel builders
