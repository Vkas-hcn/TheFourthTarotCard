package com.date.time.dog.thefourthtarotcard.data

import com.date.time.dog.thefourthtarotcard.R
import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.date.time.dog.thefourthtarotcard.DetailActivity
import com.date.time.dog.thefourthtarotcard.EndActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream

object DownloadImageUtils {
    private val REQUEST_PERMISSION_CODE = 123
    private var imageDrawableId = R.drawable.icon_car
    fun clickImage(activity: Activity) {
        if (hasWritePermission(activity)) {
            downloadAndSaveImage(activity)
        } else {
            requestWritePermission(activity)
        }
    }

    fun clickImageBitMap(activity: Activity, bitmap: Bitmap) {
        if (hasWritePermission(activity)) {
            downloadAndSaveBitMap(activity, bitmap)
        } else {
            requestWritePermission(activity)
        }
    }

    fun clickShare(activity: Activity, bitmap: Bitmap? = null) {
        if (hasWritePermission(activity)) {
            shareImage(activity, bitmap)
        } else {
            requestWritePermission(activity)
        }
    }

    private fun hasWritePermission(activity: Activity): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestWritePermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }


    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        activity: Activity,
        nextFun: () -> Unit
    ) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadAndSaveImage(activity)
            } else {
                nextFun()
            }
        }
    }


    fun openAppSettings(activity: Activity) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${activity.packageName}")
        activity.startActivity(intent)
    }

    private fun downloadAndSaveImage(activity: Activity) {
        if (activity is EndActivity) {
            imageDrawableId = activity.downloadImage
        }

        GlobalScope.launch(Dispatchers.IO) {
            val imageDrawable = ContextCompat.getDrawable(activity, imageDrawableId)
            if (imageDrawable is BitmapDrawable) {
                val bitmap = imageDrawable.bitmap
                val savedUri =
                    saveImageToGallery(bitmap, "MyImage", "My Image Description", activity)
                if (savedUri != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            activity,
                            "Image saved to gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            activity,
                            "Failed to save picture",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun downloadAndSaveBitMap(activity: Activity, bitmap: Bitmap) {
        GlobalScope.launch(Dispatchers.IO) {
            val imageDrawable = ContextCompat.getDrawable(activity, imageDrawableId)
            if (imageDrawable is BitmapDrawable) {
                val savedUri =
                    saveImageToGallery(bitmap, "MyImage", "My Image Description", activity)
                if (savedUri != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            activity,
                            "Image saved to gallery",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            activity,
                            "Failed to save picture",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun saveImageToGallery(
        bitmap: Bitmap,
        title: String,
        description: String,
        activity: Activity
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, title)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DESCRIPTION, description)
        }

        val contentResolver: ContentResolver = activity.contentResolver
        val imageUri = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        imageUri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                return imageUri
            }
        }
        return null
    }

    private fun shareImage(activity: Activity, bitmap: Bitmap? = null) {
        if (activity is EndActivity) {
            imageDrawableId = activity.downloadImage
        }

        val imageDrawable = ContextCompat.getDrawable(activity, imageDrawableId)
        if (imageDrawable is BitmapDrawable) {
            val bitmap = if (activity is DetailActivity) {
                bitmap
            } else {
                imageDrawable.bitmap
            }
            val uri = saveImageToGallery(bitmap!!, "TarotImage", "TarotImage Description", activity)
            if (uri != null) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/*"
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                activity.startActivity(Intent.createChooser(shareIntent, "share pictures"))
            }
        }
    }
}