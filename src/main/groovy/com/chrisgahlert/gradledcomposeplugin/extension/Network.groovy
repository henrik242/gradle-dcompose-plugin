/*
 * Copyright 2016 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chrisgahlert.gradledcomposeplugin.extension

import groovy.transform.TypeChecked
import org.gradle.util.ConfigureUtil

@TypeChecked
abstract class Network extends AbstractEntity {
    public static class Ipam implements Serializable {
        String driver
        List<IpamConfig> configs = []
        Map<String, String> options

        IpamConfig config(Closure config) {
            def ipamConfig = new IpamConfig()
            configs << ipamConfig
            ConfigureUtil.configure(config, ipamConfig)
        }

        @Override
        boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Ipam)) {
                return false
            }
            def other = (Ipam) obj

            return Objects.equals(this.driver, other.driver) &&
                    Objects.equals(this.configs, other.configs) &&
                    Objects.equals(this.options, other.options)
        }

        @Override
        int hashCode() {
            return Objects.hash(driver, configs, options)
        }
    }

    public static class IpamConfig implements Serializable {
        String subnet
        String ipRange
        String gateway

        @Override
        boolean equals(Object obj) {
            if (obj == null || !(obj instanceof IpamConfig)) {
                return false
            }
            def other = (IpamConfig) obj

            return Objects.equals(this.subnet, other.subnet) &&
                    Objects.equals(this.ipRange, other.ipRange) &&
                    Objects.equals(this.gateway, other.gateway)
        }

        @Override
        int hashCode() {
            return Objects.hash(subnet, ipRange, gateway)
        }
    }

    public static final String DEFAULT_NAME = "default"

    final transient Closure<String> dockerPrefix

    Network(String name, String projectPath) {
        super(name, projectPath)
    }

    String getCreateTaskName() {
        "create${nameCamelCase}Network"
    }

    String getRemoveTaskName() {
        "remove${nameCamelCase}Network"
    }

    abstract String getNetworkName()

    abstract String getDriver()

    abstract Map<String, String> getDriverOpts()

    abstract Ipam getIpam()

    abstract Boolean getEnableIpv6()

    @Override
    String toString() {
        networkName
    }

}
