# Introduction

In this example a multibranch Pipeline is used to automaticly build and publish APKs based on the name of the branch.   It uses a regexp to pull the flavor name out the branch name and passes to to the gradle build.

Useful if you have a lot of apps with a lot of flavors in a single code base.


## Instructions

* Setup a multibranch Jenkins Pipeline build.  Have it scan for branches based on a naming convention. This example uses QA_<flavor-name>, where <flavor-name> is the name of your flavor.
* Place this JenkinsFile in the root of your project.
* Push a branch matching your naming convention.

That's it!