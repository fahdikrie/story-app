package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.android.intermediate.submission.storyapp.R
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryListBinding
import com.dicoding.android.intermediate.submission.storyapp.views.factories.StoryViewModelFactory
import com.dicoding.android.intermediate.submission.storyapp.views.login.LoginActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storymap.StoryMapActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storymap.StoryMapActivity.Companion.EXTRA_TOKEN_MAP
import com.dicoding.android.intermediate.submission.storyapp.views.storyupload.StoryUploadActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storyupload.StoryUploadActivity.Companion.EXTRA_TOKEN_UPLOAD
import kotlinx.coroutines.launch

class StoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyListAdapter: StoryListAdapter
    private lateinit var storyViewModelFactory: StoryViewModelFactory
    private val storyListViewModel: StoryListViewModel by viewModels {
        storyViewModelFactory
    }
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        storyViewModelFactory = StoryViewModelFactory.getInstance(this)
        val view = binding.root
        setContentView(view)

        token = intent.getStringExtra(EXTRA_TOKEN)!!
        recyclerView = binding.storyListRv
        storyListAdapter = StoryListAdapter()

        setHeader()
        bindFloatingActionBtn()
        showRecyclerView()

    }

    private fun setHeader() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF3700B3")))
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
                    storyListViewModel.logout()
                    Intent(this@StoryListActivity, LoginActivity::class.java).also { intent ->
                        startActivity(intent)
                        finish()
                    }
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bindFloatingActionBtn() {
        binding.storyAddFab.setOnClickListener {
            Intent(this, StoryUploadActivity::class.java).also { intent ->
                intent.putExtra(EXTRA_TOKEN_UPLOAD, token)
                startActivity(intent)
            }
        }
    }

    private fun showRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = storyListAdapter.withLoadStateFooter(
            footer = StoryLoadingStateAdapter {
                storyListAdapter.retry()
            }
        )

        val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                storyListViewModel.getStoryList(token).observeForever {
                    storyListAdapter.submitData(lifecycle, it)
                }
                recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (this::recyclerView.isInitialized) {
            recyclerView.smoothScrollToPosition(0)
        }
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}