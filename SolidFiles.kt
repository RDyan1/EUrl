package com.example.extracturl

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import java.util.regex.Pattern

class SolidFiles {

    fun fetch(url: String?, onComplete: ExtractVideo.OnTaskCompleted) {
        AndroidNetworking.get(url)
            .addHeaders(
                "User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36")
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    val vModel: ArrayList<VModel>? = parse(response)
                    if (vModel != null) {
                        onComplete.onTaskCompleted(vModel, false)
                    } else onComplete.onError()
                }

                override fun onError(anError: ANError) {
                    onComplete.onError()
                }
            })
    }

    private fun parse(response: String): ArrayList<VModel>? {
        val url = getUrl(response)
        if (url != null) {
            val vModel = VModel()
            vModel.url = url
            vModel.quality = "Normal"
            val vModels: ArrayList<VModel> = ArrayList()
            vModels.add(vModel)
            return vModels
        }
        return null
    }

    private fun getUrl(html: String): String? {
        val regex = "downloadUrl\":\"(.*?)\""
        val pattern = Pattern.compile(regex, Pattern.MULTILINE)
        val matcher = pattern.matcher(html)
        return if (matcher.find()) {
            matcher.group(1)
        } else null
    }
}