pipeline {

    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    agent any

    tools {
        gradle "Gradle-4.10.3"
        jdk "jdk11"
    }

    parameters {
        string(name: 'TAG_ROLLBACK', defaultValue: 'NO', description: 'Nome da Tag para voltar para produção.', )
    }

    environment {

        JRE = 'jre_11'
        REGISTRY = 'http://docker-images.dimed.com.br:5000'
//      GIT_CREDENTIAL = '4fb2c51e-7ad7-4a2b-861a-30698c7b3f6c' // Github Credential
        GIT_CREDENTIAL = 'c12c0b9a-156f-460d-a36d-586dad491e5c' // Gitlab Credential
        EMAIL_NOTIFICACAO='dsironi@dimed.com.br'

    }

    stages {

        stage('Checkout') {

            steps {

                git branch: "${GIT_BRANCH}", credentialsId: "${GIT_CREDENTIAL}", url: "${env.GIT_URL}"

            }

        }

        stage('Setup Envoriments') {

            steps {

                script {

                    env.BUILD_TAG  = env.BUILD_ID
                    env.gitlogs  = sh(returnStdout: true, script: 'git log --pretty=format:"%h - %an, %ar : %s" --graph --since=2.hour')
                    env.protocolo_git = sh(returnStdout: true, script: "echo ${env.GIT_URL} | awk -F // '{print \$1}' |  tr -d '[:space:]' ")
                    env.git_repo = sh(returnStdout: true, script: "echo ${env.GIT_URL} | awk -F // '{print \$2}' |  tr -d '[:space:]' ")
                    env.service = sh(returnStdout: true, script: "echo ${env.GIT_URL} | awk -F '/' '{print \$NF}' |  awk -F '.' '{print \$1}' |  tr -d '[:space:]' ")

                    echo "Branch Name : ${env.BRANCH_NAME}"

                    if (env.BRANCH_NAME == "master") {
                        checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: "${env.GIT_URL}", credentialsId:  "${GIT_CREDENTIAL}" ]]], poll: false
                        env.last_tag = sh(returnStdout: true, script: 'git tag --sort=creatordate | tail -n1 |  tr -d "[:space:]"')
                        env.tag_message = sh(returnStdout: true, script:"git shortlog --format='%s %h' ${env.last_tag}..master > /tmp/tagmessage.out.${env.BUILD_ID} && cat /tmp/tagmessage.out.${env.BUILD_ID}")
                        echo "env.last_tag:${env.last_tag}"
                    }

                    echo "env.service:${env.service}."
                    echo "env.tag_message:${env.tag_message}"

                }

            }

        }


        stage('Revert to TAG Application') {

            when {

                expression { params.TAG_ROLLBACK != 'NO' }

            }

            steps {

                script {

                      env.BUILD_TAG  = sh(returnStdout: true, script: "echo ${params.TAG_ROLLBACK} | cut -c2-100 |  tr -d '[:space:]' ")

                }

                echo env.BUILD_TAG
                checkout scm: [$class: 'GitSCM', userRemoteConfigs: [[url: "${env.GIT_URL}", credentialsId:  "${GIT_CREDENTIAL}" ]], branches: [[name: "refs/tags/${TAG_ROLLBACK}"]]], poll: false

            }
        }

        stage('Build') {

            steps {
                sh "export BUILD_ID=${env.BUILD_TAG} && ./gradlew clean build"
            }
        }

        stage('Artifact') {
            steps {
                dir("build/libs/") {
                    sh "pwd"
                    script {
                        env.artifact = sh(returnStdout: true, script: 'ls *.jar |  tr -d \'[:space:]\' ')
                    }
                    stash includes: "${env.artifact}", name: 'artefato'
                    archiveArtifacts artifacts: "${env.artifact}", fingerprint: true
                }
            }
        }

        stage('Build Docker Image') {

            when {

                expression { params.TAG_ROLLBACK == 'NO' }

            }

            steps {
                unstash 'artefato'

                script {

                    docker.withRegistry("${REGISTRY}") {
                        def serviceImage = docker.build("${env.service}:${env.BUILD_ID}","--build-arg JAR_FILE=${env.artifact} -f Dockerfile .")
                        serviceImage.push()
                    }
                }
            }
        }

        stage ('Deploy [DEV]') {
            steps {
                build job: 'deploy-management/master', parameters: [
                    string(name: 'SERVICE_NAME',     value: "${env.service}"),
                    string(name: 'ACTION',           value: "services"),
                    string(name: 'ENV',              value: "dev"),
                    string(name: 'OPERATION',        value: "deploy"),
                    string(name: 'JRE',              value: "${JRE}"),
                    string(name: 'URL_JENKINS',      value: "${BUILD_URL}"),
                    string(name: 'TAG',              value: "${BUILD_TAG}"),
                    string(name: 'ARTIFACT',         value: "${env.artifact}"),
                ]
            }
        }

        stage ('Deploy [HML]') {
            steps {
                timeout(time:1, unit:'MINUTES') {
                    input message: 'Deseja Fazer o deploy em homologação?'
                }
                build job: 'deploy-management/master', parameters: [
                    string(name: 'SERVICE_NAME',     value: "${env.service}"),
                    string(name: 'ACTION',           value: "services"),
                    string(name: 'ENV',              value: "hml"),
                    string(name: 'OPERATION',        value: "deploy"),
                    string(name: 'JRE',              value: "${JRE}"),
                    string(name: 'URL_JENKINS',      value: "${BUILD_URL}"),
                    string(name: 'TAG',              value: "${BUILD_TAG}"),
                    string(name: 'ARTIFACT',         value: "${env.artifact}"),
                ]
            }
        }

        stage('Release Application') {
            when {
                expression { params.TAG_ROLLBACK == 'NO' }
            }
            steps {
                timeout(time:1, unit:'MINUTES') {
                    input message: 'Deseja fazer a release?'
                }
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: "${GIT_CREDENTIAL}", usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
                    sh "git tag -a v${env.BUILD_ID} -F /tmp/tagmessage.out.${env.BUILD_ID}"
                    sh "git push ${env.protocolo_git}//${env.GIT_USERNAME}:${env.GIT_PASSWORD}@${env.git_repo} --tags "
                }
            }
        }

        stage ('Deploy [PRD]') {
            steps {
                timeout(time:1, unit:'MINUTES') {
                    input message: 'Deseja Fazer o deploy em produção?'
                }

                build job: 'deploy-management/master', parameters: [
                    string(name: 'SERVICE_NAME',     value: "${env.service}"),
                    string(name: 'ACTION',           value: "services"),
                    string(name: 'ENV',              value: "prd"),
                    string(name: 'OPERATION',        value: "deploy"),
                    string(name: 'JRE',              value: "${JRE}"),
                    string(name: 'URL_JENKINS',      value: "${BUILD_URL}"),
                    string(name: 'TAG',              value: "${BUILD_TAG}"),
                    string(name: 'ARTIFACT',         value: "${env.artifact}"),
                ]
            }
        }

    }

    post {
      always {
        echo 'Notificar Slack'
        deleteDir()
        emailext mimeType: 'text/html', attachLog: true, body: """<h1> STATUS: ${currentBuild.currentResult} </h1><pre>${env.gitlogs}</pre> <pre>${env.buildlogs} </pre> </br></br><p><a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a> ${GIT_URL} </p>""", compressLog: true, from: 'ci-service@dimed.com.br', subject: "Build Notification: ${JOB_NAME}-Build# ${BUILD_NUMBER}  ${currentBuild.currentResult}", to: "${EMAIL_NOTIFICACAO}"
      }
    }

}
