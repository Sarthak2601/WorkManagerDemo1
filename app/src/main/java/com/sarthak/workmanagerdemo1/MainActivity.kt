package com.sarthak.workmanagerdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        const val KEY_CONSTANT = "key_constant"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { setOneTimeWorkRequest() }
    }

    private fun setOneTimeWorkRequest() {
        // Inputting data into the work manager.
        val inputData: Data = Data.Builder()
            .putInt(KEY_CONSTANT, 125)
            .build()
        // Constraints for the work manager request.
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        // Creates a one time work request corresponding to the uploading work manager class.
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorkManager::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueue(oneTimeWorkRequest) // Enqueuing the work request using the work manager instance.
        // Getting work info by ID in the form of LiveData. We then observe these changes and display the workInfo's state name on the screen.
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this, Observer {
            textView.text = it.state.name
            // Receiving data from the work manager.
            val receivedOutputValue = it.outputData.getString(UploadWorkManager.KEY_WORKER)
            Toast.makeText(applicationContext, receivedOutputValue, Toast.LENGTH_SHORT).show()
        })
    }
}
