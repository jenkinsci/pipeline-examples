#!groovy

node {
    stage('configFile Plugin') {

        // 'ID' refers to alpha-numeric value generated automatically by Jenkins.
        // This code snippet assumes that the config file is stored in Jenkins.

        // help to assign the ID of config file to a variable, this is optional 
        // as ID can be used directly within 'configFileProvider' step too.
        def mycfg_file = '<substitute-alpha-numeric-value-cfgfille-here-within-quotes>'

        // whether referencing the config file as ID (directly) or via user-defined 
        // variable, 'configFileProvider' step enables access to the config file
        // via 'name' given for the field, 'variable:'
        configFileProvider([configFile(fileId: mycfg_file, variable: 'PACKER_OPTIONS')]) {
            echo " =========== ^^^^^^^^^^^^ Reading config from pipeline script "
            sh "cat ${env.PACKER_OPTIONS}"
            echo " =========== ~~~~~~~~~~~~ ============ "
 
            // Access to config file opens up other possibilities like
            // passing on the configuration to an external script for other tasks, like,
            // for example, to set generic options that can be used for generating 
            // binary images using packer.
            echo " =========== ^^^^^^^^^^^^ Reading config via Python... "
            sh "python build_image.py ${env.PACKER_OPTIONS}"
            echo " =========== ~~~~~~~~~~~~ ============ "
        }
    }
}
