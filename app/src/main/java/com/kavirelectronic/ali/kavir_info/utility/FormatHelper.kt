package com.kavirelectronic.ali.kavir_info.utility

object FormatHelper {
    private val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
    private val engNumbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    @JvmStatic
    fun toPersianNumber(text: String?): String {
        if (text == null) {
            return ""
        }
        if (text.length == 0 || text == "") {
            return ""
        }
        var out = ""
        val length = text.length
        for (i in 0 until length) {
            val c = text[i]
            if ('0' <= c && c <= '9') {
                val number = c.toString().toInt()
                out += persianNumbers[number]
            } else if (c == '٫') {
                out += '،'
            } else {
                out += c
            }
        }
        return out
    }

    fun toEngNumber(text: String): String {
        if (text.length == 0) {
            return ""
        }
        var out = ""
        val length = text.length
        for (i in 0 until length) {
            val c = text[i]
            if ('۰' <= c && c <= '۹') {
                val number = c.toString().toInt()
                out += engNumbers[number]
            } else if (c == '،') {
                out += '٫'
            } else {
                out += c
            }
        }
        return out
    }
}