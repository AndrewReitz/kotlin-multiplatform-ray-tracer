// Stops handle bar lib from spitting out slf4j warnings.
@Grab(group='org.slf4j', module='slf4j-nop', version='1.7.9')

// Setup for handle bar template engine.
@GrabResolver(name='jcenter', root='http://jcenter.bintray.com/')
@Grab(group='uk.co.cacoethes', module='groovy-handlebars-engine', version='0.2')

import org.apache.commons.io.FileUtils
import uk.co.cacoethes.util.NameType
import uk.co.cacoethes.handlebars.HandlebarsTemplateEngine
import groovy.io.FileType

// Files with this name need to be replaced.
final String FILE_FILTER = "SuperGlue"

// Set handle bar template engine as the defule engine.
registerDefaultEngine new HandlebarsTemplateEngine()

def props = [:]
props.packageName = ask("Define value for 'package' [com.example]: ", "com.example", "packageName")

String applicationNameInput = ask("Define value for 'applicationName' [Example]: ", "Example", "applicationName")
        .replace(" ", "")
        .capitalize()
String gradleProjectName = transformText(applicationNameInput, from: NameType.CAMEL_CASE, to: NameType.HYPHENATED)
String applicationName = transformText(applicationNameInput, from: NameType.HYPHENATED, to: NameType.CAMEL_CASE)

props.applicationName = applicationName
props.projectName = gradleProjectName

processTemplates 'src/**/*.java', props
processTemplates 'src/**/*.xml', props
processTemplates 'build.gradle', props

projectDir.eachFileRecurse (FileType.FILES) { file ->
    if (file.name.contains(FILE_FILTER)) {
        destFile = new File(file.parent as File, file.name.replace(FILE_FILTER, applicationName) as String)
        FileUtils.moveFile(file as File, destFile as File)
    }
}

def getPackageDir(String value) { new File(projectDir as File, "src/$value/java/superglue") }
def getNewPackageDir(String newPackagePath, String value) { new File(projectDir as File, "src/$value/java/$newPackagePath") }
def getFileToDelete(String value) { new File(projectDir as File, "src/$value/java/superglue") }

String packagePath = props.packageName.replace(".", File.separator)

['internalDebug', 'internalRelease', 'main', 'production'].each {
    FileUtils.moveDirectory(getPackageDir(it), getNewPackageDir(packagePath, it))
    FileUtils.deleteDirectory(getFileToDelete(it))
}

def processBuilder = new ProcessBuilder('git', 'init', '.')
processBuilder.directory(projectDir as File)
process = processBuilder.start()
if (process.waitFor() != 0) {
    throw new Exception("Error adding project to git " +
            "${process.inputStream.readLines().join("\n")} " +
            "\n ${process.errorStream.readLines().join("\n")}")
}

processBuilder = new ProcessBuilder('git', 'add', '.')
processBuilder.directory(projectDir as File)
process = processBuilder.start()
if (process.waitFor() != 0) {
    throw new Exception("Error adding files to git " +
            "${process.inputStream.readLines().join("\n")} " +
            "\n ${process.errorStream.readLines().join("\n")}")
}

processBuilder = new ProcessBuilder('git', 'commit', '-am "initial commit"')
processBuilder.directory(projectDir as File)
process = processBuilder.start()
if (process.waitFor() != 0) {
    throw new Exception("Error comitting files to git " +
            "${process.inputStream.readLines().join("\n")} " +
            "\n ${process.errorStream.readLines().join("\n")}")
}
