package com.applocum.connecttomyhealth.ui.mysessions.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.applocum.connecttomyhealth.R
import com.applocum.connecttomyhealth.SepratedProgress
import com.applocum.connecttomyhealth.SoundMeter
import com.applocum.connecttomyhealth.ui.BaseActivity
import com.applocum.connecttomyhealth.ui.waitingarea.activities.WaitingAreaActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_my_session.*
import kotlinx.android.synthetic.main.custom_camera_permission_denied_dialog.view.*
import kotlinx.android.synthetic.main.custom_camera_permission_dialog.view.*
import kotlinx.android.synthetic.main.custom_microphone_permission_denied_dialog.view.*
import kotlinx.android.synthetic.main.custom_microphone_permission_dialog.view.*
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class MySessionActivity : BaseActivity(), MediaPlayer.OnCompletionListener {

    private var cameraRequestCode=1
    private var microPhoneRequestCode=2
    private lateinit var mediaPlayer:MediaPlayer
    lateinit var soundMeter: SoundMeter

    override fun getLayoutResourceId(): Int = R.layout.activity_my_session

    override fun handleInternetConnectivity(isConnect: Boolean?) {}

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaPlayer = MediaPlayer.create(this, R.raw.mpthreetest)
        soundMeter = SoundMeter()


        RxView.clicks(ivBack).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                mediaPlayer.stop()
                finish()
            }

        RxView.clicks(btnCameraContinue).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                llMobileCamera.visibility=View.GONE
                llTestSpeakers.visibility=View.GONE
                llMicroPhone.visibility=View.VISIBLE

                if (llMicroPhone.visibility == View.VISIBLE) {
                    val progress=SepratedProgress(ContextCompat.getColor(this,R.color.green),ContextCompat.getColor(this,R.color.textcolorgrey),this)
                    progressMicroPhone.progressDrawable = progress
                    audioPermission()
                }
            }

        RxView.clicks(btnMicrophoneContinue).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                llMobileCamera.visibility=View.GONE
                llTestSpeakers.visibility=View.VISIBLE
                llMicroPhone.visibility=View.GONE
                soundMeter.stop()
            }

        RxView.clicks(btnSpeakersContinue).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                startActivity(Intent(this,WaitingAreaActivity::class.java))
                mediaPlayer.stop()
                finish()
            }

        RxView.clicks(btnTestNow).throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                if (!mediaPlayer.isPlaying)
                {
                    mediaPlayer.start()
                    btnTestNow.isClickable = false
                }
            }

        mediaPlayer.setOnCompletionListener(this)


        if (llMobileCamera.visibility == View.VISIBLE) {
            cameraPermission()
            setCameraProviderListener()
         }
      }

    private fun setCameraProviderListener() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            try {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        previewView.implementationMode = (PreviewView.ImplementationMode.PERFORMANCE)
        val preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(previewView.display.rotation)
            .build()
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
        preview.setSurfaceProvider(previewView.surfaceProvider)
        cameraProvider.bindToLifecycle(this, cameraSelector, preview)
    }

    private fun cameraPermission() {
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.CAMERA), cameraRequestCode)
    }

    private fun audioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.RECORD_AUDIO), microPhoneRequestCode)
        }else {
            micOn()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode)
        {
            1->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                      previewView.visibility=View.VISIBLE
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                        // now, user has denied permission (but not permanently!)

                        val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_camera_permission_dialog, null, false)
                        val dialog = AlertDialog.Builder(this).create()
                        dialog.setView(showDialogView)

                        showDialogView.tvCameraYes.setOnClickListener {
                            cameraPermission()
                            dialog.dismiss()
                        }

                        showDialogView.tvCameraNo.setOnClickListener {
                            this.finish()
                            Toast.makeText(this,"Can't proceed for session as required permissions are not assigned",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }

                        dialog.show()

                    } else {
                        val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_camera_permission_denied_dialog, null, false)
                        val dialog = AlertDialog.Builder(this).create()
                        dialog.setView(showDialogView)

                        showDialogView.tvCameraSettingYes.setOnClickListener {
                            val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", this.packageName, null))
                            startActivityForResult(i, 1)
                            dialog.dismiss()
                        }

                        showDialogView.tvCameraSettingNo.setOnClickListener {
                            this.finish()
                            Toast.makeText(this,"Can't proceed for session as required permissions are not assigned",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }
            }

            2-> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    micOn()
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                        // now, user has denied permission (but not permanently!)

                        val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_microphone_permission_dialog, null, false)
                        val dialog = AlertDialog.Builder(this).create()
                        dialog.setView(showDialogView)

                        showDialogView.tvMicroPhoneYes.setOnClickListener {
                            audioPermission()
                            dialog.dismiss()
                        }

                        showDialogView.tvMicroPhoneNo.setOnClickListener {
                            this.finish()
                            Toast.makeText(this,"Can't proceed for session as required permissions are not assigned",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }

                        dialog.show()

                    } else {
                        val showDialogView = LayoutInflater.from(this).inflate(R.layout.custom_microphone_permission_denied_dialog, null, false)
                        val dialog = AlertDialog.Builder(this).create()
                        dialog.setView(showDialogView)

                        showDialogView.tvMicroPhoneSettingYes.setOnClickListener {
                            val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", this.packageName, null))
                            startActivityForResult(i, 2)
                            dialog.dismiss()
                        }

                        showDialogView.tvMicroPhoneSettingNo.setOnClickListener {
                            this.finish()
                            Toast.makeText(this,"Can't proceed for session as required permissions are not assigned",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                }

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun micOn()
    {
        soundMeter.start()
        try {
            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val amplitude = soundMeter.amplitude

                    val data = ((amplitude * 30) / 32767)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        progressMicroPhone.setProgress((data.roundToInt()), true)
                    }
                }
            }, 0, 100)
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        super.onPause()
       if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying)
        {
            mediaPlayer.stop()
        }
        soundMeter.stop()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        btnTestNow.isClickable = true
    }
}