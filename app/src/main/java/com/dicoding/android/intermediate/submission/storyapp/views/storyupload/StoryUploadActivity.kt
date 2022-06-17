package com.dicoding.android.intermediate.submission.storyapp.views.storyupload

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.android.intermediate.submission.storyapp.R
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryUploadBinding
import com.dicoding.android.intermediate.submission.storyapp.utils.createCustomTempFile
import com.dicoding.android.intermediate.submission.storyapp.utils.reduceFileImage
import com.dicoding.android.intermediate.submission.storyapp.utils.uriToFile
import com.dicoding.android.intermediate.submission.storyapp.views.factories.StoryViewModelFactory
import com.dicoding.android.intermediate.submission.storyapp.views.login.LoginActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryUploadBinding
    private lateinit var storyViewModelFactory: StoryViewModelFactory
    private val storyUploadViewModel: StoryUploadViewModel by viewModels {
        storyViewModelFactory
    }
    private lateinit var token: String
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryUploadBinding.inflate(layoutInflater)
        storyViewModelFactory = StoryViewModelFactory.getInstance(this)
        val view = binding.root
        setContentView(view)

        token = intent.getStringExtra(EXTRA_TOKEN_UPLOAD)!!
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setHeader()
        bindButtons()
    }

    private fun setHeader() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF3700B3")))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu_post_login, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            R.id.logout -> {
                lifecycleScope.launch {
                    storyUploadViewModel.logout()
                    Intent(this@StoryUploadActivity, LoginActivity::class.java).also { intent ->
                        startActivity(intent)
                        finish()
                    }
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bindButtons() {
        binding.cameraBtn.setOnClickListener { startTakePhoto() }
        binding.galleryBtn.setOnClickListener { startGallery() }
        binding.uploadBtn.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@StoryUploadActivity,
                "com.dicoding.android.intermediate.submission.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.previewIv.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@StoryUploadActivity)

            getFile = myFile

            binding.previewIv.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        val isFormValid: Boolean = validateData()

        if (isFormValid) {
            setLoading(true)
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val description = binding
                .storyDescriptionEt
                .text.toString()
                .toRequestBody("text/plain".toMediaType())
            val image = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            lifecycleScope.launchWhenStarted {
                launch {
                    storyUploadViewModel.postStoryItem(
                        token, image, description).collect { response ->
                        response.onSuccess {
                            Toast.makeText(
                                this@StoryUploadActivity,
                                getString(R.string.toast_story_upload_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }

                        response.onFailure {
                            Toast.makeText(
                                this@StoryUploadActivity,
                                getString(R.string.toast_story_upload_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        setLoading(false)
    }

    private fun validateData(): Boolean {
        val descFormData = binding.storyDescriptionEt.text.toString()
        if (descFormData.isEmpty() or descFormData.isBlank()) {
            binding.storyDescriptionEt.error = getString(R.string.story_description_layout_required)
            return false
        }

        if (getFile == null) {
            Toast.makeText(
                this,
                getString(R.string.toast_story_upload_image_required),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun setLoading(isLoading: Boolean) {
        binding.apply {
            cameraBtn.isEnabled = !isLoading
            galleryBtn.isEnabled = !isLoading
            uploadBtn.isEnabled = !isLoading
            storyDescriptionEt.isEnabled = !isLoading
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.toast_story_upload_no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        const val EXTRA_TOKEN_UPLOAD = "extra_token_upload"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}