package csense.swift.not.highlighter

import com.intellij.psi.*
import com.jetbrains.swift.psi.*

fun PsiElement.isNotOperator(): Boolean {
    val ref = this as? SwiftOperatorSignReference ?: return false
    //justification for length testing: accessing "text" (in PSI) is potentially expensive.
    val isEitherLength1_or_2 = ref.textLength in 1..2
    if (!isEitherLength1_or_2) {
        return false
    }
    val text = ref.text
    return text in arrayOf("!", "!=")
}


fun String.indexOfNotOrNull(): Int? {
    forEachKebabCase { string, index ->
        if (string.equals(NotText, ignoreCase = true)) {
            return@indexOfNotOrNull index
        }
    }
    return null
}

const val NotText = "not"