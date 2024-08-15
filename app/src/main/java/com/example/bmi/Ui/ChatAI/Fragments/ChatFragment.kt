package com.example.bmi.Ui.ChatAI.Fragments


import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bmi.Base.BaseFragment
import com.example.bmi.R
import com.example.bmi.Ui.ChatAI.Adapter.ChatAdapter
import com.example.bmi.Ui.ChatAI.ViewModel.ChatViewModel
import com.example.bmi.Utils.Config
import com.example.bmi.Utils.Constants
import com.example.bmi.databinding.FragmentChatBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.java.GenerativeModelFutures
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding>() {
    //    private var adView: AdView? = null
    private var model: GenerativeModelFutures? = null
//    private val executor: Executor = Executors.newSingleThreadExecutor()
    private var chatAdapter: ChatAdapter? = null
    private var viewModel: ChatViewModel? = null

    override fun inflateViewBinding(): FragmentChatBinding {
        return FragmentChatBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
//        loadCollapBanner()
//        loadNativeAds()
        chatAdapter =
            ChatAdapter(
                requireActivity(),
                viewBinding.textViewEmpty
            )
        viewBinding.recyclerViewChat.adapter = chatAdapter

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        // Initialize Generative Model
        val gm = GenerativeModel("gemini-pro", Config.apiKey)
        model = GenerativeModelFutures.from(gm)

        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        viewModel?.chatLiveData?.observe(viewLifecycleOwner) { data ->

        }

        lifecycleScope.launch {
            viewModel?.chatFlow?.collect { data ->
                withContext(Dispatchers.Main) {
                    if (data == Constants.STREAMING_COMPLETED) {
                        chatAdapter?.saveMessageToDatabase(chatAdapter?.getMessages()?.last())
                        return@withContext
                    }
                    Log.v("tag111", "aaaa $data")
                    chatAdapter?.updateLastMessage(data)
                    if ((chatAdapter?.itemCount ?: 0) > 0) {
                        viewBinding.recyclerViewChat.smoothScrollToPosition(chatAdapter!!.itemCount - 1)
                    }
                }
            }
        }

        viewBinding.editTextUserInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isNotEmpty()) {
                    viewBinding.actionButton.setImageResource(R.drawable.ic_send_ai_on)
                    viewBinding.editTextUserInput.setBackgroundResource(R.drawable.bg_edit_text_on)
                } else {
                    viewBinding.actionButton.setImageResource(R.drawable.ic_send_ai_off)
                    viewBinding.editTextUserInput.setBackgroundResource(R.drawable.bg_edit_text_off)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        viewBinding.actionButton.setOnClickListener() {
            val userInput = viewBinding.editTextUserInput.text.toString().trim()

            if (userInput.isNotEmpty()) {
                sendMessage()
//                if (ClickCounter.increment() % 3 == 0) {}
            } else {
                Toast.makeText(requireActivity(), "Please type something before sending", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Method to send user message and receive bot response
    private fun sendMessage() {
        val userInput = viewBinding.editTextUserInput.text.toString().trim()
        if (userInput.isNotEmpty()) {
            chatAdapter?.addMessage(userInput, false)
            viewBinding.editTextUserInput.text.clear()

            chatAdapter?.addMessage("", true)
            viewModel?.sendChat(userInput)
        }
    }

    // Inner class to count button clicks
    private object ClickCounter {
        private var count = 0

        fun increment(): Int {
            return ++count
        }
    }


}