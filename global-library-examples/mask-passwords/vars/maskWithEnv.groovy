// vars.maskWithEnv.groovy
def call(Map<String, String> secrets, Closure body) {
	com.cloudbees.plugins.maskpasswordsplugin.MaskPasswords.maskWithEnv(steps, secrets, body)
}
