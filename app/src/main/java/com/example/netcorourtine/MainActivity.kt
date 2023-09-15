package com.example.netcorourtine

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var bitmapState by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

            // Fetch image using coroutines
            lifecycleScope.launch {
                val fetchedBitmap = fetchImageFromURL("https://users.metropolia.fi/~jarkkov/folderimage.jpg")
                bitmapState = fetchedBitmap
            }

            // Display the bitmap in Composable
            Box(modifier = Modifier.fillMaxSize()) {
                bitmapState?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Fetched Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    private suspend fun fetchImageFromURL(urlStr: String): android.graphics.Bitmap? {
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpURLConnection
        return try {
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } finally {
            connection.disconnect()
        }
    }
}
