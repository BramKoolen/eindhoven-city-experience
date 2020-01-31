package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.list_item_tour_card.view.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TextColor
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
                    .skipMemoryCache(true)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Timber.e(e)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
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
                tourCardProgressLabel.text = createProgressString(context,tourViewModel)
                tourCardProgressLabel.visibility = View.VISIBLE
                when(tourViewModel.textColorProgress){
                    TextColor.BLACK -> tourCardProgressLabel.setTextColor(ContextCompat.getColor(context,R.color.black))
                    TextColor.WHITE -> tourCardProgressLabel.setTextColor(ContextCompat.getColor(context,R.color.white))
                }

            }

            if (tourViewModel.state == TourState.STARTED) {
                tourCardStartedImage.visibility = View.VISIBLE
            }else{
                tourCardStartedImage.visibility = View.GONE
            }
        }

        private fun createProgressString(context: Context, tourViewModel: TourViewModel): String {
            val unfinishedStories = context.getString(R.string.tour_list_progress_stories,tourViewModel.stories.filter { !it.completed }.size.toString())
            val remainingDistance = context.getString(R.string.tour_list_progress_distance, tourViewModel.remainingDistanceInMeters.toString())
            val remainingTime = context.getString(R.string.tour_list_progress_time,tourViewModel.remainingTourTimeInMinutes.toString())
            return "$unfinishedStories - $remainingDistance - $remainingTime"
        }

    }
}