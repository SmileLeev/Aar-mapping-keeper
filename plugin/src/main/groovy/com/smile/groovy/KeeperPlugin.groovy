package com.smile.groovy


import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.TaskProvider
import proguard.obfuscate.MappingReader

class KeeperPlugin implements Plugin<Project> {
    Project project
    static Logger logger

    @Override
    void apply(Project target) {
        project = target
        logger = project.logger
        LibraryExtension libraryExtension = project.extensions.findByType(LibraryExtension.class)
        def taskContainer = project.tasks
        project.afterEvaluate {
            libraryExtension.libraryVariants.each { LibraryVariant variant ->
                if (!variant.buildType.minifyEnabled) {
                    return
                }
                variant.packageLibraryProvider.get().doFirst {
                    String mergeTaskName = "merge${variant.name.capitalize()}ConsumerProguardFiles"
                    TaskProvider mergeFileTask = taskContainer.named(mergeTaskName)
                    if (mergeFileTask == null) {
                        throw new RuntimeException("Can not find task ${mergeTaskName}!")
                    }
                    // todo need to adapt other gradle version
                    def mergeProguardFile = mergeFileTask.get().outputs.files.singleFile
                    def mappingFile = variant.getMappingFile()

                    def mappingReader = new MappingReader(mappingFile)
                    def mappingProcess = new SelfMappingProcess(mergeProguardFile)
                    mappingReader.pump(mappingProcess)
                    mappingProcess.finish()
                }
            }
        }
    }

}