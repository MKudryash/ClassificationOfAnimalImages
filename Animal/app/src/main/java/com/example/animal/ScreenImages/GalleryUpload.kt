package com.example.animal.ScreenImages

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavHostController
import com.example.animal.ImageClassifier

private lateinit var getImage: ActivityResultLauncher<Intent>
private lateinit var requestPermission: ActivityResultLauncher<String>

@Composable
fun GalleryUpload(navController: NavHostController) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    getImage =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data?.data
            }
        }

    requestPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(
                    context,
                    "Разрешение на доступ к хранилищу отклонено",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    ImagePickerScreen(imageUri) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            requestPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

}

private fun openGallery() {
    val intent = Intent(Intent.ACTION_PICK).apply {
        type = "image/*"
    }
    getImage.launch(intent)
}


@Composable
fun ImagePickerScreen(imageUri: Uri?, onPickImage: () -> Unit) {
    var result by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val imageClass = ImageClassifier(context)

    fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    imageUri?.let {
        bitmap = uriToBitmap(it)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onPickImage) {
            Text("Choose image")
        }
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
            )
            Button(onClick = {
                bitmap?.let { bmp ->
                    result = "The predicted animal: ${imageClass.classifyImage(bmp)}"
                }
            }) {
                Text("Classify Image")
            }

            Text(text = result)
        }
    }
}