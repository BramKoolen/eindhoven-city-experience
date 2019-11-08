package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.list_item_tour_card.view.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import timber.log.Timber

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
                Glide
                    .with(this)
                    .load(viewModel.thumbnailUrl)
                    .centerCrop()
                    .error(R.drawable.eindhoven_error)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            Timber.e(e)
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                        ): Boolean {
                            setTourItems(viewModel)
                            return false
                        }
                    })
                    .into(tourCardBackgroundImage)

                setOnClickListener { onTourClicked?.invoke(viewModel) }
            }
        }

        private fun View.setTourItems(tourViewModel: TourViewModel) {
            tourCardImageLoadingAnimation.visibility = View.GONE
            tourCardTitle.text = tourViewModel.title
            tourCardDescription.text = tourViewModel.smallDescription

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tourCardBackgroundImage.foreground =
                    context.getDrawable(R.drawable.bg_gradient_black)
            }

            if (tourViewModel.state == TourState.STARTED || tourViewModel.state == TourState.STOPPED) {
                tourCardProgressLabel.text = "todo get data"
                tourCardProgressLabel.visibility = View.VISIBLE
            }

            if (tourViewModel.state == TourState.STARTED) {
                tourCardStartedImage.visibility = View.VISIBLE
            }
        }
    }
}