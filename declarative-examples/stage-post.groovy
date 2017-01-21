package postStage
/*
 * The MIT License
 *
 * Copyright (c) 2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

pipeline {
    agent {
        label "here"
    }
    stages {
        stage("foo") {
            steps {
                echo "hello"
                script {
                    String res = env.MAKE_RESULT
                    if (res != null) {
                        echo "Setting build result ${res}"
                        currentBuild.result = res
                    } else {
                        echo "All is well"
                    }
                }
            }
            post {
                aborted {
                    echo "I WAS ABORTED"
                }
                always {
                    echo "I AM ALWAYS WITH YOU"
                }
                changed {
                    echo "I HAVE CHANGED"
                }
                failure {
                    echo "I FAILED"
                }
                success {
                    echo "MOST DEFINITELY FINISHED"
                }
                unstable {
                    echo "I AM UNSTABLE"
                }
            }
        }
    }
    post {
        always {
            echo "And AAAAIIIAAAIAI"
        }
    }
}
