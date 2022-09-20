// As of Pipeline Supporting APIs v2.22, there is a whitelisted API to access
// build causes as JSON that is available inside of the Pipeline Sandbox.

// Get all Causes for the current build
def causes = currentBuild.getBuildCauses()

// Get a specific Cause type (in this case the user who kicked off the build),
// if present.
def specificCause = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')

// The JSON data is created by calling methods annotated with `@Exported` for
// each Cause type. See the Javadoc for specific Cause types to check exactly
// what data will be available.
// For example, for a build triggered manually by a specific user, the resulting
// JSON would be something like the following:
//
// [
//  { 
//    "_class\": "hudson.model.Cause$UserIdCause",
//    "shortDescription": "Started by user anonymous",
//    "userId": "tester",
//    "userName": "anonymous"
//  }
// ]
// cf. https://javadoc.jenkins-ci.org/hudson/model/Cause.UserIdCause.html
