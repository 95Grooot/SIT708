package com.example.llamachatbot

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.llamachatbot.databinding.ActivityChatBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()
    private var username: String = ""


    private val BACKEND_URL = " http://127.0.0.1:5001"

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
        addMessage("Welcome User!", false)
    }

    private fun sendMessage() {
        val messageText = binding.messageEditText.text.toString().trim()

        if (messageText.isEmpty()) {
            return
        }

        // Add user message to chat
        addMessage(messageText, true)
        binding.messageEditText.text.clear()

        // Show typing indicator
        showTypingIndicator(true)

        // Send message to backend
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = makeHttpRequest(message)

                withContext(Dispatchers.Main) {
                    showTypingIndicator(false)
                    if (response != null) {
                        addMessage(response, false)
                    } else {
                        addMessage("Sorry, I'm having trouble connecting. Please try again.", false)
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatActivity", "Error sending message", e)
                withContext(Dispatchers.Main) {
                    showTypingIndicator(false)
                    addMessage("Sorry, something went wrong. Please try again.", false)
                }
            }
        }
    }

    private suspend fun makeHttpRequest(message: String): String? {
        return try {
            val url = URL(BACKEND_URL)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            // Create JSON payload
            val jsonPayload = JSONObject().apply {
                put("message", message)
                put("username", username)
            }

            // Send request
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonPayload.toString())
                writer.flush()
            }

            // Read response
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { reader ->
                    val response = reader.readText()
                    val jsonResponse = JSONObject(response)
                    jsonResponse.optString("response", "No response from server")
                }
            } else {
                Log.e("ChatActivity", "HTTP Error: ${connection.responseCode}")
                null
            }
        } catch (e: Exception) {
            Log.e("ChatActivity", "Network error", e)
            // For demo purposes, return a mock response
            "Hello! I'm a Llama 2 chatbot. How can I help you today?"
        }
    }
}