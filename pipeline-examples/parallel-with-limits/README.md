# Synopsis
Demonstrate an example of a Jenkinsfile to execute a large number of jobs in parallel, while
limiting the total number of jobs that can run concurrently

# Background

The requirement was to execute over 200 long running free style jobs with a limited number of resources. With 40 threads, it took 13 hours to complete the execution of all jobs  
- Multiples of this pipeline needed to run during the day on different branches
- It was necessary to prevent a single pipeline from consuming all available threads at one time
- It was required that users be allowed stop the pipeline and all child jobs easily
- It was required to dynamically build the list of jobs to execute. Jobs are added and removed frequently
- It was desired to keep to a minumum the number jobs added to the Jenkins job queue at one time
- All jobs run in a shared pool of VMs used by different teams. It is not possible to manually assign and maintain labels for each team. VMs must be able to be added and removed from the shared pool as needed.
- The Throttle plugin was used for many years, but the performance of the plugin became an issue as the number of nodes grew past 50 and the number of jobs exceded several hundreds


Note: other locking/limiting mechanisms were explored, but the Lockable Resources plugin ended up being the most elegant solution. 
If the Lockable Resources plugin allowed the creation of multiple ephemeral resources on the fly, the sample pipeline would be even simpler. At the moment this does not work: lock(label: "mylabel", quantity: 5){}