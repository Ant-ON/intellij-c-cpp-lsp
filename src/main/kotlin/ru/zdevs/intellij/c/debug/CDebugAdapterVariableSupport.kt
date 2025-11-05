package ru.zdevs.intellij.c.debug

import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.redhat.devtools.lsp4ij.dap.descriptors.DebugAdapterVariableSupport


class CDebugAdapterVariableSupport: DebugAdapterVariableSupport() {

    /**
     * Modified version of DebugAdapterVariableSupport::getTextRange. Added handling of accessing fields using "->".
     */
    override fun getTextRange(
        project: Project,
        file: VirtualFile,
        document: Document,
        offset: Int,
        sideEffectsAllowed: Boolean
    ): TextRange? {
        // Get the entire document text
        val text = document.text

        // Safety check: ensure offset is within bounds
        if (offset < 0 || offset >= text.length) {
            return null
        }

        // Find the start of the identifier at the given offset
        var start = offset
        while (start > 0) {
            val ch = text[start - 1]
            if (Character.isJavaIdentifierPart(ch) || ch == '.') {
                start--
            } else if (ch == '>' && start > 1 && text[start - 2] == '-') {
                start -= 2
            } else {
                break
            }
        }

        // Find the end of the identifier at the given offset
        var end = offset
        while (end < text.length && Character.isJavaIdentifierPart(text[end])) {
            end++
        }

        // If there is no identifier at the offset, do not return an expression
        if (start == end) {
            return null
        }
        return TextRange(start, end)
    }
}