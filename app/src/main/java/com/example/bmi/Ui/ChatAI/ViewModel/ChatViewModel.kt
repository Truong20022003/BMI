package com.example.bmi.Ui.ChatAI.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bmi.ApiService.ChatService
import com.example.bmi.Model.ChatRequest
import com.example.bmi.Utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel

import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject



@HiltViewModel
class ChatViewModel @Inject constructor(private val retrofit: Retrofit) : ViewModel() {

    private var disposable: Disposable? = null
    private val _chatLiveData = MutableLiveData<String>()
    val chatLiveData: LiveData<String> get() = _chatLiveData
    val chatFlow = MutableStateFlow<String>("")
    fun sendChat(text: String) {
        val stringBuilder = StringBuilder()
        disposable = retrofit.create(ChatService::class.java)
            .chatData(ChatRequest(text, "English", true))
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : DisposableObserver<ResponseBody>() {
                override fun onNext(responseBody: ResponseBody) {
                    try {
                        val reader = BufferedReader(InputStreamReader(responseBody.byteStream()))
                        var line: String?
                        var data = ""
                        Log.v("streaming", "request ")
                        while (reader.readLine().also { line = it } != null) {
                            Log.v("streaming", "line $line")
                            if (line!!.startsWith("data:")) {
                                val actualData = line!!.substring(6) // Remove "data:" prefix
                                stringBuilder.append(actualData) // Append actual data with a space
                                val newValue = stringBuilder.toString().replace("\\n", "\n")
                                data = newValue
                                Log.d("streaming", "onNext: $newValue $actualData")
                                viewModelScope.launch {
                                    chatFlow.emit(newValue)
                                }
                            }
                        }
//                        _chatLiveData.postValue(data)
                    } catch (e: IOException) {
                        Log.d("streaming", "Parsing data exception: ${e.message}")
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("streaming", "onError: ${e.message}")
                }

                override fun onComplete() {
                    Log.d("streaming", "onComplete")
//                    _chatLiveData.postValue()
                    viewModelScope.launch {
                        chatFlow.emit(Constants.STREAMING_COMPLETED)
                    }
                }
            })
    }
    override fun onCleared() {
        super.onCleared()
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}

