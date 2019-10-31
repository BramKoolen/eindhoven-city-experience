package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_tour_card.view.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState

class TourListAdapter : RecyclerView.Adapter<TourListAdapter.TourViewHolder>() {

    var tourViewModels: List<TourViewModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onTourClicked: ((TourViewModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        return TourViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_tour_card,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        holder.bind(tourViewModels[position])
    }

    override fun getItemCount(): Int {
        return tourViewModels.size
    }

    inner class TourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(viewModel: TourViewModel) {
            itemView.run {
                tourCardTitle.text = viewModel.title
                tourCardDescription.text = viewModel.smallDescription

                if (viewModel.state == TourState.STARTED || viewModel.state == TourState.STOPPED) {
                    tourCardProgressLabel.text = "test"
                    tourCardProgressLabel.visibility = View.VISIBLE
                }

                if (viewModel.state == TourState.STARTED) {
                    tourCardStartedImage.visibility = View.VISIBLE
                }

                Glide
                    .with(this)
                    .load(viewModel.thumbnailUrl)
                    .centerCrop()
                    .into(tourCardBackgroundImage)

                setOnClickListener { onTourClicked?.invoke(viewModel) }
            }
        }
    }
}