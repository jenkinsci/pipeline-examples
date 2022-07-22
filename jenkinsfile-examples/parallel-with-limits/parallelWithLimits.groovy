/*
This pipeline will execute all jobs in the folder that contains it.
This pipeline uses the LockableResources plugin to control how many parallel jobs run at the same time.
The maximum number of jobs that can run at the same time is specified by the optional RESOURCES parameter.
The optional LABEL parameter can be used to customize how resources are grouped.
If this job is stopped, all child jobs started by this job will be stopped by Jenkins.
 */

import org.jenkins.plugins.lockableresources.LockableResourcesManager

node {
    // the label of all resources used by this pipeline
    def label = params.getOrDefault('LABEL', currentBuild.rawBuild.project.parent.name)
    println "LABEL: $label"
    
    // number of resources to be used by this pipeline
    def resources = Integer.valueOf(params.getOrDefault('EXECUTORS', 5))
    println "RESOURCES: $resources"
    
    // the list of stages that will be executed in parallel
    def parallelStages = [:]

    stage('Prepare') {
        // setup lockable resources needed for this pipeline
        setupLockableResources(label, resources)
      
        // list of jobs to be executed
        def jobs = currentBuild.rawBuild.project.parent.items.findAll({
            project -> project.name != currentBuild.rawBuild.project.name && !project.isDisabled()
        }).collect { job -> return job.name }

        println "Total jobs to run: ${jobs.size()}"

        jobs.each() { job ->
            parallelStages[job] = {
                stage(job) {
                    // Lock one of the resources assigned the specified label
                    lock(label: label, quantity: 1) {
                        build(job: job, wait: true, propagate: false, quietPeriod: 10)
                    }
                }
            }
        }
    }

    stage('Run') {
        // run all stages in parallel
        parallel parallelStages
    }
}

// Setup lockable ephemeral resources that will be deleted after the pipeline completes.
// Ephemeral resources are not configurable via the Global Configuration screen,
// this is an advantage when working with large number of resources.
@NonCPS
def setupLockableResources(label, executors) {
    println "Setting up lockable resources for this build"
    def lrm = LockableResourcesManager.get()
    for (i = 0; i < executors; i++) {
        lrm.createResourceWithLabel("${label}-$i", label)
        // Get lockable resource by name
        def lockableResource = lrm.fromName("${label}-$i")
        // Make resource ephemeral, resources will be automatically deleted when this pipeline ends
        lockableResource.setEphemeral(true)
    }
}
