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
          bat 'mvn clean'
          bat(script: 'mvn sonar:sonar', label: 'Unit testing and Code Coverage')
          cobertura(autoUpdateHealth: true, autoUpdateStability: true, classCoverageTargets: 'target\\cobertura', coberturaReportFile: 'target\\cobertura\\*', failUnstable: true)
        }

      }
    }

  }
}