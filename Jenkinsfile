pipeline {
  agent {
    node {
      label 'main'
    }

  }
  stages {
    stage('PreBuild') {
      agent {
        node {
          label 'main'
        }

      }
      steps {
        echo 'Pre-Build Message~'
      }
    }

    stage('Continuous Integration') {
      agent {
        node {
          label 'main'
        }

      }
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