# Synopsis

An example showing how to take a list of objects and transform it into
a map of steps to be run with the parallel command.

# Caveats

* There is no need for the generation of the step itself to be in a
separate method. I've opted to do so here to show how to return a step
closure from a method.
