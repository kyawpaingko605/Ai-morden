import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var inputEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var outputTextView: TextView
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputEditText = findViewById(R.id.input_edit_text)
        sendButton = findViewById(R.id.send_button)
        outputTextView = findViewById(R.id.output_text_view)

        sendButton.setOnClickListener {
            val input = inputEditText.text.toString()
            if (input.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.IO) {
                    val request = Request.Builder()
                        .url("https://api.example.com/endpoint")
                        .post(null)
                        .build()
                    val response = client.newCall(request).execute()
                    val responseBody = response.body?.string()
                    val jsonObject = JSONObject(responseBody)
                    val output = jsonObject.getString("output")
                    Log.d("Response", output)
                    runOnUiThread {
                        outputTextView.text = output
                    }
                }
            }
        }
    }
}