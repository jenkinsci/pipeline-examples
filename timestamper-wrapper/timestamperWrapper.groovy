// This shows a simple build wrapper example, using the Timestamper plugin.
node {
    // This is the current syntax for invoking a build wrapper, naming the class.
    wrap([$class: 'TimestamperBuildWrapper']) {
        // Just some echoes to show the timestamps.
        stage "First echo"
        echo "Hey, look, I'm echoing with a timestamp!"

        // A sleep to make sure we actually get a real difference!
        stage "Sleeping"
        sleep 30

        // And a final echo to show the time when we wrap up.
        stage "Second echo"
        echo "Wonder what time it is now?"
    }
}