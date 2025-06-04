package ru.zdevs.intellij.c

import com.intellij.util.EnvironmentUtil
import java.io.File
import java.nio.file.Paths

class Utils {
    companion object {
        fun findExecutableInPATH(executable: String) =
            EnvironmentUtil.getValue("PATH")?.split(File.pathSeparator)?.firstNotNullOfOrNull { path ->
                Paths.get(path, executable).toFile().takeIf { it.canExecute() }
            }?.path
    }
}