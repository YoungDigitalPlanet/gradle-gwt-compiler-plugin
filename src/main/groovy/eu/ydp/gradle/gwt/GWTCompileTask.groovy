package eu.ydp.gradle.gwt

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction

class GWTCompileTask extends DefaultTask {
    def File buildDir;
    def String module;
    def gwtArgs = [:].withDefault { '' }

    GWTCompileTask() {
        gwtArgs['-logLevel'] = 'INFO'
        gwtArgs['-localWorkers'] = '2'
        gwtArgs['-XdisableCastChecking']
    }

    @TaskAction
    public void exec() {
        def javaConvention = project.getConvention().getPlugin(JavaPluginConvention);
        def mainSourceSet = javaConvention.getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME);
        buildDir.mkdirs()

        def basicGwtArgs = [
                this.module,
                '-war', this.buildDir.absolutePath
        ]

        def customGwtArgs = gwtArgs
                .collectMany { k, v -> [k, v] }.findAll { it != '' }

        project.javaexec {
            main = 'com.google.gwt.dev.Compiler'
            classpath {
                [
                        mainSourceSet.java.srcDirs,           // Java source
                        mainSourceSet.output.resourcesDir,    // Generated resources
                        mainSourceSet.output.classesDir,      // Generated classes
                        mainSourceSet.compileClasspath,       // Deps
                ]
            }

            args = basicGwtArgs + customGwtArgs

            systemProperty 'java.awt.headless', 'true'

            maxHeapSize = '1024M'
            jvmArgs('-XX:MaxPermSize=512M')
        }
    }
}
