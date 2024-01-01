pipeline {
  agent {
    node {
      label 'main'
    }

  }
  stages {
    stage('PreBuild') {
      steps {
        echo 'Pre-Build Message~'
      }
    }

    stage('Continuous Integration') {
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