// There is no direct access to the build Causes from the Pipeline, but you can
// get this by using the `currentBuild.rawBuild` variable, as shown below.

// Get all Causes for the current build
def causes = currentBuild.rawBuild.getCauses()

// Get a specific Cause type (in this case the user who kicked off the build),
// if present.
def specificCause = currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause)

// If you see errors regarding 'Scripts not permitted to use method...' approve 
// these scripts at JENKINS_URL/scriptApproval/ - the UI shows the blocked methods 

// See the Javadoc for Cause for more information on what's in Causes, etc at:
// http://javadoc.jenkins-ci.org/hudson/model/class-use/Cause.html
