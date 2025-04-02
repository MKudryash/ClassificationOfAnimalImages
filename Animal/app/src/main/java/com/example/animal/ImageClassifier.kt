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

/**
 * Класс ImageClassifier для классификации изображений с использованием модели TensorFlow Lite*
 * */
class ImageClassifier(context: Context) {
    // Интерпретатор для выполнения модели
    private val interpreter: Interpreter?

    // Размер входного изображения
    private val inputSize = 224

    // Инициализация класса
    init {
        // Загрузка файла модели из ресурсов
        val modelFile = loadModelFile(context)
        // Настройки интерпретатора
        val options = Interpreter.Options()
        // Создание интерпретатора с загруженной моделью
        interpreter = modelFile?.let { Interpreter(it, options) }
    }

    // Метод для загрузки файла модели из ресурсов
    private fun loadModelFile(context: Context): MappedByteBuffer? {
        return try {
            // Открытие дескриптора файла модели
            val assetFileDescriptor = context.assets.openFd("animals10_1.tflite")
            // Создание InputStream для чтения файла
            val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            // Получение канала файла
            val fileChannel = inputStream.channel
            // Получение смещения и длины файла
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            // Возвращение смонтированного байтового буфера для чтения
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        } catch (ex: Exception) {
            Log.e("LoadModelFileError", "Error loading model file: ${ex.message}", ex)
            null // Возвращаем null в случае ошибки
        }
    }
    // Метод для классификации изображения
    fun classifyImage(bitmap: Bitmap): String {
        return try {
            // Изменение размера изображения до нужного формата
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
            // Предобработка изображения для подачи в модель
            val input = preprocessImage(scaledBitmap)
            // Массив для хранения выходных данных модели
            val output = Array(1) { FloatArray(10) }
            // Запуск интерпретатора с входными данными и получение выходных данных
            interpreter?.run(input, output)
            // Обработка результата и возврат класса
            postprocessResult(output[0])
        } catch (ex: Exception) {
            Log.e("ClassifyImageError", "Error during image classification: ${ex.message}")
            "Error" // Возвращаем строку с сообщением об ошибке или любое другое значение по умолчанию
        }
    }

    // Метод для предобработки изображения
    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        return try {
            // Создание байтового буфера для хранения данных изображения
            val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder()) // Установка порядка байтов

            // Массив для хранения значений пикселей
            val intValues = IntArray(inputSize * inputSize)
            // Получение пикселей из изображения
            bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            // Преобразование значений пикселей в формат, подходящий для модели
            for (pixelValue in intValues) {
                // Извлечение значений RGB и нормализация
                byteBuffer.putFloat(((pixelValue shr 16) and 0xFF) / 255.0f) // Красный
                byteBuffer.putFloat(((pixelValue shr 8) and 0xFF) / 255.0f)  // Зеленый
                byteBuffer.putFloat((pixelValue and 0xFF) / 255.0f)         // Синий
            }

            // Возвращение подготовленного байтового буфера
            byteBuffer
        } catch (ex: Exception) {
            Log.e("PreprocessImageError", "Error during image preprocessing: ${ex.message}")
            ByteBuffer.allocateDirect(0) // Возвращаем пустой байтовый буфер в случае ошибки
        }
    }

    // Метод для обработки результата классификации
    private fun postprocessResult(output: FloatArray): String {
        // Списки классов на русском и английском языках
        val classesOfRussian = listOf(
            "бабочка", "кот", "лошадь", "курица", "корова",
            "слон", "собака", "паук", "овца", "белка"
        )
        val classesOfEnglish = listOf(
            "butterfly", "cat", "horse", "chicken", "cow",
            "elephant", "dog", "spider", "sheep", "squirrel"
        )
        // Нахождение индекса максимального значения в выходном массиве
        val maxIndex = output.indices.maxByOrNull { output[it] } ?: -1
        // Возврат соответствующего класса или "Unknown", если индекс вне диапазона
        return if (maxIndex in classesOfEnglish.indices) classesOfEnglish[maxIndex] else "Unknown"
    }
}