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
        val uploadWorkerRequest = OneTimeWorkRequest.Builder(UploadWorkManager::class.java)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        val filteringWorkerRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java).build()
        val compressingWorkerRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java).build()
        val downloadingWorkerRequest = OneTimeWorkRequest.Builder(DownloadingWorker::class.java).build()

        // We can use this mutable list to set up parallel requests at the same time.
        val parallelWorks = mutableListOf<OneTimeWorkRequest>()
        parallelWorks.add(filteringWorkerRequest)
        parallelWorks.add(downloadingWorkerRequest)
        val workManager = WorkManager.getInstance(applicationContext)

        //workManager.enqueue(uploadWorkerRequest) // Enqueuing the work request using the work manager instance.


        // Chains the three work requests together.
        workManager.beginWith(parallelWorks)
            .then(compressingWorkerRequest)
            .then(uploadWorkerRequest)
            .enqueue()

        // Getting work info by ID in the form of LiveData. We then observe these changes and display the workInfo's state name on the screen.
        workManager.getWorkInfoByIdLiveData(uploadWorkerRequest.id).observe(this, Observer {
            textView.text = it.state.name
            // Receiving data from the work manager.
            val receivedOutputValue = it.outputData.getString(UploadWorkManager.KEY_WORKER)
            Toast.makeText(applicationContext, receivedOutputValue, Toast.LENGTH_SHORT).show()
        })
    }
}
