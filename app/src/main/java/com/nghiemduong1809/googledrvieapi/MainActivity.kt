package com.nghiemduong1809.googledrvieapi

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.nghiemduong1809.googledrvieapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainMV: MainViewModel

    private val launcherSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d("TAG123", "${it.data?.data} \n resultCode = ${it.resultCode}")
            if (it.resultCode == Activity.RESULT_OK) {

            }
        }

    private val launcherSelectFile =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.data?.let { uri ->
                mainMV.backupDrive(uri = uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initOnClickListeners()
    }

    private fun initViews() {
        mainMV = MainViewModel(application)
    }

    private fun initOnClickListeners() {
        binding.btnLogin.setOnClickListener {
            Toast.makeText(this, "${mainMV.isUserSignIn()}", Toast.LENGTH_SHORT).show()
            launcherSignIn.launch(mainMV.getIntentSignInGoogleAccount())
        }

        binding.btnUpload.setOnClickListener {
            launcherSelectFile.launch(mainMV.getSelectFileIntent())
        }
    }
}