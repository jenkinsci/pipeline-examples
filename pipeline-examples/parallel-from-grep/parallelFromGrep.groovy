import jenkins.model.*

// While you can't use Groovy's .collect or similar methods currently, you can
// still transform a list into a set of actual build steps to be executed in
// parallel.

def stepsForParallel = [:]

// Since this method uses grep/collect it needs to be annotated with @NonCPS
// It returns a simple string map so the workflow can be serialized
@NonCPS
def jobs(jobRegexp) {
  Jenkins.instance.getAllItems()
         .grep { it.name ==~ ~"${jobRegexp}"  }
         .collect { [ name : it.name.toString(),
                      fullName : it.fullName.toString() ] }
}

j = jobs('test-(dev|stage)-(unit|integration)')
for (int i=0; i < j.size(); i++) {
    stepsForParallel["${j[i].name}"] = transformIntoStep(j[i].fullName)
}

// Actually run the steps in parallel - parallel takes a map as an argument,
// hence the above.
parallel stepsForParallel

// Take the string and echo it.
def transformIntoStep(jobFullName) {
    // We need to wrap what we return in a Groovy closure, or else it's invoked
    // when this method is called, not when we pass it to parallel.
    // To do this, you need to wrap the code below in { }, and either return
    // that explicitly, or use { -> } syntax.
    return {
       // Job parameters can be added to this step
       build jobFullName
    }
}
