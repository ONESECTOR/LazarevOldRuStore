package com.owlylabs.platform.util

import android.content.Context
import java.io.File

class FileUtil {
    companion object {
        fun getBookFilePath(context: Context, bookId: Int): String {
            val sb = StringBuilder()
            sb.append(context.getExternalFilesDir(null)!!.absolutePath)
            sb.append(File.separator)
            sb.append("books")
            sb.append(File.separator)
            sb.append(bookId)
            sb.append(".epub")
            return sb.toString()

        }

        fun getBookFolderFilePath(context: Context, bookId: Int): String {
            val sb = StringBuilder()
            sb.append(context.getExternalFilesDir(null)!!.absolutePath)
            sb.append(File.separator)
            sb.append("books")
            sb.append(File.separator)
            return sb.toString()

        }
    }
}