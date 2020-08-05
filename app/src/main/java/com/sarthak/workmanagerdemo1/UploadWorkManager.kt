package com.sarthak.workmanagerdemo1

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class UploadWorkManager(context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object{
        const val KEY_WORKER = "key_worker"
    }
    override fun doWork(): Result {
        return try {
            val inputData = inputData.getInt(MainActivity.KEY_CONSTANT, 0)
            for (i in 0 until inputData) {
                Log.i("WORK", "Uploading $i")
            }
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            val outputValue = Data.Builder().putString(KEY_WORKER, currentDate).build()

            Result.success(outputValue)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }
}