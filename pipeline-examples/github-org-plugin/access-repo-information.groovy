#!groovy

    // github-organization-plugin jobs are named as 'org/repo/branch'
    // we don't want to assume that the github-organization job is at the top-level
    // instead we get the total number of tokens (size) 
    // and work back from the branch level Pipeline job where this would actually be run
    // Note: that branch job is at -1 because Java uses zero-based indexing
    tokens = "${env.JOB_NAME}".tokenize('/')
    org = tokens[tokens.size()-3]
    repo = tokens[tokens.size()-2]
    branch = tokens[tokens.size()-1]

