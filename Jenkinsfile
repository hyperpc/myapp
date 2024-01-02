pipeline {
  agent any
  stages {
    stage('PreBuild') {
      agent any
      steps {
        echo 'Pre-Build Message~'
      }
    }

    stage('Continuous Integration') {
      agent any
      steps {
        withMaven(jdk: 'JAVA_HOME', maven: 'MAVEN_HOME') {
          bat(script: 'mvn clean', label: 'Clean output path')
          bat(script: 'mvn compile', label: 'Compile source code')
          bat(script: 'mvn sonar:sonar install', label: 'Unit testing and Code Coverage')
          cobertura(autoUpdateHealth: true, autoUpdateStability: true, classCoverageTargets: 'target\\cobertura', coberturaReportFile: 'target\\cobertura\\*', failUnstable: true)
          withSonarQubeEnv(installationName: 'SonarQube-Server', credentialsId: 'SonarQubeToken') {
            bat(label: 'SonarQube Analysis', script: 'sonar-scanner -Dproject-settings=sonar-project.properties')
          }

          waitForQualityGate(abortPipeline: true, credentialsId: 'SonarQubeToken', webhookSecretId: 'SonarQubeWebhook')
        }

      }
    }

  }
}