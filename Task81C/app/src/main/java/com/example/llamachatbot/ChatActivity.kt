package com.example.llamachatbot

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.llamachatbot.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()
    private var username: String = ""

    // FIXED: Use correct emulator URL and port
    private val BACKEND_URL = "http://10.0.2.2:5001/chat" // For Android Emulator
    // Use "http://192.168.x.x:5001/chat" for physical device (replace with your computer's IP)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("USERNAME") ?: "User"

        setupUI()
        setupRecyclerView()
        setupClickListeners()
        sendWelcomeMessage()
    }

    private fun setupUI() {
        // Set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_blue)

        // Set welcome message
        binding.welcomeText.text = "Welcome $username!"
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages)
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }
    }

    private fun setupClickListeners() {
        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        binding.messageEditText.setOnEditorActionListener { _, _, _ ->
            sendMessage()
            true
        }
    }

    private fun sendWelcomeMessage() {
        addMessage("Welcome $username! I'm your Llama 2 ChatBot. How can I help you today?", false)
    }

    private fun sendMessage() {
        val messageText = binding.messageEditText.text.toString().trim()

        if (messageText.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            return
        }

        // Add user message to chat
        addMessage(messageText, true)
        binding.messageEditText.text.clear()

        // Show typing indicator
        showTypingIndicator(true)

        // Send message to backend using Volley
        sendMessageToBackend(messageText)
    }

    private fun addMessage(text: String, isUser: Boolean) {
        messages.add(ChatMessage(text, isUser))
        chatAdapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.scrollToPosition(messages.size - 1)
    }

    private fun showTypingIndicator(show: Boolean) {
        binding.typingIndicator.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun sendMessageToBackend(message: String) {
        Log.d("ChatActivity", "Sending message to backend: '$message'")
        Log.d("ChatActivity", "Backend URL: $BACKEND_URL")

        val request = object : StringRequest(
            Request.Method.POST, BACKEND_URL,
            { response ->
                // Hide typing indicator and show response
                showTypingIndicator(false)
                Log.d("ChatActivity", "Raw backend response: '$response'")
                val botMessage = response.trim()
                if (botMessage.isNotEmpty()) {
                    addMessage(botMessage, false)
                } else {
                    addMessage("Sorry, I received an empty response from the server.", false)
                }
            },
            { error ->
                // Hide typing indicator and show error
                showTypingIndicator(false)
                val errorMessage = "Sorry, I couldn't connect to the server. Please make sure the backend is running."
                addMessage(errorMessage, false)
                Log.e("ChatActivity", "Volley error: ${error.message}")
                Log.e("ChatActivity", "Error details: ${error.networkResponse?.let { String(it.data) }}")
                Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show()
            }
        ) {
            // FIXED: Send form data with correct parameter name
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["userMessage"] = message  // Flask expects 'userMessage', not 'message'
                Log.d("ChatActivity", "Request params: $params")
                return params
            }

            // FIXED: Use correct Content-Type for form data
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                Log.d("ChatActivity", "Request headers: $headers")
                return headers
            }
        }

        // Set timeout and retry policy (Ollama can be slower)
        request.retryPolicy = DefaultRetryPolicy(
            45000, // 45 seconds timeout (Ollama can be slower than direct models)
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Add request to queue
        Volley.newRequestQueue(this).add(request)
    }
}