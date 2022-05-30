package csense.swift.not.highlighter.settings

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.*
import com.intellij.openapi.project.ProjectManager
import csense.swift.not.highlighter.settings.form.*
import javax.swing.JComponent

class NotHighlighterSettingsConfigurable : Configurable {

    var ui: NotHighlighterSettingsUI? = null

    override fun createComponent(): JComponent? {
        ui = ui ?: NotHighlighterSettingsUI()
        return ui?.component()
    }

    override fun isModified(): Boolean {
        return ui?.isModified() ?: return false
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        val ui = ui ?: return
        updateSettings(ui)
        reloadAllActiveIntellijWindows()
    }

    private fun updateSettings(ui: NotHighlighterSettingsUI) {
        val settings = NotHighlighterSettings.instance
        settings.isEnabled = ui.isEnabled()

        settings.foregroundColor = ui.foregroundColor()
        settings.backgroundColor = ui.backgroundColor()

        settings.highlightFunctionNames = ui.highlightFunctionNames()
        settings.highlightVariableNames = ui.highlightVariableNames()

        settings.italic = ui.italic()
        settings.bold = ui.bold()
    }

    private fun reloadAllActiveIntellijWindows() {
        ProjectManager.getInstance().openProjects.forEach {
            DaemonCodeAnalyzer.getInstance(it).restart()
        }
    }

    override fun reset() {
        ui?.loadSettings()
    }

    override fun disposeUIResources() {
        ui = null
    }

    override fun getDisplayName() = "Csense Swift Not Highlighter"
}