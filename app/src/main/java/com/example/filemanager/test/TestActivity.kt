package com.example.filemanager.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yandex.disk.rest.Credentials
import com.yandex.disk.rest.RestClient


class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }
    }


    @Composable
    fun MainContent() {
        val coroutineScope = rememberCoroutineScope()
        Column(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {
                /*val client = OkHttpClient()
                val url = "https://oauth.yandex.ru/authorize?response_type=token&client_id=a39322e53c53479aa4e243942f3a5a75"
                val request = Request.Builder().url(url).build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(request: Request?, e: IOException?) {
                        e!!.printStackTrace()
                    }

                    override fun onResponse(response: Response?) {
                        if (response!!.isSuccessful) {
                            val myResponse: String = response.body().string()
                            println(myResponse)
                        }
                    }
                })*/
                /*val c = Credentials("dmitriylosevxxx", "AQAAAABW_ZzeAADLW9NeCoVoc0x-m4_XOYhLDs4")
                val client: RestClient = RestClientUtil.getInstance(c)
                val k = yandexDisk.diskInfo.totalSpace
                println(k)*/
            }) {
                /*AndroidView(factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = WebViewClient()
                        loadUrl("https://oauth.yandex.ru/authorize?response_type=token&client_id=a39322e53c53479aa4e243942f3a5a75") //Url to be loaded
                    }
                }, update = {
                    it.loadUrl("https://oauth.yandex.ru/authorize?response_type=token&client_id=a39322e53c53479aa4e243942f3a5a75") //Url to be loaded
                })
                Text(text = "Test")*/
            }
        }
    }


    @Preview
    @Composable
    fun ComposablePreview() {
        MainContent()
    }
}