package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.android.intermediate.submission.storyapp.data.entities.StoryEntity
import com.dicoding.android.intermediate.submission.storyapp.databinding.ItemStoryCardBinding
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity.Companion.EXTRA_STORY_DETAIL


class StoryListAdapter :
    PagingDataAdapter<StoryEntity, StoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemStoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            Log.e("ERROR", story.name)
            holder.bind(holder.itemView.context, story)
        }
    }

    class ViewHolder(
        private val binding: ItemStoryCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: StoryEntity) {
            binding.apply {
                storyTitleTv.text = story.name
                storyDescTv.text = story.description
                Glide.with(context)
                    .load(story.photoUrl)
                    .fitCenter()
                    .into(storyImageIv)

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            Pair(storyImageIv, "photoUrl"),
                            Pair(storyTitleTv, "name"),
                            Pair(storyDescTv, "description"),
                        )

                    Intent(context, StoryDetailActivity::class.java).also {
                        it.putExtra(EXTRA_STORY_DETAIL, story)
                        context.startActivity(it, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}