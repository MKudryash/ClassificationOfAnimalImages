# 🐾 ImageClassification

A mobile application for recognizing animals in photographs using TensorFlow and Jetpack Compose.

---

## 🛠 Technologies

- **Model**: TensorFlow + MobileNetV2 (transfer learning)
- **Mobile part**: Jetpack Compose (Kotlin)
- **Data**: Animal dataset from [Kaggle](https://www.kaggle.com/datasets/alessiocorrado99/animals10)
- **Additional**: Coil (image loading)

---

## 📁 Files

- 📦 **Trained model**: `animals10_1.tflite` - a ready-to-use model that recognizes animals ("butterfly", "cat", "horse", "chicken", "cow", "elephant", "dog", "spider", "sheep", "squirrel") in photos.
- 📱 **Mobile application**: A user-friendly interface that supports uploading photos from the gallery, via URL, and classification.
- 📚 **Model training file**: An example of model training that can be used for fine-tuning or adaptation.

---

## 📥 Installation and Launch

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-profile/ImageClassification.git
2. Open the project in Android Studio
3. Install dependencies via Gradle
4. Add the TensorFlow Lite model:
   - Place the animals10_1.tflite file in the app/src/main/assets folder
   - Configure the Classifier class to work with the model
   - Run the application on an emulator or device (Android 8.0+)

---
## 📸 Menggunakan
- **mage Upload**: Users can upload images from the gallery or via URL, or view already uploaded images in the gallery
- **Classification**: The application automatically identifies the type of animal in the uploaded image and displays the result 

For the Russian version, see [README.ru.md](README.ru.md).