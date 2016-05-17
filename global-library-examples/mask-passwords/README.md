# Synopsis
This is a workaround to inject mask passwords variables in
the build environment meanwhile JENKINS-34264 is not fixed.

These files could be deployed in your workflowLib:

src/com/cloudbees/plugins/maskpasswordsplugin/MaskPasswords.groovy
vars/maskWithEnv.groovy

And a example of use:

```
node { 
    Map<String, String> variables = new HashMap();

    variables.put("FOO","123456")
    variables.put("param1","654321")
    variables.put("param2","qwerty")

    maskWithEnv(variables) {
        sh 'echo FOO=$FOO'
        echo "$env.param1"
    }
    
    if (env.param1 == null) {
        echo "not available here!"
    }
}
```
