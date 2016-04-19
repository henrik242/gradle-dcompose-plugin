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

import com.chrisgahlert.gradledcomposeplugin.tasks.*
import com.chrisgahlert.gradledcomposeplugin.utils.DcomposeUtils
import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskContainer
import org.gradle.util.ConfigureUtil

@CompileStatic
class DcomposeExtension {
    final private TaskContainer tasks

    final private Set<Container> containers = new HashSet<>()

    String namePrefix

    DcomposeExtension(TaskContainer tasks, File rootProjectDir) {
        this.tasks = tasks

        String hash = DcomposeUtils.sha1Hash(rootProjectDir.canonicalPath)
        def pathHash = hash.substring(0, 7)
        namePrefix = "dcompose_${pathHash}_"
    }

    Container getByNameOrCreate(String name, Closure config) {
        def container = findByName(name)

        if (container == null) {
            container = new Container(name, { namePrefix })
            config.setDelegate container
            ConfigureUtil.configure(config, container)
            container.validate()

            createContainerTasks(container)
            containers << container
        } else {
            config.setDelegate container
            ConfigureUtil.configure(config, container)
        }

        return container
    }

    private void createContainerTasks(Container container) {
        def initImage;
        if (container.hasImage()) {
            initImage = tasks.create(container.pullTaskName, DcomposeImagePullTask)
        } else {
            initImage = tasks.create(container.buildTaskName, DcomposeImageBuildTask)
        }
        initImage.container = container

        def create = tasks.create(container.createTaskName, DcomposeContainerCreateTask)
        create.container = container
        create.dependsOn initImage

        def start = tasks.create(container.startTaskName, DcomposeContainerStartTask)
        start.container = container
        start.dependsOn create

        def stop = tasks.create(container.stopTaskName, DcomposeContainerStopTask)
        stop.container = container

        def removeContainer = tasks.create(container.removeContainerTaskName, DcomposeContainerRemoveTask)
        removeContainer.container = container
        removeContainer.dependsOn stop

        def removeImage = tasks.create(container.removeImageTaskName, DcomposeImageRemoveTask)
        removeImage.container = container
        removeImage.dependsOn removeContainer
    }

    Container findByName(String name) {
        return containers.find { it.name == name }
    }

    Container getByName(String name) {
        def container = findByName(name)

        if (container == null) {
            throw new GradleException("dcompose container with name '$name' not found")
        }

        container
    }

    Set<Container> getContainers() {
        containers
    }

    def methodMissing(String name, def args) {
        def argsAr = args as Object[]

        if (argsAr.size() != 1 || !(argsAr[0] instanceof Closure)) {
            throw new MissingMethodException(name, DcomposeExtension, argsAr)
        }

        getByNameOrCreate(name, argsAr[0] as Closure)
    }

    def propertyMissing(String name) {
        getByName(name)
    }

}
