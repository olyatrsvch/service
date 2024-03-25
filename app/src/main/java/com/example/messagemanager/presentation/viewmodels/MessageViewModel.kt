package com.example.messagemanager.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.messagemanager.data.MessageDataItem
import com.example.messagemanager.domain.GetDataWorker
import com.example.messagemanager.domain.Helper
import com.example.messagemanager.domain.MessageRepository
import com.example.messagemanager.domain.MessageRepositoryImpl
import com.example.messagemanager.presentation.fragments.TAG
import kotlinx.coroutines.launch

class MessageViewModel(private val repository: MessageRepositoryImpl): ViewModel() {


    val _messageList = MutableLiveData<List<MessageDataItem>>()
    val messageList: LiveData<List<MessageDataItem>> get() = _messageList

    fun getData() {
        viewModelScope.launch {
            _messageList.postValue(repository.getData())
        }
    }

    fun postData(messages: List<MessageDataItem>) {
        viewModelScope.launch {
            repository.postData(messages)
            getData()
        }
    }

}


class MessageViewModelFactory(private val repository: MessageRepositoryImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}