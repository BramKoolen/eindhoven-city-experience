package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourdetail

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_tour_detail.*
import kotlinx.android.synthetic.main.view_tour_detail.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.domain.tour.model.TourState
import tech.bkdevelopment.eindhovencityexperience.generic.view.collapsingtoolbar.AppbarChangeListener
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import javax.inject.Inject

class TourDetailActivity : DaggerAppCompatActivity(), TourDetailContract.View {

    @Inject
    lateinit var presenter: TourDetailContract.Presenter

    override val tourId: String?
        get() = intent.getStringExtra(TOUR_ID_EXTRA)

    override var tour: TourViewModel? = null
        set(value) {
            field = value
            fillTourDetailLayout()
        }

    private val tourDetailCafeAdapter by lazy { TourDetailCafeAdapter() }

    private var isCafesShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_detail)

        setSupportActionBar(tourDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupAppbarLayout()
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra(LAUNCH_FROM_NOTIFICATION_EXTRA, false)) {
            presenter.onBackPressedLaunchedFromNotification()
        } else {
            super.onBackPressed()
        }
    }

    private fun fillTourDetailLayout() {
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
            tour?.remainingDistanceInMeters.toString()
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

        tour?.state?.let { showTourState(it) }
        tourDetailFloatingButton.visibility = View.VISIBLE
        tourDetailFloatingButton.setOnClickListener { presenter.onStartStopTourButtonClicked() }
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

    override fun onResume() {
        super.onResume()
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

    private fun setupAppbarLayout() {
        tourDetailAppBar.addOnOffsetChangedListener(object : AppbarChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                when(state){
                    State.EXPANDED -> appbarLayoutExpanded()
                    State.COLLAPSED -> appbarLayoutCollapsed()
                    State.IDLE -> appbarLayoutIDLE()
                    else -> appbarLayoutExpanded()
                }
            }
        })
    }

    private fun appbarLayoutExpanded(){
        tourDetailCollapsingToolbar?.title = ""
    }

    private fun appbarLayoutCollapsed(){
        tourDetailCollapsingToolbar?.title = tour?.title
    }

    private fun appbarLayoutIDLE(){
        tourDetailCollapsingToolbar?.title = ""
    }

    override fun showTourState(tourState: TourState) {
        if (tourState == TourState.STARTED) {
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
        buttonNo.text = getString(R.string.tour_detail_stop_tour_dialog_stop)
        buttonYes.setOnClickListener { dialog.dismiss() }
        buttonNo.setOnClickListener { onDialogStopTourButtonClicked(dialog) }
        dialog.show()
    }



    private fun onDialogStopTourButtonClicked(dialog: Dialog) {
        dialog.dismiss()
        presenter.onDialogStopTourButtonClicked()
    }

    override fun checkLocationPermissions() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    presenter.onLocationPermissionGranted()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    presenter.onLocationPermissionDenied()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
    }

    companion object {

        private const val TOUR_ID_EXTRA = "intentTourIdExtra"
        private const val LAUNCH_FROM_NOTIFICATION_EXTRA = "intentLaunchFromNotificationExtra"

        fun createIntent(
            context: Context,
            tourId: String,
            launchFromNotification: Boolean
        ): Intent {
            return Intent(context, TourDetailActivity::class.java).apply {
                putExtra(TOUR_ID_EXTRA, tourId)
                putExtra(LAUNCH_FROM_NOTIFICATION_EXTRA, launchFromNotification)
            }
        }
    }
}