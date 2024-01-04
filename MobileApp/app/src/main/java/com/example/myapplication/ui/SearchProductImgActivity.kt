package com.example.myapplication.ui

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.ml.ModelTflite
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SearchProductImgActivity : AppCompatActivity() {

    private lateinit var GalleryBtn: Button
    private lateinit var SearchImg: Button
    private lateinit var PreviewImg: ImageView
    private lateinit var TxtPrediction: TextView
    private lateinit var bitmap: Bitmap
    private val labels = listOf("Ayaka", "Killua", "Zoro", "Asuka", "Kurumi")
    private val imageSize = 299

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product_img)

        GalleryBtn = findViewById(R.id.gallery_btn)
        SearchImg = findViewById(R.id.searchImageBtn)
        PreviewImg = findViewById(R.id.previewImageView)
        TxtPrediction = findViewById(R.id.textPrediction)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(imageSize, imageSize, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        GalleryBtn.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }
            startActivityForResult(intent, 100)
        }

        SearchImg.setOnClickListener {
            // Check whether image is selected or not
            if (!::bitmap.isInitialized) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)

            val processedImage = imageProcessor.process(tensorImage)

            val model = ModelTflite.newInstance(this)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(convertBitmapToByteBuffer(processedImage.bitmap))

            // Run model inference and get the result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

            val maxIdx = outputFeature0.indices.maxByOrNull { outputFeature0[it] } ?: -1

            if (maxIdx >= 0 && maxIdx < labels.size) {
                val classificationResult = labels[maxIdx]
                TxtPrediction.text = classificationResult

                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra("prediction", classificationResult)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid classification result", Toast.LENGTH_SHORT).show()
            }

            model.close()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            val uri = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            PreviewImg.setImageBitmap(bitmap)
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(imageSize * imageSize)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val value = intValues[pixel++]

                byteBuffer.putFloat(((value shr 16) and 0xFF) / 255.0f)
                byteBuffer.putFloat(((value shr 8) and 0xFF) / 255.0f)
                byteBuffer.putFloat((value and 0xFF) / 255.0f)
            }
        }

        return byteBuffer
    }
}