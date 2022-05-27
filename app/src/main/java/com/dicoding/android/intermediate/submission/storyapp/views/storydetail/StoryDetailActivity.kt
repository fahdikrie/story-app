package com.dicoding.android.intermediate.submission.storyapp.views.storydetail

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.dicoding.android.intermediate.submission.storyapp.R
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryDetailBinding
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryItem
import com.dicoding.android.intermediate.submission.storyapp.utils.withDateFormat

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var story: StoryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        story = intent.getParcelableExtra(EXTRA_STORY_DETAIL)!!

        setHeader()
        bindContent()
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

    private fun bindContent() {
        binding.apply {
            storyDetailTitleTv.text = story.name ?: "-"
            storyDetailDateTv.text = resources.getString(
                R.string.created_at_text,
                story.createdAt?.withDateFormat() ?: "-"
            )
            storyDetailDescTv.text = story.description ?: "-"
            Glide
                .with(this@StoryDetailActivity)
                .load(story.photoUrl)
                .fitCenter()
                .into(storyDetailImageIv)
        }
    }

    companion object {
        const val EXTRA_STORY_DETAIL = "extra_story_detail"
    }
}