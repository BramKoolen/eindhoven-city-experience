package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_tour_detail_extented.view.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.AddressViewModel

class TourDetailCafeAdapter : RecyclerView.Adapter<TourDetailCafeAdapter.CafeViewHolder>() {

    var address: List<AddressViewModel> = emptyList()
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
        holder.bind(address[position])
    }

    override fun getItemCount(): Int {
        return address.size
    }

    inner class CafeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(viewModel: AddressViewModel) {
            itemView.run {
                tourDetailCardExtendedTitle.text = viewModel.name
            }
        }
    }
}