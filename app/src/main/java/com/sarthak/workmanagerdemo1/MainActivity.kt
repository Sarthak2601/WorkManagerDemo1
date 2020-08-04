package com.sarthak.workmanagerdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
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
        // Constraints for the work manager request.
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        // Creates a one time work request corresponding to the uploading work manager class.
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorkManager::class.java)
            .setConstraints(constraints)
            .build()
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(oneTimeWorkRequest) // Enqueuing the work request using the work manager instance.
        // Getting work info by ID in the form of LiveData. We then observe these changes and display the workInfo's state name on the screen.
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this, Observer {
            textView.text = it.state.name
        })
    }
}
