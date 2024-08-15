package com.example.bmi.Ui.ChatAI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmi.Base.BaseFragment
import com.example.bmi.Database.ChatDatabaseHelper
import com.example.bmi.Model.ChatMessage
import com.example.bmi.R
import com.example.bmi.Ui.ChatAI.Adapter.ChatAdapter
import com.example.bmi.databinding.FragmentHistoryBinding

class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var textViewEmptyConversation: TextView
    private lateinit var dbHelper: ChatDatabaseHelper
    override fun inflateViewBinding(): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(layoutInflater)
    }

    //    var bannerManager: BannerManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView)
        textViewEmptyConversation = view.findViewById(R.id.textViewEmptyConversation)

        // Initialize database helper and chat adapter
        dbHelper = ChatDatabaseHelper(context)
        chatAdapter = ChatAdapter(
            context,
            textViewEmptyConversation
        )

        // Set layout manager and adapter to RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = chatAdapter

        // Load chat history
        loadChatHistory()
        return view
    }

    override fun onResume() {
        super.onResume()
        loadChatHistory()
    }

    private fun loadChatHistory() {
        val messages: List<ChatMessage> = dbHelper.allMessages
        if (messages.isEmpty()) {
            // If there are no messages, show empty conversation message
            textViewEmptyConversation.visibility = View.VISIBLE
            chatAdapter.setMessages(messages)
        } else {
            // If there are messages, hide empty conversation message and set messages to adapter
            textViewEmptyConversation.visibility = View.GONE
            chatAdapter.setMessages(messages)
        }
    }
}