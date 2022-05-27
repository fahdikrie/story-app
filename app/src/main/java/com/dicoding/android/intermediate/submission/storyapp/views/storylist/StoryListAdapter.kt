package com.dicoding.android.intermediate.submission.storyapp.views.storylist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.android.intermediate.submission.storyapp.databinding.ItemStoryCardBinding
import com.dicoding.android.intermediate.submission.storyapp.models.responses.StoryItem
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity
import com.dicoding.android.intermediate.submission.storyapp.views.storydetail.StoryDetailActivity.Companion.EXTRA_STORY_DETAIL


class StoryListAdapter(
    private val storyList: List<StoryItem?>
) : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ViewHolder(private val binding: ItemStoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: StoryItem) {
//            val circularProgress = CircularProgressDrawable(this)
//            circularProgress.strokeWidth = 5f
//            circularProgress.centerRadius = 30f
//            circularProgress.start()

            binding.apply {
                storyTitleTv.text = story.name
                storyDescTv.text = story.description
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .fitCenter()
//                    .apply(
//                        RequestOptions
//                            .placeholderOf(R.drawable.ic_loading)
//                            .error(R.drawable.ic_error))
                    .into(storyImageIv)
                
                root.setOnClickListener {
//                    val optionsCompat: ActivityOptionsCompat =
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            root.context as Activity,
//                            Pair(ivStoryImage, "story_image"),
//                            Pair(tvStoryUsername, "username"),
//                            Pair(tvStoryDate, "date"),
//                            Pair(tvStoryDescription, "description")
//                        )

                    Intent(context, StoryDetailActivity::class.java).also { intent ->
                        intent.putExtra(EXTRA_STORY_DETAIL, story)
                        context.startActivity(intent)
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