# Synopsis
Demonstrate an example of a Jenkinsfile to execute a large number of jobs in parallel, while
limiting the total number of jobs that can run concurrently

# Background

The requirement was to execute over 200 long running free style jobs with a limited number of resources.  
- Multiples of this pipeline needed to run during the day on different branches
- With 40 threads, it took 13 hours to complete the execution of all jobs
- It was necessary to prevent a single pipeline from consuming all available threads at one time
- It was necessary to allow exection of jobs from the same pipeline on other branches at the same time
- It was required that users be allowed stop the pipeline and all child jobs easily
- It was required to dynamically build the list of jobs to execute as jobs are added and removed frequently
- It was desired to keep to a minumum the number jobs added to the Jenkins job queue
- The Throttle plugin was used for many years, but the performance of the plugin became an issue as the number of nodes grew past 50 and the number of jobs exceded several 100


Note: other locking/limiting mechanisms were explored, but the Lockable Resources plugin ended up being the most elegant solution. 
A missing feature of the Lockable Resources plugin, to allow the creation of multiple ephemeral resources on the fly, would simplify the pipeline code even more. This does not work: lock(label: "mylabel", quantity: 5){}