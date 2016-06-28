def labels = ['precise', 'trusty'] // labels for Jenkins node types we will build on
def builders = [:]
for (x in labels) {
    def label = x
    builders[label] = {
      node(label) {
	// build steps that should happen on all nodes go here
      }
    }
}

parallel builders
