package com.example.animal.ScreenImages

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.animal.ImageClassifier


@Composable
fun UploadImages(navController: NavHostController) {
    val context = LocalContext.current

    val urlImage = remember { mutableStateOf("") }
    var loadImage = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = urlImage.value,
            onValueChange = {
                urlImage.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.LightGray,
                focusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(15.dp),
        )

        Button(onClick = {
            loadImage.value = true
        }) {
            Text("Upload Image")
        }
        if (loadImage.value) {
            LoadImage(urlImage.value)
        }
    }
}

@Composable
fun LoadImage(urlImage: String) {
    val context = LocalContext.current
    val imageClass = ImageClassifier(context)
    var result by remember { mutableStateOf("") }
    var bitmap: Bitmap? = null


    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(urlImage)
            .size(Size.ORIGINAL).build()
    ).state

    when (imageState) {
        AsyncImagePainter.State.Empty -> {

        }

        is AsyncImagePainter.State.Error -> {
            Text("Error loading")
        }

        is AsyncImagePainter.State.Loading -> {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator()
            }
        }

        is AsyncImagePainter.State.Success -> {

        }
    }
    if (imageState is AsyncImagePainter.State.Success) {
        val drawable = imageState.result.drawable

        bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)


        drawable.setBounds(0, 0, canvas.width, canvas.height)


        if (drawable is BitmapDrawable) {
            val hardwareBitmap = drawable.bitmap
            if (hardwareBitmap.config == Bitmap.Config.HARDWARE) {
                val softwareBitmap = hardwareBitmap.copy(Bitmap.Config.ARGB_8888, true)
                canvas.drawBitmap(softwareBitmap, 0f, 0f, null)
            } else {
                drawable.draw(canvas)
            }
        } else {
            drawable.draw(canvas)
        }

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Button(onClick = {
            bitmap.let {
                result = "The predicted animal: ${imageClass.classifyImage(it)}"
            }
        }) {
            Text("Classify Image")
        }
        Text(result)
    }

}
