package com.example.extracturl

import com.androidnetworking.error.ANError
import org.json.JSONException
import org.json.JSONObject
import com.androidnetworking.interfaces.StringRequestListener
import com.androidnetworking.AndroidNetworking
import org.apache.commons.lang3.StringEscapeUtils
import java.util.ArrayList
import java.util.Collections
import java.util.regex.Matcher
import java.util.regex.Pattern

class OkRu {

    fun fetch(vUrl: String?, onComplete: ExtractVideo.OnTaskCompleted) {
        AndroidNetworking.get(fixURL(vUrl!!))
            .addHeaders(
                "User-agent",
                "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19"
            )
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String) {
                    var json = getJson(response)
                    if (json != null) {
                        json = StringEscapeUtils.unescapeHtml4(json)
                        try {
                            json = JSONObject(json).getJSONObject("flashvars").getString("metadata")
                            if (json != null) {
                                val jsonArray = JSONObject(json).getJSONArray("videos")
                                val models: ArrayList<VModel> = ArrayList()
                                for (i in 0 until jsonArray.length()) {
                                    val url = jsonArray.getJSONObject(i).getString("url")
                                    val name = jsonArray.getJSONObject(i).getString("name")
                                    if (name == "mobile") {
                                        putModel(url, "144p", models)
                                    } else if (name == "lowest") {
                                        putModel(url, "240p", models)
                                    } else if (name == "low") {
                                        putModel(url, "360p", models)
                                    } else if (name == "sd") {
                                        putModel(url, "480p", models)
                                    } else if (name == "hd") {
                                        putModel(url, "720p", models)
                                    } else if (name == "full") {
                                        putModel(url, "1080p", models)
                                    } else if (name == "quad") {
                                        putModel(url, "2000p", models)
                                    } else if (name == "ultra") {
                                        putModel(url, "4000p", models)
                                    } else {
                                        putModel(url, "Default", models)
                                    }
                                }
                                onComplete.onTaskCompleted(sortMe(models), true)
                            } else {
                                onComplete.onError()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            onComplete.onError()
                        }
                    } else onComplete.onError()
                }

                private fun getJson(html: String): String? {
                    val regex = "data-options=\"(.*?)\""
                    val pattern: Pattern = Pattern.compile(regex, Pattern.MULTILINE)
                    val matcher: Matcher = pattern.matcher(html)
                    return if (matcher.find()) {
                        matcher.group(1)
                    } else null
                }

                override fun onError(anError: ANError) {
                    onComplete.onError()
                }
            })
    }

    private fun fixURL(vUrl: String): String {
        var url = vUrl
        if (!url.startsWith("https")) {
            url = url.replace("http", "https")
        }
        if (url.contains("m.")) {
            url = url.replace("m.", "")
        }
        if (url.contains("/video/")) {
            url = url.replace("/video/", "/videoembed/")
        }
        return url
    }


    fun putModel(url: String?, quality: String?, model: ArrayList<VModel>) {
        if (url != null && quality != null) {
            val xModel = VModel()
            xModel.url = url
            xModel.quality = quality
            model.add(xModel)
        }
    }

    fun sortMe(x: ArrayList<VModel>?): ArrayList<VModel>? {
        if (x != null) {
            if (x.size > 0) {
                val result: ArrayList<VModel> = ArrayList<VModel>()
                for (t in x) {
                    if (startWithNumber(t.quality!!) || t.quality!!.isEmpty()) {
                        result.add(t)
                    }
                }
                Collections.sort(result, Collections.reverseOrder<Any>())
                return result
            }
        }
        return null
    }

    private fun startWithNumber(string: String): Boolean {
        val regex = "^[0-9][A-Za-z0-9-\\s,]*$"
        val pattern = Pattern.compile(regex, Pattern.MULTILINE)
        val matcher = pattern.matcher(string)
        return matcher.find()
    }
}