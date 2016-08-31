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
import org.gradle.api.GradleException
import org.gradle.api.Project

@TypeChecked
class ServiceReference extends Service {
    private final Project targetProject
    private Service resolvedContainer

    ServiceReference(String name, Project targetProject) {
        super(name, targetProject.path)
        this.targetProject = targetProject
    }

    Service getResolved() {
        if (resolvedContainer == null) {
            def extension = targetProject.extensions.findByType(DcomposeExtension)
            if (extension == null) {
                throw new GradleException("Could not find dcompose extension on $targetProject - has the dcompose plugin been applied?")
            }

            resolvedContainer = extension.getByName(name)
        }

        resolvedContainer
    }

    @Override
    String getContainerName() {
        resolved.containerName
    }

    @Override
    boolean isWaitForCommand() {
        resolved.waitForCommand
    }

    @Override
    boolean isIgnoreExitCode() {
        resolved.ignoreExitCode
    }

    @Override
    int getWaitTimeout() {
        resolved.waitTimeout
    }

    @Override
    boolean isPreserveVolumes() {
        resolved.preserveVolumes
    }

    @Override
    List<String> getCommand() {
        resolved.command
    }

    @Override
    List<String> getEntrypoints() {
        resolved.entrypoints
    }

    @Override
    List<String> getEnv() {
        resolved.env
    }

    @Override
    String getWorkingDir() {
        resolved.workingDir
    }

    @Override
    String getUser() {
        resolved.user
    }

    @Override
    Boolean getReadonlyRootfs() {
        resolved.readonlyRootfs
    }

    @Override
    List<String> getVolumes() {
        resolved.volumes
    }

    @Override
    List<String> getBinds() {
        resolved.binds
    }

    @Override
    List getVolumesFrom() {
        resolved.volumesFrom
    }

    @Override
    List<String> getExposedPorts() {
        resolved.exposedPorts
    }

    @Override
    List<String> getPortBindings() {
        resolved.portBindings
    }

    @Override
    Boolean getPublishAllPorts() {
        resolved.publishAllPorts
    }

    @Override
    List getLinks() {
        resolved.links
    }

    @Override
    String getHostName() {
        resolved.hostName
    }

    @Override
    List<String> getDns() {
        resolved.dns
    }

    @Override
    List<String> getDnsSearch() {
        resolved.dnsSearch
    }

    @Override
    List<String> getExtraHosts() {
        resolved.extraHosts
    }

    @Override
    String getNetworkMode() {
        resolved.networkMode
    }

    @Override
    Boolean getAttachStdin() {
        resolved.attachStdin
    }

    @Override
    Boolean getAttachStdout() {
        resolved.attachStdout
    }

    @Override
    Boolean getAttachStderr() {
        resolved.attachStderr
    }

    @Override
    Boolean getPrivileged() {
        resolved.privileged
    }

    @Override
    File getBaseDir() {
        resolved.baseDir
    }

    @Override
    String getDockerFilename() {
        resolved.dockerFilename
    }

    @Override
    Long getMemory() {
        resolved.memory
    }

    @Override
    Long getMemswap() {
        resolved.memswap
    }

    @Override
    String getCpushares() {
        resolved.cpushares
    }

    @Override
    String getCpusetcpus() {
        resolved.cpusetcpus
    }

    @Override
    Map<String, String> getBuildArgs() {
        resolved.buildArgs
    }

    @Override
    Boolean getForceRemoveImage() {
        resolved.forceRemoveImage
    }

    @Override
    Boolean getNoPruneParentImages() {
        resolved.noPruneParentImages
    }

    @Override
    Boolean getBuildNoCache() {
        resolved.buildNoCache
    }

    @Override
    Boolean getBuildRemove() {
        resolved.buildRemove
    }

    @Override
    Boolean getBuildPull() {
        resolved.buildPull
    }

    @Override
    void setHostPortBindings(Map hostPortBindings) {
        resolved.setHostPortBindings(hostPortBindings)
    }

    @Override
    def findHostPort(Map<String, String> properties, int containerPort) {
        resolved.findHostPort(properties, containerPort)
    }

    @Override
    void setDockerHost(URI uri) {
        resolved.dockerHost = uri
    }

    @Override
    String getDockerHost() {
        resolved.dockerHost
    }

    @Override
    Set<Service> getLinkDependencies() {
        resolved.linkDependencies
    }

    @Override
    Set<Service> getVolumesFromDependencies() {
        resolved.volumesFromDependencies
    }

    @Override
    String getTag() {
        resolved.tag
    }

    @Override
    String getImage() {
        resolved.image
    }

    @Override
    boolean hasImage() {
        resolved.hasImage()
    }

    @Override
    void validate() {
        resolved.validate()
    }

    @Override
    List<Network> getNetworks() {
        resolved.networks
    }

    @Override
    List<String> getAliases() {
        resolved.aliases
    }

    @Override
    Integer getStopTimeout() {
        resolved.stopTimeout
    }
}