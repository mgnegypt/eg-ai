package com.mgn.ai.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.mgn.ai.BuildConfig
import com.mgn.ai.RouteActivity
import com.mgn.ai.ui.locale.applyAppLocale
import java.io.File

class ShortcutHandlerActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(applyAppLocale(newBase))
    }

    private var photoURI: Uri? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            finish()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoURI?.let {
                val intent = Intent(this, RouteActivity::class.java).apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, it.toString())
                }
                startActivity(intent)
            }
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun launchCamera() {
        val imageFile = File(cacheDir, "shortcut_camera_image.jpg")
        photoURI = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", imageFile)
        photoURI?.let {
            takePictureLauncher.launch(it)
        } ?: finish()
    }
}
