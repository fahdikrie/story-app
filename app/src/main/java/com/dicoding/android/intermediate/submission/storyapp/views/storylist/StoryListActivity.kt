package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryListBinding

class StoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}