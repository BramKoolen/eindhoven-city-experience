package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_tour_detail_extented.view.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel

class TourDetailStoryAdapter : RecyclerView.Adapter<TourDetailStoryAdapter.CafeViewHolder>() {

    var story: List<StoryViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        return CafeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_tour_detail_extented,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.bind(story[position])
    }

    override fun getItemCount(): Int {
        return story.size
    }

    inner class CafeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(viewModel: StoryViewModel) {
            itemView.run {
                tourDetailCardExtendedTitle.text = viewModel.title
            }
        }
    }
}