package com.example.extracturl

import java.util.regex.Matcher
import java.util.regex.Pattern

class VModel:Comparable<VModel> {

    var quality : String? = null
    var url : String? = null

        override fun compareTo(other: VModel): Int {
        if (startWithNumber(other.quality!!)){
            try {
                return Integer.valueOf(quality!!.replace("\\D+", "")) -
                        Integer.valueOf(other.quality!!.replace("\\D+", ""))
            }catch (e:Exception){
                return this.quality!!.length - other.quality!!.length
            }
        }
        return this.quality!!.length - other.quality!!.length
    }


    private fun startWithNumber(string: String): Boolean {
        val regex = "^[0-9][A-Za-z0-9-\\s,]*$"
        val pattern: Pattern = Pattern.compile(regex, Pattern.MULTILINE)
        val matcher: Matcher = pattern.matcher(string)
        return matcher.find()
    }
}