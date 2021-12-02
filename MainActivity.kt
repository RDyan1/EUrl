package com.example.extracturl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var extractVideo: ExtractVideo
    private val TAG = "mainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        extractVideo = ExtractVideo(this)

        val btnSolidFiles = findViewById<Button>(R.id.btnSolidFiles)
        val btnOk = findViewById<Button>(R.id.btnOk)
        btnOk.setOnClickListener {
            extractVideo.find("https://ok.ru/video/3111370099275")
        }
        btnSolidFiles.setOnClickListener {
            extractVideo.find("https://www.solidfiles.com/v/nk2Dp6gkkgrZN")
        }

        extractVideo.onFinish(object : ExtractVideo.OnTaskCompleted {
            override fun onTaskCompleted(vidURL: ArrayList<VModel>?, multipleQuality: Boolean) {
                if (vidURL != null) {
                    if (multipleQuality){
                        for (url in vidURL){
                            Toast.makeText(this@MainActivity, "url download: ${url.url}",Toast.LENGTH_SHORT).show()
                            Log.d(TAG,"quality: ${url.quality} url download: ${url.url}")
                            Log.d(TAG,"quality: ${url.quality} url download: ${url.url}")
                        }
                    }else{
                        Log.d(TAG,"url download: ${vidURL[0].url}")
                        Toast.makeText(this@MainActivity, "url download: ${vidURL[0].url}",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG,"url download is null")
                    Toast.makeText(this@MainActivity, "null",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError() {
                Log.d(TAG,"Error")
                Toast.makeText(this@MainActivity, "Error",Toast.LENGTH_SHORT).show()
            }

        })
    }
}