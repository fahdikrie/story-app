package com.dicoding.android.intermediate.submission.storyapp.views.storyupload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryUploadBinding

class StoryUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}