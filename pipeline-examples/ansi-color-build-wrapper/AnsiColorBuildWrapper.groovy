// This shows a simple build wrapper example, using the AnsiColorBuildWrapper plugin.
node {
    // This is the current syntax for invoking a build wrapper, naming the class.
    wrap([$class: 'AnsiColorBuildWrapper']) {
        // Just some echoes to show the ANSI color.
        stage "\u001B[31mI'm Red\u001B[0m Now not"
    }
}