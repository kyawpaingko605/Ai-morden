package com.aiapp
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.gemini.Gemini
import com.google.android.gms.gemini.GeminiClient
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
class MainActivity : AppCompatActivity() {
    private lateinit var geminiClient: GeminiClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        geminiClient = Gemini.getClient(this)
        button.setOnClickListener {
            MainScope().launch {
                val response = geminiClient.converse "Hello, how are you?"
                println(response.text)
            }
        }
    }
}