package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.android.intermediate.submission.storyapp.databinding.ActivityStoryListBinding
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryItem
import com.dicoding.android.intermediate.submission.storyapp.views.factories.StoryViewModelFactory
import com.dicoding.android.intermediate.submission.storyapp.views.factories.UserViewModelFactory
import com.dicoding.android.intermediate.submission.storyapp.views.register.RegisterViewModel
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity.Companion.EXTRA_STORY_DETAIL
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyListAdapter: StoryListAdapter
    private lateinit var storyViewModelFactory: StoryViewModelFactory
    private val storyListViewModel: StoryListViewModel by viewModels {
        storyViewModelFactory
    }
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        storyViewModelFactory = StoryViewModelFactory.getInstance(this)
        val view = binding.root
        setContentView(view)

        token = intent.getStringExtra(EXTRA_TOKEN)!!

        setHeader()
        bindViewModelToRV()
    }

    private fun setHeader() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun bindViewModelToRV() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                storyListViewModel.getStoryList(token).collect {
                    it.onSuccess { response ->
                        Toast.makeText(
                            this@StoryListActivity,
                            "Success!!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        response.listStory?.let { storyList -> showRecyclerList(storyList) }
                    }

                    it.onFailure {
                        Toast.makeText(
                            this@StoryListActivity,
                            "Error on wkwk",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showRecyclerList(storyList: List<StoryItem?>) {
        val linearLayoutManager = LinearLayoutManager(this)
        storyListAdapter = StoryListAdapter(storyList)

        recyclerView = binding.storyListRv
        recyclerView.apply {
            adapter = storyListAdapter
            layoutManager = linearLayoutManager
        }

        storyListAdapter.setOnItemClickCallback(object : StoryListAdapter.OnItemClickCallback {
            override fun onItemClicked(story: StoryItem?) {
                val intentToDetail = Intent(this@StoryListActivity, StoryDetailActivity::class.java)
                intentToDetail.putExtra(EXTRA_STORY_DETAIL, story)
                startActivity(intentToDetail)
            }
        })
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}