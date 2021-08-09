pipeline {
    agent any
    
    parameters {
      text defaultValue: 'anonymous', description: 'my name', name: 'name'
      booleanParam defaultValue: true, description: 'whether skip this', name: 'skip'
    }

    stages{
        stage("one"){
            steps{
                echo params.name
            }
        }
        
        stage("two"){
            when{
                anyOf{
                    expression{
                        return params.skip
                    }
                }
            }
            steps{
                echo "stage two"
            }
        }
    }
}
