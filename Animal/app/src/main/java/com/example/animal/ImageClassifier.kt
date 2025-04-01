package com.example.animal

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ImageClassifier(context: Context) {
    private val interpreter: Interpreter
    private val inputSize = 224

    init {
        val modelFile = loadModelFile(context)
        val options = Interpreter.Options()

             interpreter = Interpreter(modelFile, options)

    }

    private fun loadModelFile(context: Context): MappedByteBuffer {
        val assetFileDescriptor = context.assets.openFd("animals10_1.tflite")
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classifyImage(bitmap: Bitmap): String {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        val input = preprocessImage(scaledBitmap)
        val output = Array(1) { FloatArray(10) }
        interpreter.run(input, output)

        return postprocessResult(output[0])
    }

    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputSize * inputSize)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in intValues) {
            byteBuffer.putFloat(((pixelValue shr 16) and 0xFF) / 255.0f)
            byteBuffer.putFloat(((pixelValue shr 8) and 0xFF) / 255.0f)
            byteBuffer.putFloat((pixelValue and 0xFF) / 255.0f)
        }

        return byteBuffer
    }

    private fun postprocessResult(output: FloatArray): String {
        val classes = listOf("бабочка", "кот", "лошадь", "курица", "корова",
            "слон", "собака", "паук", "овца", "белка")
        val maxIndex = output.indices.maxByOrNull { output[it] } ?: -1
        return if (maxIndex in classes.indices) classes[maxIndex] else "Неизвестно"
    }
}