package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import com.bumptech.glide.Glide
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_tour_detail.*
import kotlinx.android.synthetic.main.view_tour_detail.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import javax.inject.Inject

class TourDetailActivity : DaggerAppCompatActivity(), TourDetailContract.View {

    @Inject
    lateinit var presenter: TourDetailContract.Presenter

    override val tour: TourViewModel?
        get() = intent.getParcelableExtra(TOUR_EXTRA) as TourViewModel

    private val tourDetailCafeAdapter by lazy { TourDetailCafeAdapter() }

    private var isCafesShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_detail)

        setSupportActionBar(tourDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setTourDetailText()

        tourDetailFloatingButton.setOnClickListener { presenter.onStartStopTourButtonClicked() }
    }

    private fun setTourDetailText() {
        Glide
            .with(this)
            .load(tour?.thumbnailUrl)
            .centerCrop()
            .into(tourDetailHeaderImage)
        Glide
            .with(this)
            .load(tour?.iconUrl)
            .centerCrop()
            .into(tourDetailHeaderTourIcon)
        TourDetailHeaderTourTitle.text = tour?.title

        tourDetailDetailCardDistanceText.text = getString(
            R.string.tour_detail_detail_card_distance,
            tour?.remainingDistance.toString(),
            "Meters"
        )

        tourDetailDetailCardTimeText.text = getString(
            R.string.tour_detail_detail_card_time,
            tour?.remainingTourTimeInMinutes.toString()
        )
        tourDetailDetailCardStoryText.text =
            getString(R.string.tour_detail_detail_stories, tour?.stories?.size.toString())

        if (!tour?.extraTourItemLabel.isNullOrEmpty()) {
            Glide
                .with(this)
                .load(tour?.extraTourItemIconUrl)
                .centerCrop()
                .into(tourDetailDetailCardCustomIcon)
            tourDetailDetailCardCustomText.text = tour?.extraTourItemLabel
        } else {
            tourDetailDetailCardCustomText.visibility = View.GONE
            tourDetailDetailCardCustomIcon.visibility = View.GONE
        }
        tourDetailDetailCardCafesText.text = getString(
            R.string.tour_detail_detail_card_cafes,
            tour?.cafesOnThisRoute?.size.toString()
        )

        tourDetailAddressCardStartAddressLocationText.text = tour?.startAddress?.name
        tourDetailAddressCardParkingLocationText.text = tour?.parkingAddress?.name
        tourDetailDescriptionCardText.text = tour?.longDescription

        if (tour?.cafesOnThisRoute?.size != 0) {
            tour?.cafesOnThisRoute?.let {
                tourDetailDetailCardCafesRecycler.adapter = tourDetailCafeAdapter
                tourDetailCafeAdapter.address = it
            }

            tourDetailDetailCardCafesArrow.setOnClickListener { onCafesArrowClicked() }
            tourDetailDetailCardCafesShowOnMapButton.setOnClickListener { presenter.onMapButtonClicked() }
        } else {
            tourDetailDetailCardCafesArrow.visibility = View.GONE
        }
    }

    private fun onCafesArrowClicked() {
        if (isCafesShown) {
            tourDetailDetailCardCafesRecycler.visibility = View.GONE
            tourDetailDetailCardCafesShowOnMapButton.visibility = View.GONE
            tourDetailDetailCardCafesArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_black)
            isCafesShown = false
        } else {
            tourDetailDetailCardCafesRecycler.visibility = View.VISIBLE
            tourDetailDetailCardCafesShowOnMapButton.visibility = View.VISIBLE
            tourDetailDetailCardCafesArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_black)
            isCafesShown = true
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.startPresenting()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tour_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_map -> presenter.onMapButtonClicked()
        }
        return true
    }

    override fun showTourState(started: Boolean) {
        if (started) {
            tourDetailFloatingButton.setImageResource(R.drawable.ic_stop_black)
        } else {
            tourDetailFloatingButton.setImageResource(R.drawable.ic_play_arrow_black)
        }
    }

    override fun showLocationPermissionDeniedExplanationDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_default)
        val title = dialog.findViewById(R.id.dialogTitle) as TextView
        val body = dialog.findViewById(R.id.dialogBody) as TextView
        val buttonYes = dialog.findViewById(R.id.dialogYes) as TextView
        val buttonNo = dialog.findViewById(R.id.dialogNo) as TextView
        title.text = getString(R.string.dialog_default_title)
        body.text = getString(R.string.tour_detail_start_tour_error_dialog_body)
        buttonYes.text = getString(R.string.tour_detail_permission_denied_dialog_settings)
        buttonNo.text = getString(R.string.dialog_default_close)
        buttonYes.setOnClickListener { presenter.onDialogSettingsButtonClicked() }
        buttonNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun showCantStartTourDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_default)
        val title = dialog.findViewById(R.id.dialogTitle) as TextView
        val body = dialog.findViewById(R.id.dialogBody) as TextView
        val buttonNo = dialog.findViewById(R.id.dialogNo) as TextView
        title.text = getString(R.string.dialog_default_title)
        body.text = getString(R.string.tour_detail_start_tour_error_dialog_body)
        buttonNo.text = getString(R.string.dialog_default_close)
        buttonNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun showStopTourDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_default)
        val title = dialog.findViewById(R.id.dialogTitle) as TextView
        val body = dialog.findViewById(R.id.dialogBody) as TextView
        val buttonYes = dialog.findViewById(R.id.dialogYes) as TextView
        val buttonNo = dialog.findViewById(R.id.dialogNo) as TextView
        title.text = getString(R.string.tour_detail_stop_tour_dialog_title)
        body.text = getString(R.string.tour_detail_stop_tour_dialog_body)
        buttonYes.text = getString(R.string.tour_detail_stop_tour_dialog_continue)
        buttonNo.text = getString(R.string.dialog_default_close)
        buttonYes.setOnClickListener { dialog.dismiss() }
        buttonNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
    }

    companion object {

        private const val TOUR_EXTRA = "intentTourExtra"

        fun createIntent(context: Context, tour: TourViewModel): Intent {
            return Intent(context, TourDetailActivity::class.java).apply {
                putExtra(TOUR_EXTRA, tour)
            }
        }

        fun createSettingsIntent(context: Context): Intent {
            return Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.packageName))
                .addCategory(Intent.CATEGORY_DEFAULT)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}