package com.dicoding.android.intermediate.submission.storyapp.views.storyupload

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
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
import com.dicoding.android.intermediate.submission.storyapp.views.storylist.StoryListActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storylist.StoryListActivity.Companion.EXTRA_TOKEN
import com.dicoding.android.intermediate.submission.storyapp.views.storymap.StoryMapActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storymap.StoryMapActivity.Companion.EXTRA_TOKEN_MAP
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryUploadBinding
    private lateinit var currentPhotoPath: String
    private lateinit var storyViewModelFactory: StoryViewModelFactory
    private val storyUploadViewModel: StoryUploadViewModel by viewModels {
        storyViewModelFactory
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var token: String

    private var getFile: File? = null
    private var latLon: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryUploadBinding.inflate(layoutInflater)
        storyViewModelFactory = StoryViewModelFactory.getInstance(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
            R.id.story_map -> {
                Intent(this, StoryMapActivity::class.java).also { intent ->
                    intent.putExtra(EXTRA_TOKEN_MAP, token)
                    startActivity(intent)
                }
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
        binding.uploadBtn.setOnClickListener { uploadStory() }
        binding.swIsUsingLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) getUserLastLocation()
        }
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

    private val launcherIntentLocation =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getUserLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getUserLastLocation()
                }
                else -> {}
            }
        }

    private fun getUserLastLocation() {
        if (checkLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkLocationPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener {
                latLon = it
            }
        } else {
            launcherIntentLocation.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun uploadStory() {
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

            if (binding.swIsUsingLocation.isChecked) {
                val lat = latLon?.latitude.toString().toRequestBody()
                val lon = latLon?.longitude.toString().toRequestBody()

                lifecycleScope.launchWhenStarted {
                    launch {
                        storyUploadViewModel.postStoryItem(
                            token,
                            image,
                            description,
                            lat,
                            lon,
                        ).collect { response ->
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
                                    getString(R.string.toast_story_upload_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                lifecycleScope.launchWhenStarted {
                    launch {
                        storyUploadViewModel.postStoryItem(
                            token,
                            image,
                            description,
                        ).collect { response ->
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
                                    getString(R.string.toast_story_upload_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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

    private fun checkLocationPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val EXTRA_TOKEN_UPLOAD = "extra_token_upload"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}