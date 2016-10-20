checkout scm: [	$class: 'GitSCM', 
				branches: [[name: '*/master']], 
				userRemoteConfigs: [[url: 'https://github.com/jglick/simple-maven-project-with-tests']], 
				extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'bla']]
]