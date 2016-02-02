# Synopsis

An example showing how to search for a list of existing jobs and
triggering all of them in parallel.

# Caveats

* Calling other jobs is not the most idiomatic way to use the Worflow DSL, 
however, the chance of re-using existing jobs is always welcome under certain
circumstances.

* Due to limitations in Workflow - i.e.,
[JENKINS-26481](https://issues.jenkins-ci.org/browse/JENKINS-26481) -
it's not really possible to use Groovy closures or syntax that depends
on closures, so you can't do the Groovy standard of using
.collectEntries on a list and generating the steps as values for the
resulting entries. You also can't use the standard Java syntax for For
loops - i.e., "for (String s: strings)" - and instead have to use old
school counter-based for loops.
* There is no need for the generation of the step itself to be in a
separate method. I've opted to do so here to show how to return a step
closure from a method.

