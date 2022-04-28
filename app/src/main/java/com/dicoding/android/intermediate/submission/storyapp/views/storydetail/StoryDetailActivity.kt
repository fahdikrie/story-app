package com.dicoding.android.intermediate.submission.storyapp.views.storydetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryDetailBinding

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}