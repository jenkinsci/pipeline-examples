[configFile Provider plugin](https://plugins.jenkins.io/config-file-provider) enables provisioning of various types of configuration files. Plugin works in such a way as to make the configuration available for the entire duration of the build across all the build agents that are used to execute the build.

Common scenarios that demand the usage of configuration files:

- Provide properties that can be consumed by the build tool
- Global settings that override local settings
- Details of credentials needed to access repos
- Inputs to generate binary images that need to be tailored to specific architectures 

The example shows simple usage of configFile Provider plugin and howto access it's contents.
