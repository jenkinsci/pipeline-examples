# Introduction
This is a collection of tips, advice, gotchas and other best practices for using the [Jenkins Pipeline plugin](https://github.com/jenkinsci/workflow-plugin/blob/master/README.md). Contributions and comments are happily accepted.

# General tips and advice
* Do everything that makes sense there within `stage`s. This will make your builds easier to visualize, debug, etc.
* Do all real work that involves running a shell script, building, etc, within `node` blocks, so that it actually happens on a real executor, rather than a flyweight executor on the master node.
* Get your flows from source control - `Jenkinsfile`s, loading libraries, global [CPS](https://en.wikipedia.org/wiki/Continuation-passing_style) library, you name it - but if you pull the main flow from SCM (i.e., Multibranch with `Jenkinsfile`s or `Pipeline from SCM`), be aware that you may need to whitelist a lot of method calls in the script security plugin. By getting your flows from source control, you benefit from `Jenkinsfile` versioning and also testing and merging against your CD Pipeline definition.
* `input` shouldn’t be done within a `node` block. For `input` step is recommended to use `timeout` in order to avoid waiting for an infinite amount of time, and also control structures (`try/catch/finally`).
* As Pipeline usage is adopted for multiple projects and teams in an organization, common patterns should be stored in [Shared Libraries](https://jenkins.io/doc/book/pipeline/shared-libraries/). It is also an escape value for allowing out-of-sandbox execution in a safe context.
* When writing functions, use unique names in your pipeline script and avoid using built-in/pre-defined items (such as "build", "stage", etc).  Using pre-defined methods may result in runtime issues, such as generating a `sandbox.RejectedAccessException` error when using build job DSL. 
* Make use of the available [Pipeline Development Tools](https://jenkins.io/doc/book/pipeline/development/#pipeline-development-tools) for debugging your Pipeline as code.
* Use [Multibranch Pipeline](https://jenkins.io/doc/book/pipeline/multibranch/) for project collaboration, new features (developed in separate branches) are validated before merging them to the master branch. Besides, it comes with automating features out-of-the-box (webhooks).
 
# Parallelism
* Within `parallel` blocks, use `node` blocks to make sure you farm out to real nodes for your parallelized work.
* Nested `parallel` blocks can lead to swamping your available executors, as each execution of the first `parallel` block calls multiple executions of the second `parallel` block, and so on. In general, think carefully about your parallelism and your available executors when using `parallel`.
* The [Parallel Test Executor plugin](https://github.com/jenkinsci/parallel-test-executor-plugin) is awesome and can be immensely helpful both for distributing your test execution and for throttling your parallelism, since you define how many "buckets" your tests get divided into.
* Don’t put `stage`s directly inside `parallel` blocks - that just goes *weird*, breaking a lot of logic in the Stage View and elsewhere. Save yourself the pain - don't do it!

# `Jenkinsfile`s and Multibranch
* Use `checkout scm` to automatically checkout current revision of branch
* Use `$env.BRANCH_NAME` variable if you have logical difference in your flow between branches, i.e. to distinguish different behavior for production-ready branches versus sandbox or pull request branches.
* For `Jenkinsfile`s, make sure to put `#!/usr/bin/env groovy` at the top of the file so that IDEs, GitHub diffs, etc properly detect the language and do syntax highlighting for you.
 * But note that this doesn't mean you can run "groovy Jenkinsfile" or "./Jenkinsfile" - Pipeline doesn't run standalone! This is just a trick to help in your IDE, etc.

# Groovy gotchas
* Don’t have the Groovy interpreter making blocking i/o calls, i.e., `HTTPClient` and the like - these can cause real problems with resumability, and also require a lot of explicit whitelisting of methods in the Script Security plugin, which is not ideal.
* Don’t use the Groovy scripting in place of shell scripting - work coming for the ability to run a Groovy step on the node as with the normal Groovy plugin build step, but until then, shell out, even if it’s just to do `sh 'groovy foo.groovy'`.

# Scripted Pipeline development tips
* When developing new flows, you can often iterate faster with an inline pipeline, rather than running from SCM. You can use the 'load' operation to load common utility methods from common pipelines, and then as you finish out methods, commit them to the utility flows.  This lets you strike a balance between having traceability on commits and being able to move fast.
 * NOTE: this isn't possible with Multibranch pipelines, since those *have* to pull their script from SCM completely, so you will probably want to do your initial development iteration on a single branch using this approach before moving to `Jenkinsfile`s.
