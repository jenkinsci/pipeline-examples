package com.cloudbees.plugins.maskpasswordsplugin

import hudson.util.Secret

class MaskPasswords {

  static void maskWithEnv(def steps, Map<String, String> secrets, Closure body) {
    String[] secret_keys = secrets.keySet() as String[] 
    List masks = [] 
    secret_keys.each { masks << [var: it, password: secrets[it]] } 
    List envs = [] 
    secret_keys.each { envs << "${it}=${Secret.fromString(secrets[it])}" } 
    steps.wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: masks]) { 
      steps.withEnv(envs) { 
        body.call()
      } 
    }		 
  }
}
