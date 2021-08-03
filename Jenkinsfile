pipeline { 
    agent any 
    
    tools { 
	    maven 'Maven 3.8.1' 
	    jdk 'jdk1.8' 
    }
	
    environment {
	    COMMIT_HASH = "${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"
	    
	    registry = "202447729588.dkr.ecr.us-east-2.amazonaws.com/ucm_lib"
	 
	   // AWS_ID = credentials('AWS_ID')
	    
	    IMG_NAME = "ucmlib"
	    scannerHome = tool 'SonarqubeScanner'
    }
    
    stages { 
	      stage ('Checkout Git Repo') {
	        steps {
	                git branch: 'master', url: 'https://github.com/Utopian-CashMoney/ucm_lib.git'            
	        }
	    }
	    
          stage ('Unit Tests') {
            
            steps {
            
                  sh 'mvn clean test'        
            }
        }
	
	stage ('SonarQube Analysis') {
            
	     tools {
			jdk 'jdk11'
		}
		
             steps {
                      withSonarQubeEnv('Sonarqube') {
                          sh 'mvn sonar:sonar'
                      }
                  }
        }    
	    
        stage ('Build') {
            
            steps {
            
                  sh 'mvn clean package' 
                
            }
        }
	    
       stage("Docker Build") {
	   steps {
	        echo "Docker Build...."
		   		   
		sh "docker build --tag ${IMG_NAME}:${COMMIT_HASH} ."
		  		   
	   }
	       
	}
	    
	stage('Pushing to ECR') {
		
             steps{ 
		    
                sh 'aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 202447729588.dkr.ecr.us-east-2.amazonaws.com'
		     		
		 
		// Here we are tagging the docker image with the new name which is ECR name and giving the image name which will be stored in ECR   
		sh "docker tag ${IMG_NAME}:${COMMIT_HASH} ${registry}:${COMMIT_HASH}"
    		
		// We are pushing the new created docker image from our initial docker image to ECR
		 sh "docker push ${registry}:${COMMIT_HASH}"
		     
         
             }
		
      }
	    
  }
	    
	post {
	  always {
	      sh 'mvn clean'
	      sh "docker system prune -f"
	   }
      }
}
