package com.owlylabs.platform.util

class WebViewUtil {
    companion object{
        fun overrideTextToRemovePaddings(text: String) : String{
            return "<body style=\"margin: 0; padding: 0\">".plus(text).plus("</body>")
        }
    }
}