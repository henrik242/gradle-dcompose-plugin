package com.chrisgahlert.gradledcomposeplugin.extension

import com.chrisgahlert.gradledcomposeplugin.AbstractDcomposeSpec
import spock.lang.Unroll

/**
 * Created by chris on 21.04.16.
 */
class ContainerSpec extends AbstractDcomposeSpec {

    def 'should validate correctly on missing defintion'() {
        given:
        buildFile << """
            dcompose {
                main {
                }
            }
        """

        when:
        def result = runTasksWithFailure 'help'

        then:
        result.standardError.contains("Either dockerFile or image must be provided for dcompose container 'main'")
    }

    def 'should validate correctly on duplicate defintion'() {
        given:
        buildFile << """
            dcompose {
                main {
                    baseDir = file('docker/')
                    image = 'abc'
                }
            }
        """

        when:
        def result = runTasksWithFailure 'help'

        then:
        result.standardError.contains("Either dockerFile or image must be provided for dcompose container 'main'")
    }

    def 'should validate direct container link'() {
        given:
        buildFile << """
            dcompose {
                server {
                    image = 'def'
                }
                client {
                    image = 'abc'
                    links = [server]
                }
            }
        """

        when:
        def result = runTasksWithFailure 'help'

        then:
        result.standardError.contains("Invalid container link from client to server")
    }

    @Unroll
    def 'should #successLabel finding the host port "#expectedLabel" for container port "#find"'() {
        given:
        buildFile << """
            dcompose {
              server {
                image = '$DEFAULT_IMAGE'
                command = ['sleep', '300']
                exposedPorts = ['1001', '1002', '1003', '1004']
                portBindings = [
                  '1001',
                  '10001:1001',
                  '127.0.0.1::1002',
                  '127.0.0.2:10002:1002',
                  '9002:1002',
                  '10003:1003',
                  '10005:1005'
                ]
              }
            }

            task findBindings(dependsOn: dcompose.server.startTaskName) << {
                file('result').text = dcompose.server.findHostPort($find)
            }
        """

        when:
        def result = runTasks 'findBindings'
        println result.standardOutput
        println result.standardError

        then:
        assert result.success == success

        if (expectedPort) {
            assert file('result').text == "$expectedPort"
        } else if (success) {
            assert file('result').text.isInteger()
        }
        if (errorMessage) {
            assert result.standardError.contains(errorMessage)
        }

        where:
        find                        || success || expectedPort || errorMessage
        1001                        || true    || 10001        || null
        1002                        || false   || null         || 'container server has multiple host ports bound'
        "1002, hostIp: '127.0.0.2'" || true    || 10002        || null
        "1002, hostIp: '127.0.0.1'" || true    || null         || null
        "1002, hostIp: '0.0.0.0'"   || true    || 9002         || null
        1003                        || true    || 10003        || null
        1004                        || false   || null         || 'has not been bound to a host port'
        1005                        || false   || null         || 'Could not find container port 1005'

        expectedLabel = expectedPort ?: 'dynamic'
        successLabel = success ? 'succeed' : 'fail'
    }
}
