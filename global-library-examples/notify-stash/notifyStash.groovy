/**
 * Notify stash of build status for commit
 *
 * @param status{String} INPROGRESS | SUCCESSFUL | FAILED
 * @param message{String} Message to record in stash
 * @param gitCommit{String} Full hash of git commit
 */
def call( status, message = "", gitCommit )
{
    env.USERNAME = env.PASSWORD = ""

    def postData = """{
            "state": "$status",
            "key": "$env.JOB_NAME #$env.BUILD_NUMBER",
            "url": "$env.BUILD_URL",
            "description": "Built by Jenkins: $message"
        }"""

    // CREDENTIAL_ID_IN_JENKINS should be replaced with your stash user credentials
    // that have been stored in jenkins.  

    withCredentials([[$class: 'UsernamePasswordMultiBinding',
                      credentialsId: CREDENTIAL_ID_IN_JENKINS,
                      usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']])
            {
                def stashUrl = "https://YOUR_STASH_SERVER/rest/build-status/1.0/commits/$gitCommit"
                def headers = "-H \"Content-Type: application/json\""
                def credentials = "--user $env.USERNAME:$env.PASSWORD"
                sh "curl $credentials $headers -X POST -d '$postData' $stashUrl"
            }
}
