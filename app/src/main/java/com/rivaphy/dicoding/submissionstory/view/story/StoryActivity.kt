package com.rivaphy.dicoding.submissionstory.view.story

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.rivaphy.dicoding.submissionstory.data.api.ApiConfig
import com.rivaphy.dicoding.submissionstory.data.api.response.StoryResponse
import com.rivaphy.dicoding.submissionstory.databinding.ActivityStoryBinding
import com.rivaphy.dicoding.submissionstory.uriToFile
import com.rivaphy.dicoding.submissionstory.view.ViewModelFactory
import com.rivaphy.dicoding.submissionstory.view.main.MainActivity
import com.rivaphy.dicoding.submissionstory.view.main.MainViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var getImage: Uri? = null
    private lateinit var token: String

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            getImage = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        getImage?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivCamera.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.getSession().observe(this) {
            if (it != null) {
                token = it.token
                ApiConfig.setToken(token)
                binding.btnCameraGallery.setOnClickListener { startGallery() }
                binding.btnCameraUpload.setOnClickListener {
                    val desc = binding.edtCameraDesc.text.toString()
                    startUpload(desc)
                }
            } else {
                toastMessage("Failed to get token")
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun startUpload(description: String) {
        if (description.isBlank()) {
            toastMessage("Description cannot be empty")
            return
        }

        getImage?.let {
            val imageFile = uriToFile(it, this)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            showLoading(true)

            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val response = apiService.postUserStories(multipartBody, requestBody)
                    showLoading(false)
                    val intent = Intent(this@StoryActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } catch (e: HttpException) {
                    val body = e.response()?.errorBody()?.string()
                    val response = Gson().fromJson(body, StoryResponse::class.java)
                    toastMessage("Upload failed: ${response.message}")
                    showLoading(false)
                } catch (e: Exception) {
                    toastMessage("Upload failed: ${e.message}")
                    showLoading(false)
                }
            }
        } ?: toastMessage("No Image")
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressCamera.visibility = View.VISIBLE
        } else {
            binding.progressCamera.visibility = View.GONE

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

}