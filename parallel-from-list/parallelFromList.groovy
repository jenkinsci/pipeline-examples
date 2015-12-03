// While you can't use Groovy's .collect or similar methods currently, you can
// still transform a list into a set of actual build steps to be executed in
// parallel.

// Our initial list of strings we want to echo in parallel
def stringsToEcho = ["a", "b", "c", "d"]

// The map we'll store the parallel steps in before executing them.
def stepsForParallel = [:]

// The standard 'for (String s: stringsToEcho)' syntax also doesn't work, so we
// need to use old school 'for (int i = 0...)' style for loops.
for (int i = 0; i < stringsToEcho.size(); i++) {
    // Get the actual string here.
    def s = stringsToEcho.get(i)

    // Transform that into a step and add the step to the map as the value, with
    // a name for the parallel step as the key. Here, we'll just use something
    // like "echoing (string)"
    def stepName = "echoing ${s}"
    
    stepsForParallel[stepName] = transformIntoStep(s)
}

// Actually run the steps in parallel - parallel takes a map as an argument,
// hence the above.
parallel stepsForParallel

// Take the string and echo it.
def transformIntoStep(inputString) {
    // We need to wrap what we return in a Groovy closure, or else it's invoked
    // when this method is called, not when we pass it to parallel.
    // To do this, you need to wrap the code below in { }, and either return
    // that explicitly, or use { -> } syntax.
    return {
        node {
            echo inputString
        }
    }
}