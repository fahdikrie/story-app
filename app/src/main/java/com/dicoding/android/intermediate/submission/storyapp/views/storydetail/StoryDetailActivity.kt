package com.dicoding.android.intermediate.submission.storyapp.views.storydetail

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.android.intermediate.submission.storyapp.R
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryDetailBinding
import com.dicoding.android.intermediate.submission.storyapp.data.responses.StoryItem
import com.dicoding.android.intermediate.submission.storyapp.utils.withDateFormat
import com.dicoding.android.intermediate.submission.storyapp.views.factories.StoryViewModelFactory
import com.dicoding.android.intermediate.submission.storyapp.views.login.LoginActivity
import kotlinx.coroutines.launch

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private lateinit var story: StoryItem
    private lateinit var storyViewModelFactory: StoryViewModelFactory
    private val storyDetailViewModel: StoryDetailViewModel by viewModels {
        storyViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        storyViewModelFactory = StoryViewModelFactory.getInstance(this)
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
                    storyDetailViewModel.logout()
                    Intent(this@StoryDetailActivity, LoginActivity::class.java).also { intent ->
                        startActivity(intent)
                        finish()
                    }
                }
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