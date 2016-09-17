# Introduction
This is a collection of tips, advice, gotchas and other best practices for using the [Jenkins Pipeline plugin](https://github.com/jenkinsci/workflow-plugin/blob/master/README.md). Contributions and comments are happily accepted.

# General tips and advice
* Do everything that makes sense there within `stage`s. This will make your builds easier to visualize, debug, etc.
* Do all real work that involves running a shell script, building, etc, within `node` blocks, so that it actually happens on a real executor, rather than a flyweight executor on the master node.
* Get your flows from source control - `Jenkinsfile`s, loading libraries, global CPS library, you name it - but if you pull the main flow from SCM (i.e., Multibranch with `Jenkinsfile`s or `Pipeline script from SCM`), be aware that you may need to whitelist a lot of method calls in the script security plugin. [JENKINS-28178](https://issues.jenkins-ci.org/browse/JENKINS-28178) has been created to simplify this, but you'll still want to be careful with that functionality due to the open security it implies.
* `input` shouldn’t be done within a `node` block - that eats up two executors waiting on the `input` feedback, both the flyweight executor that’s used for the `input` itself and the real executor used by the `node` block, which won’t free up until after the `input` itself has completed.

# Parallelism
* Within `parallel` blocks, use `node` blocks to make sure you farm out to real nodes for your parallelized work.
* Nested `parallel` blocks can lead to swamping your available executors, as each execution of the first `parallel` block calls multiple executions of the second `parallel` block, and so on. In general, think carefully about your parallelism and your available executors when using `parallel`.
* The [Parallel Test Executor plugin](https://github.com/jenkinsci/parallel-test-executor-plugin) is awesome and can be immensely helpful both for distributing your test execution and for throttling your parallelism, since you define how many "buckets" your tests get divided into.
* Don’t put `stage`s in `parallel` blocks - that just goes *weird*, breaking a lot of logic in the Stage View and elsewhere. Save yourself the pain - don't do it!

# `Jenkinsfile`s and Multibranch
* Use `checkout scm` to automatically checkout current revision of branch
* Use `$env.BRANCH_NAME` variable if you have logical difference in your flow between branches, i.e. to distinguish different behavior for production-ready branches versus sandbox or pull request branches.
* For `Jenkinsfile`s, make sure to put `#!/usr/bin/env groovy` at the top of the file so that IDEs, GitHub diffs, etc properly detect the language and do syntax highlighting for you.
 * But note that this doesn't mean you can run "groovy Jenkinsfile" or "./Jenkinsfile" - Pipeline doesn't run standalone! This is just a trick to help in your IDE, etc.

# Groovy gotchas
* Don’t have the Groovy interpreter making blocking i/o calls, i.e., `HTTPClient` and the like - these can cause real problems with resumability, and also require a lot of explicit whitelisting of methods in the Script Security plugin, which is not ideal.
* Beware `for (Foo f: foos)` loops and Groovy closure-style operators like `.each` and the like. They will not work right in normal Pipeline script contexts where Pipeline steps are involved directly.
* If you need to do that kind of thing to make your scripting make sense, do it in methods annotated with `@NonCPS`. These methods will not be CPS-transformed, so can do some things that just can't be done in the serializable/resumable CPS context.
* Don’t use the Groovy scripting in place of shell scripting - work coming for the ability to run a Groovy step on the node as with the normal Groovy plugin build step, but until then, shell out, even if it’s just to do `sh 'groovy foo.groovy'`.
* If you really need `Map`s in your normal Pipeline scripts, consider using arrays of `[key,value]` instead - this way, you can use C-style for loops (i.e., `for (int i = 0; i < mapArray.size(); i++) { def entry = mapArray.get(i); ... }`. You can create such an array from a `Map` using this helper method:
```groovy
@NonCPS def entries(m) {m.collect {k, v -> [k, v]}}
```

# Pipeline script development tips
* When developing new flows, you can often iterate faster with an inline pipeline, rather than running from SCM. You can use the 'load' operation to load common utility methods from common pipelines, and then as you finish out methods, commit them to the utility flows.  This lets you strike a balance between having traceability on commits and being able to move fast.
 * NOTE: this isn't possible with Multibranch pipelines, since those *have* to pull their script from SCM completely, so you will probably want to do your initial development iteration on a single branch using this approach before moving to `Jenkinsfile`s.





