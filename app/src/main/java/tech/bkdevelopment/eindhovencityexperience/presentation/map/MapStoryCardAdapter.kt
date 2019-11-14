package tech.bkdevelopment.eindhovencityexperience.presentation.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_map_story_card.view.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel

class MapStoryCardAdapter : RecyclerView.Adapter<MapStoryCardAdapter.StoryViewHolder>() {

    var stories: List<StoryViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onStoryCardClicked: ((StoryViewModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_map_story_card,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(viewModel: StoryViewModel) {
            itemView.run {
                mapStoryCardTitle.text = viewModel.title
                mapStoryCardDescription.text = viewModel.summaryText
                mapStoryCardDistance.text = context.getString(
                    R.string.map_story_card_distance,
                    viewModel.distanceToCurrentLocation.toString()
                )

                if (viewModel.completed) {
                    mapStoryCardImage.setImageDrawable(context.getDrawable(R.drawable.ic_lock_open_gray))
                } else {
                    if (viewModel.distanceToCurrentLocation <= DISTANCE_TO_UNLOCK_STORY) {
                        mapStoryCardImage.setImageDrawable(context.getDrawable(R.drawable.ic_lock_open_green))
                    } else {
                        mapStoryCardImage.setImageDrawable(context.getDrawable(R.drawable.ic_lock_outline_black))
                    }
                    setOnClickListener { onStoryCardClicked?.invoke(viewModel) }
                }
            }
        }
    }

    companion object {

        private const val DISTANCE_TO_UNLOCK_STORY = 10
    }
}