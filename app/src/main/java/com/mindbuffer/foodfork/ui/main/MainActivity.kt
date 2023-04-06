package com.mindbuffer.foodfork.ui.main

import android.Manifest
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mindbuffer.foodfork.R
import com.mindbuffer.foodfork.data.remote.connectivity.ConnectivityManager
import com.mindbuffer.foodfork.databinding.ActivityMainBinding
import com.mindbuffer.foodfork.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding<ActivityMainBinding>()

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkNotificationPermissionAndroidApi33()
        setupObserver()
    }

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }

    private fun setupObserver() {
        connectivityManager.isNetworkAvailable.observe(this) {
            if (it)
                binding.connectionStatus.visibility = View.GONE
            else
                binding.connectionStatus.visibility = View.VISIBLE
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                // PERMISSION GRANTED
            } else {
                // PERMISSION NOT GRANTED
            }
        }

    private fun checkNotificationPermissionAndroidApi33() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}