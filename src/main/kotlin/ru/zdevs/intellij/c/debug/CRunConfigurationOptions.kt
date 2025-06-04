package ru.zdevs.intellij.c.debug

import com.intellij.openapi.components.StoredProperty
import com.intellij.util.execution.ParametersListUtil
import com.redhat.devtools.lsp4ij.dap.configurations.DAPRunConfigurationOptions
import com.redhat.devtools.lsp4ij.launching.ServerMappingSettings

class CRunConfigurationOptions : DAPRunConfigurationOptions() {
    private val stopAtMainProp: StoredProperty<Boolean> = property(true).provideDelegate(this, "stopAtMain")
    private val launchArgumentsProp: StoredProperty<String?> = string(null).provideDelegate(this, "launchArguments")

    var stopAtMain: Boolean
        get() = stopAtMainProp.getValue(this)
        set(value) = stopAtMainProp.setValue(this, value)

    var launchArguments: String?
        get() = launchArgumentsProp.getValue(this)
        set(value) = launchArgumentsProp.setValue(this, value)

    fun getLaunchArgumentJSONList() : List<String> {
        val launchArguments = launchArguments
        if (launchArguments != null) {
            val args = ParametersListUtil.parse(launchArguments)
            val list = ArrayList<String>(args.size)
            for (arg in args) {
                list.add("\"" + arg.replace("\"", "\\\"") + "\"")
            }
            return list
        } else {
            return listOf()
        }
    }

    override fun getServerMappings(): List<ServerMappingSettings> {
        return listOf(MAPPING_FILE_TYPE, MAPPING_LANG)
    }

    companion object {
        private val MAPPING_FILE_TYPE = ServerMappingSettings.createFileNamePatternsMappingSettings(listOf("*.c", "*.cpp", "*.cc", "*.h", "*.hpp", "*.s", "*.S"), "clangd")
        private val MAPPING_LANG = ServerMappingSettings.createLanguageMappingSettings("clangd", "clangd")
    }
}