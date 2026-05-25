package com.mingalarai

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private val messageList = mutableListOf<Message>() // စကားပြောစာရင်း သိမ်းရန်

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // XML ပါ Element များကို ကုဒ်နှင့် ချိတ်ဆက်ခြင်း
        recyclerView = findViewById(R.id.chatRecyclerView)
        val etMessage = findViewById<EditText>(R.id.inputPanel).findViewById<EditText>(com.google.android.material.R.id.textinput_placeholder) // ဆေးတောင့်ပုံစံ စာရိုက်ကွက်
        val btnSend = findViewById<MaterialButton>(R.id.inputPanel).findViewById<MaterialButton>(R.id.inputPanel.nextFocusDownId) // စာပို့ခလုတ်

        // RecyclerView ပုံစံချခြင်း
        recyclerView.layoutManager = LinearLayoutManager(this)
        // မှတ်ချက် - သင့် ChatAdapter ထဲတွင် messageList ကို လက်ခံရန် လိုအပ်ပါသည်
        adapter = ChatAdapter() 
        recyclerView.adapter = adapter

        // Gemini AI Setup (ApiKey နေရာတွင် မိမိ Key ထည့်ပါ)
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "YOUR_GEMINI_API_KEY" 
        )

        // စာပို့ခလုတ် နှိပ်လိုက်ချိန် လုပ်ဆောင်ချက်
        findViewById<MaterialButton>(R.id.inputPanel).setOnClickListener {
            // စာရိုက်ကွက်ထဲမှ စာသားကို ယူခြင်း
            val userText = "မင်္ဂလာပါ" // ဥပမာ စမ်းသပ်စာသား (စာရိုက်ကွက်နှင့် တိုက်ရိုက်ချိတ်ဆက်နိုင်သည်)
            
            if (userText.isNotEmpty()) {
                // ၁။ မိမိပို့လိုက်သောစာကို Chat ထဲထည့်ခြင်း
                // messageList.add(Message(userText)) // သင့် Message Class ပုံစံအတိုင်း ပြင်သုံးပါ
                adapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messageList.size - 1)

                // ၂။ AI ထံမှ အဖြေတောင်းခြင်း
                MainScope().launch {
                    try {
                        val prompt = "You are a helpful assistant. Reply in Myanmar language: $userText"
                        val response = generativeModel.generateContent(prompt)
                        
                        // AI အဖြေပြန်ရလာလျှင် Chat ထဲ ထည့်ပြခြင်း
                        // messageList.add(Message(response.text ?: ""))
                        adapter.notifyDataSetChanged()
                        recyclerView.scrollToPosition(messageList.size - 1)
                    } catch (e: Exception) {
                        // လိုင်းမကောင်းပါက Error ပြရန်
                    }
                }
            }
        }
    }
}
