package com.example.messagemanager.domain

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.messagemanager.presentation.fragments.TAG
import kotlinx.coroutines.delay
import retrofit2.HttpException

class GetDataWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    var repository = MessageRepositoryImpl()

    override suspend fun doWork(): Result {



        delay(3000L)

        return try {
            val result = repository.getData()
            val output = workDataOf("success" to Helper.serializeToJson(result.take(30)))
            Result.success(output)
        } catch (e: HttpException){
            Log.d(TAG, "error - $e")
            Result.retry()
        }

        catch (e: Exception) {
            Result.failure()
        }

    }

}