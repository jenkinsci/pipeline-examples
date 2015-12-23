def branches = [:]
for (int i = 0; i < 4; i++) {
  def which = "${i}" 
  branches["branch${i}"] = {
    build job: 'test_jobs', parameters: [[$class: 'StringParameterValue', name: 'queue', value:   
      'test_praam'], [$class: 'StringParameterValue', name:'dummy', value: which]]
    }
} 
parallel branches

