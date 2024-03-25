package com.example.messagemanager.domain

import android.graphics.Bitmap
import com.example.messagemanager.data.MessageDataItem

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object Helper {

    fun serializeToJson(list: List<MessageDataItem>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    fun deserializeFromJson(jsonString: String?): List<MessageDataItem>? {
        val gson = Gson()
        val itemType = object : TypeToken<List<MessageDataItem>>() {}.type
        return gson.fromJson(jsonString, itemType)
    }

}