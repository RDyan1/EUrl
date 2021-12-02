package com.example.extracturl

import android.content.Context
import com.androidnetworking.AndroidNetworking
import java.util.regex.Matcher
import java.util.regex.Pattern

class ExtractVideo (context: Context){

    private val okru = "https?:\\/\\/(www.|m.)?(ok)\\.[^\\/,^\\.]{2,}\\/(video|videoembed)\\/.+"
    private val solidfiles = "https?:\\/\\/(www\\.)?(solidfiles)\\.[^\\/,^\\.]{2,}\\/(v)\\/.+"
    private var onComplete: OnTaskCompleted? = null
    init {
        AndroidNetworking.initialize(context)
    }

    fun find(url:String){
        when {
            check(solidfiles,url) -> {
                SolidFiles().fetch(url, onComplete!!)
            }
            check(okru,url) -> {
                OkRu().fetch(url, onComplete!!)
            }
            else -> {
                onComplete!!.onError()
            }
        }
    }
    interface OnTaskCompleted {
        fun onTaskCompleted(vidURL: ArrayList<VModel>?, multipleQuality: Boolean)
        fun onError()
    }

    fun onFinish(onComplete: OnTaskCompleted?) {
        this.onComplete = onComplete
    }

    private fun check(regex: String, string: String): Boolean {
        val pattern: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(string)
        return matcher.find()
    }

}