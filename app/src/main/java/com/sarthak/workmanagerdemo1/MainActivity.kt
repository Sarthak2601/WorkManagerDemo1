package com.sarthak.workmanagerdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { setOneTimeWorkRequest() }
    }

    private fun setOneTimeWorkRequest() {
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorkManager::class.java).build()
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(oneTimeWorkRequest)
    }
}
