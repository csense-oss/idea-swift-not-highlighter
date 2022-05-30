package csense.swift.not.highlighter

import com.intellij.lang.annotation.*
import com.intellij.openapi.editor.markup.*
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.*
import com.intellij.psi.util.*
import com.jetbrains.swift.lexer.*
import com.jetbrains.swift.psi.*
import csense.kotlin.extensions.*
import csense.swift.not.highlighter.settings.*
import java.awt.*

class NotHighlighter : Annotator {
    private val settings by lazy {
        NotHighlighterSettings.instance
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (settings.isNotEnabled) {
            return
        }
        //by documentation: we should only process leaf elements. (this is for perf and multiple colors etc..)
        if (element !is LeafPsiElement) {
            return
        }
        element.highlightNotInLeafyCode(holder)
    }


    private fun LeafPsiElement.highlightNotInLeafyCode(holder: AnnotationHolder) {
        if (parent.isNotOperator()) {
            highlightNotOperator(holder)
            return
        }
        if (elementType.isIdentifier() && mayHighlightNotText()) {
            highlightNotText(holder)
            return
        }
    }

    private fun IElementType.isIdentifier(): Boolean {
        return this in SwiftTokenTypes.IDENTIFIERS
    }

    private fun LeafPsiElement.highlightNotText(holder: AnnotationHolder) {
        val notIndexInIdentifier = text.indexOfNotOrNull() ?: return
        val start = textRange.startOffset + notIndexInIdentifier
        holder.highlightNotElement(TextRange(start, start + NotText.length))
    }

    private fun LeafPsiElement.highlightNotOperator(holder: AnnotationHolder) {
        holder.highlightNotElement(this)
    }

    private fun LeafPsiElement.mayHighlightNotText(): Boolean {
        return when (parent) {
            is SwiftComment -> false
            is SwiftImportDeclaration -> false
            is SwiftImportPath -> false
            is SwiftString -> false
            is SwiftClassDeclaration -> true
            is SwiftEnumCase -> true
            is SwiftEnumDeclaration -> true
            is SwiftFunctionDeclaration -> settings.highlightFunctionNames
            else -> {
                parent.isNotOperator() || settings.highlightVariableNames
            }
        }
    }

    private fun AnnotationHolder.highlightNotElement(textRange: TextRange) {
        highlightWithRange { range(textRange) }
    }

    private fun AnnotationHolder.highlightNotElement(element: PsiElement) {
        highlightWithRange { range(element) }
    }

    private fun AnnotationHolder.highlightWithRange(range: AnnotationBuilder.() -> AnnotationBuilder) {
        this.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range()
            .notHighlighterText()
            .create()
    }

    private fun AnnotationBuilder.notHighlighterText(): AnnotationBuilder = apply {
        enforcedTextAttributes(getNotHighlightingTextAttributes())
    }

    private fun getNotHighlightingTextAttributes(): TextAttributes {
        return TextAttributes(
            /* foregroundColor = */  settings.foregroundColor,
            /* backgroundColor = */ settings.backgroundColor,
            /* effectColor = */ null,
            /* effectType = */ null,
            /* fontType = */ getFontBySettings()
        )
    }

    private fun getFontBySettings(): Int {
        return settings.bold.map(
            ifTrue = Font.BOLD,
            ifFalse = Font.PLAIN
        ) + settings.italic.map(
            ifTrue = Font.ITALIC,
            ifFalse = Font.PLAIN
        )
    }


}