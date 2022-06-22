package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.android.intermediate.submission.storyapp.databinding.ItemStoryCardBinding
import com.dicoding.android.intermediate.submission.storyapp.data.responses.StoryItem
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity.Companion.EXTRA_STORY_DETAIL


class StoryListAdapter(
    private val storyList: List<StoryItem?>
) : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(
        private val binding: ItemStoryCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: StoryItem) {
            binding.apply {
                storyTitleTv.text = story.name
                storyDescTv.text = story.description
                Glide.with(context)
                    .load(story.photoUrl)
                    .fitCenter()
                    .into(storyImageIv)
                
                root.setOnClickListener {
                    Intent(context, StoryDetailActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_STORY_DETAIL, story)
                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as Activity,
                                Pair(storyImageIv, "photoUrl"),
                                Pair(storyTitleTv, "name"),
                                Pair(storyDescTv, "description"),
                            )
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemStoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(holder.itemView.context, story as StoryItem)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(storyList[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int = storyList.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(story: StoryItem?)
    }
}