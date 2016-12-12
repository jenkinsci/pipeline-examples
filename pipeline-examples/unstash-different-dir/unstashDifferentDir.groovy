// First we'll generate a text file in a subdirectory on one node and stash it.
stage "first step on first node"

// Run on a node with the "first-node" label.
node('first-node') {
    // Make the output directory.
    sh "mkdir -p output"

    // Write a text file there.
    writeFile file: "output/somefile", text: "Hey look, some text."

    // Stash that directory and file.
    // Note that the includes could be "output/", "output/*" as below, or even
    // "output/**/*" - it all works out basically the same.
    stash name: "first-stash", includes: "output/*"
}

// Next, we'll make a new directory on a second node, and unstash the original
// into that new directory, rather than into the root of the build.
stage "second step on second node"

// Run on a node with the "second-node" label.
node('second-node') {
    // Run the unstash from within that directory!
    dir("first-stash") {
        unstash "first-stash"
    }

    // Look, no output directory under the root!
    // pwd() outputs the current directory Pipeline is running in.
    sh "ls -la ${pwd()}"

    // And look, output directory is there under first-stash!
    sh "ls -la ${pwd()}/first-stash"
}
