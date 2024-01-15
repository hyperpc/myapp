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
            print 'env.GIT_COMMIT=' + env.GIT_COMMIT
            //Artifactory Credentials
            env.username = username
            env.password = password

            //Batch command to Upload artifactory using above credentials
            bat(script: 'jfrog rt u "target/demo-*.war" myapp/samples/%SVERSION%/ --user=%username% --password=%password% --url=http://localhost:8040/artifactory', label: 'Artifactory Upload')
          }
        }

      }
    }

    stage('DEV') {
      steps {
        script {
          withCredentials([
            usernamePassword(credentialsId: 'JFrogArtifactoryUsernamePwdID',
            usernameVariable: 'username',
            passwordVariable: 'password')
          ]) {
            //Batch command to Upload artifactory using above credentials
            bat(script: 'jfrog rt dl myapp/samples/%SVERSION%/target/demo-*.war --user=%username% --password=%password% --url=http://localhost:8040/artifactory', label: 'Artifactory Download')
          }
        }

      }
    }

    stage('Deploy to QA?') {
      steps {
        input(message: 'Are you sure you want to deploy to QA environment?', id: 'QA', ok: 'QA Deploy', submitter: 'admin', submitterParameter: 'admin')
      }
    }

    stage('QA') {
      steps {
        script {
          def pom = readMavenPom file:'pom.xml'
          VERSION = pom.version.replaceAll('-SNAPSHOT','')
          env.RVERSION=VERSION
          println env.RVERSION
        }

        steps() {
          echo 'QA release...'
        }

        withMaven(jdk: 'JAVA_HOME', maven: 'MAVEN_HOME') {
          bat(script: 'mvn release:prepare release:perform -Dmaven.clean.skip=true -Dmaven.test.skip=true -Dmaven.delpoy.skip=true', label: 'Maven Release')
        }

        echo 'jfrog upload: Release Upload Artifactory'
        bat(script: 'jfrog rt u "QA/target/demo-*.war" myapp/samples/%SVERSION%/ --user=%username% --password=%password% --url=http://localhost:8040/artifactory', label: 'Release Upload Artifactory')
        echo 'jfrog upload: Release Package Download from Artifactory'
        bat(script: 'jfrog rt dl myapp/samples/%SVERSION%/QA/target/demo-*.war --user=%username% --password=%password% --url=http://localhost:8040/artifactory', label: 'Release Package Download from Artifactory')
      }
    }

  }
}