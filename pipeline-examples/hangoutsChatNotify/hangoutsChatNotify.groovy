import groovy.json.JsonOutput
// Add whichever params you think you'd most want to have
// replace the chatURL below with the hook url provided by
// Hangouts when you configure the webhook
def notifyHangoutsChat(text) {
    def chatURL = 'https://chat.googleapis.com/v1/spaces/xxxxxxx/messages?key=API_KEY'
    def payload = JsonOutput.toJson([text      : text])
    sh "curl -X POST ${chatURL} \ -H 'Content-Type: application/json' \ -d '${payload}'"
}