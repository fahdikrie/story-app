package com.dicoding.android.intermediate.submission.storyapp.views.storyupload

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.dicoding.android.intermediate.submission.storyapp.R
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryUploadBinding
import com.dicoding.android.intermediate.submission.storyapp.views.factories.StoryViewModelFactory

class StoryUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryUploadBinding
    private lateinit var storyViewModelFactory: StoryViewModelFactory
    private val storyUploadViewModel: StoryUploadViewModel by viewModels {
        storyViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryUploadBinding.inflate(layoutInflater)
        storyViewModelFactory = StoryViewModelFactory.getInstance(this)
        val view = binding.root
        setContentView(view)

        setHeader()
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
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                return true
            }
            R.id.logout -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}