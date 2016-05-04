# Synopsis

An example showing how to build a standard maven project with specific
versions for Maven and the JDK.

It shows how to use the `withEnv` step to define the right `PATH` to use the tools.

# Caveats

* in `tool 'thetool'`, the `thetool` string **must** match a defined
  tool in your Jenkins installation.
