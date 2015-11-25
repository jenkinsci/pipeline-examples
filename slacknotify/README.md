# Synopsis
Use a slack webhook to send an arbitrary message.

# Background
Using a combination of groovy and curl from shell, send a message to slack for notifications.
Some of the more friendly groovy http libs like HTTPBuilder are not easily available. However,
we can use groovy's built in json handling to build up the request and ship it to a command
line curl easily enough.

This will require that you configure a webhook integration in slack (not the Jenkins specific configuration.)
