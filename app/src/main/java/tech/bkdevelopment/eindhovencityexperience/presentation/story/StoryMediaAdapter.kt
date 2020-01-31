package tech.bkdevelopment.eindhovencityexperience.presentation.story

import android.view.View
import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter
import tech.bkdevelopment.eindhovencityexperience.presentation.media.MediaViewModel
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_story_media.view.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.MediaType


class StoryMediaAdapter : SliderViewAdapter<StoryMediaAdapter.MediaViewHolder>() {

    var mediaViewModels: List<MediaViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onMediaItemClicked: ((MediaViewModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup): MediaViewHolder {
        return MediaViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_story_media,
                parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(mediaViewModels[position])
    }

    override fun getCount(): Int {
       return mediaViewModels.size
    }

    inner class MediaViewHolder(private val itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

        fun bind(viewModel: MediaViewModel) {
            itemView.run {
                Glide
                    .with(this)
                    .load(viewModel.thumbnailUrl)
                    .centerCrop()
                    .into(storyMediaImageContainer)

                when(viewModel.type){
                    MediaType.AUDIO -> storyMediaIcon.setImageResource(R.drawable.ic_header_icon_audio)
                    MediaType.VIDEO -> storyMediaIcon.setImageResource(R.drawable.ic_header_icon_video)
                    else -> null
                }
            }
        }
    }
}