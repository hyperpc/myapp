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
          bat(script: 'mvn test', label: 'Test the compiled file')
          bat(script: 'mvn sonar:sonar install', label: 'Unit testing and Code Coverage')
          cobertura(autoUpdateHealth: true, autoUpdateStability: true, classCoverageTargets: 'target\\cobertura', coberturaReportFile: 'target\\cobertura\\*', failUnstable: true)
        }

        withSonarQubeEnv(installationName: 'SonarQube-Server', credentialsId: 'SonarQubeToken') {
          bat(label: 'SonarQube Analysis', script: 'C:\\workspace\\progm\\sonar-scanner-5.0.1.3006-windows\\bin\\sonar-scanner.bat -Dproject-settings=sonar-project.properties')
        }

        waitForQualityGate(credentialsId: 'SonarQubeToken', webhookSecretId: 'SonarQubeWebhook', abortPipeline: true)
        withMaven(jdk: 'JAVA_HOME', maven: 'MAVEN_HOME', publisherStrategy: 'IMPLICIT') {
          bat(script: 'mvn package -Dmaven.clean.skip=true -Dmaven.test.skip=true', label: 'Snapshot War Packing')
        }

        archiveArtifacts(artifacts: 'target/*.war', fingerprint: true, onlyIfSuccessful: true)
        script {
          def pom = readMavenPom file: 'pom.xml'
          VERSION = pom.version
          env.SVERSION = VERSION //Stable version value from pom
          echo env.SVERSION
        }

        script {
          withCredentials([
            usernamePassword(credentialsId: 'JFrogArtifactoryUsernamePwdID',
            usernameVariable: 'username',
            passwordVariable: 'password')
          ]) {
            print 'username=' + username + ' password=' + password

            //Artifactory Credentials
            env.username = username
            env.password = password

            //Batch command to Upload artifactory using above credentials
            bat(script: 'jfrog rt u "target/demo-*.war" myapp/samples/%SVERSION%/ --user=%username% --password=%password% --url=http://localhost:8040/artifactory', label: 'Artifactory Upload')
          }
        }

      }
    }

  }
}