package csense.swift.not.highlighter.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import csense.kotlin.extensions.type
import java.awt.Color

@State(name = "CsenseNotHighlighter", storages = [(Storage("csense_not_highlighter.xml"))])
class NotHighlighterSettings : PersistentStateComponent<NotHighlighterSettings> {
    var isEnabled = true

    var foregroundColorInt: Int? = 0xBD57FF
    var backgroundColorInt: Int? = null

    var highlightVariableNames = true

    var highlightFunctionNames = true

    var italic = true

    var bold = true


    override fun loadState(state: NotHighlighterSettings) {
        this.isEnabled = state.isEnabled
        this.bold = state.bold
        this.italic = state.italic
        this.highlightFunctionNames = state.highlightFunctionNames
        this.highlightVariableNames = state.highlightVariableNames
        this.backgroundColor = state.backgroundColor
        this.foregroundColor = state.foregroundColor
    }

    override fun getState(): NotHighlighterSettings = this

    companion object {
        val instance: NotHighlighterSettings
            get() = ApplicationManager.getApplication().getService(type())
    }


}


val NotHighlighterSettings.isNotEnabled: Boolean
    get() = !isEnabled

var NotHighlighterSettings.foregroundColor: Color?
    get() = foregroundColorInt?.let { Color(it) }
    set(newValue) {
        foregroundColorInt = newValue?.rgb
    }

var NotHighlighterSettings.backgroundColor: Color?
    get() = backgroundColorInt?.let { Color(it) }
    set(newValue) {
        backgroundColorInt = newValue?.rgb
    }

