// in this array we'll place the jobs that we wish to run
def branches = [:]

//running the job 4 times concurrently
//the dummy parameter is for preventing mutation of the parameter before the execution of the closure.
//we have to assign it outside the closure or it will run the job multiple times with the same paraemter "4"
//and jenkins will unite them into a single run of the job 

for (int i = 0; i < 4; i++) {
  branches["branch${i}"] = {
    build job: 'test_jobs', parameters: [[$class: 'StringParameterValue', name: 'param1', value:   
      'test_param'], [$class: 'StringParameterValue', name:'dummy', value: "${i}"]]
    }
} 
parallel branches

