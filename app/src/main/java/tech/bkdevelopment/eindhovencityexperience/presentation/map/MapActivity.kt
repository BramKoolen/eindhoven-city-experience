package tech.bkdevelopment.eindhovencityexperience.presentation.map

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_map.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.domain.location.model.Location
import tech.bkdevelopment.eindhovencityexperience.domain.story.model.MediaType
import tech.bkdevelopment.eindhovencityexperience.generic.extension.getBitmapFromVectorDrawable
import tech.bkdevelopment.eindhovencityexperience.generic.extension.getBitmapFromVectorDrawableResize
import tech.bkdevelopment.eindhovencityexperience.generic.view.recyclerview.OnSnapPositionChangeListener
import tech.bkdevelopment.eindhovencityexperience.generic.view.recyclerview.SnapOnScrollListener
import tech.bkdevelopment.eindhovencityexperience.presentation.story.StoryViewModel
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.AddressViewModel
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import timber.log.Timber
import javax.inject.Inject

class MapActivity : DaggerAppCompatActivity(), OnMapReadyCallback, MapContract.View,
    OnSnapPositionChangeListener {

    @Inject
    lateinit var presenter: MapContract.Presenter

    override val tourId: String?
        get() = intent.getStringExtra(TOUR_ID_EXTRA)

    override var tour: TourViewModel? = null
        set(value) {
            field = value
            if (markers.size == 0) {
                setupMarkers()
            }
            updateStories()
        }

    private val storyCardAdapter by lazy {
        MapStoryCardAdapter().apply {
            onStoryCardClicked = presenter::onStoryClicked
        }
    }

    private var map: GoogleMap? = null
    private var markers = mutableListOf<Marker>()
    private var selectedMarkerPosition: Int = 0
    private var recyclerViewState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        setupStoryCards()
        mapFloatingBackButton.setOnClickListener { onBackPressed() }
        mapFloatingCurrentLocationButton.setOnClickListener { presenter.onCurrentLocationButtonClicked() }
    }

    private fun setupStoryCards() {
        mapStoryCardsRecycler.adapter = storyCardAdapter

        val snapOnScrollListener = SnapOnScrollListener(
            LinearSnapHelper(),
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL_STATE_IDLE,
            this
        )
        mapStoryCardsRecycler.addOnScrollListener(snapOnScrollListener)
    }

    override fun onResume() {
        super.onResume()
        presenter.startPresenting()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.uiSettings?.isMyLocationButtonEnabled = false
        map?.uiSettings?.isCompassEnabled = false
        try {
            map?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.maps_style
                )
            )
        } catch (exception: Resources.NotFoundException) {
            Timber.e(exception)
        }
    }

    private fun setupMarkers() {
        tour?.stories?.map { addStoryMarker(it) }
        tour?.parkingAddress?.let { addParkingMarker(it) }
        tour?.cafesOnThisRoute?.map { addCafeMarker(it) }
    }


    private fun addStoryMarker(story: StoryViewModel) {
        val markerIcon = getStoryMarkerIcon(story)
        val marker: Marker = map!!.addMarker(
            MarkerOptions()
                .position(LatLng(story.lat, story.long))

                .icon(BitmapDescriptorFactory.fromBitmap(markerIcon?.let {
                    this.getBitmapFromVectorDrawable(it)
                }))
        )

        markers.add(marker)
    }

    private fun addParkingMarker(address: AddressViewModel) {
        map!!.addMarker(
            MarkerOptions()
                .position(LatLng(address.lat, address.long))

                .icon(BitmapDescriptorFactory.fromBitmap(this.getDrawable(R.drawable.ic_pin_car).let {
                    it?.let { it1 ->
                        this.getBitmapFromVectorDrawable(it1)
                    }
                }))
        )
    }

    private fun addCafeMarker(address: AddressViewModel) {
        map!!.addMarker(
            MarkerOptions()
                .position(LatLng(address.lat, address.long))

                .icon(BitmapDescriptorFactory.fromBitmap(this.getDrawable(R.drawable.ic_pin_cafe).let {
                    it?.let { it1 ->
                        this.getBitmapFromVectorDrawable(it1)
                    }
                }))
        )
    }

    private fun getStoryMarkerIcon(story: StoryViewModel): Drawable? {
        val markerIcon: Drawable?
        if (story.completed) {
            markerIcon = when (story.storyType) {
                MediaType.TEXT -> this.getDrawable(R.drawable.ic_pin_description_disabled)
                MediaType.PHOTO -> this.getDrawable(R.drawable.ic_pin_photo_disabled)
                MediaType.AUDIO -> this.getDrawable(R.drawable.ic_pin_audio_disabled)
                MediaType.VIDEO -> this.getDrawable(R.drawable.ic_pin_play_disabled)
                MediaType.VR -> this.getDrawable(R.drawable.ic_pin_description_disabled)
            }
        } else {
            markerIcon = when (story.storyType) {
                MediaType.TEXT -> this.getDrawable(R.drawable.ic_pin_description_locked)
                MediaType.PHOTO -> this.getDrawable(R.drawable.ic_pin_photo_locked)
                MediaType.AUDIO -> this.getDrawable(R.drawable.ic_pin_audio_locked)
                MediaType.VIDEO -> this.getDrawable(R.drawable.ic_pin_play_locked)
                MediaType.VR -> this.getDrawable(R.drawable.ic_pin_description_locked)
            }
        }
        return markerIcon
    }

    override fun updateStories() {
        storyCardAdapter.stories = tour?.stories ?: emptyList()
        mapStoryCardsRecycler.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    override fun updateSelectedMarker(position: Int) {
        if (map != null && markers.size != 0) {
            markers[position].setIcon(BitmapDescriptorFactory.fromBitmap(tour?.stories?.get(position)?.let {
                getStoryMarkerIcon(it)?.let {
                    this.getBitmapFromVectorDrawableResize(it, 150, 164)
                }
            }))


            markers[selectedMarkerPosition].setIcon(
                BitmapDescriptorFactory.fromBitmap(
                    tour?.stories?.get(position)?.let {
                        getStoryMarkerIcon(it)?.let {
                            this.getBitmapFromVectorDrawableResize(it, 64, 78)
                        }
                    })
            )

            selectedMarkerPosition = position
        }
    }

    override fun zoomToLocation(location: Location) {
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(location.lat, location.long), 13f)
        )

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(location.lat, location.long))
            .zoom(15f)
            .tilt(40f)
            .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun showNotCloseEnoughToStoryDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_default)
        val title = dialog.findViewById(R.id.dialogTitle) as TextView
        val body = dialog.findViewById(R.id.dialogBody) as TextView
        val buttonNo = dialog.findViewById(R.id.dialogNo) as TextView
        title.text = getString(R.string.dialog_default_title)
        body.text = getString(R.string.map_not_close_enough_to_story_dialog_body)
        buttonNo.text = getString(R.string.dialog_default_close)
        buttonNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun showTourFinishedDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_default)
        val title = dialog.findViewById(R.id.dialogTitle) as TextView
        val body = dialog.findViewById(R.id.dialogBody) as TextView
        val buttonNo = dialog.findViewById(R.id.dialogNo) as TextView
        title.text = getString(R.string.map_tour_finished_dialog_title)
        body.text = getString(R.string.map_tour_finished_dialog_body)
        buttonNo.text = getString(R.string.dialog_default_close)
        buttonNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun showStartTourDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_default)
        val title = dialog.findViewById(R.id.dialogTitle) as TextView
        val body = dialog.findViewById(R.id.dialogBody) as TextView
        val buttonNo = dialog.findViewById(R.id.dialogNo) as TextView
        val buttonYes = dialog.findViewById(R.id.dialogYes) as TextView
        title.text = getString(R.string.map_start_tour_title)
        body.text = getString(R.string.map_start_tour_body)
        buttonNo.text = getString(R.string.dialog_default_close)
        buttonNo.setOnClickListener { dialog.dismiss() }
        buttonYes.text = getString(R.string.map_start_tour_starten_button)
        buttonYes.setOnClickListener { onStartTourDialogClicked(dialog) }
        dialog.show()
    }

    private fun onStartTourDialogClicked(dialog: Dialog) {
        dialog.dismiss()
        presenter.onStartTourClicked()
    }

    override fun onSnapPositionChange(position: Int) {
        recyclerViewState = mapStoryCardsRecycler.layoutManager?.onSaveInstanceState()
        presenter.onStoryCardFocusChanged(position)

    }

    override fun checkLocationPermissions() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    map?.isMyLocationEnabled = true
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

    override fun updateCurrentLocationButton(visible: Boolean) {
        if (visible) {
            mapFloatingCurrentLocationButton.visibility = View.VISIBLE
        } else {
            mapFloatingBackButton.visibility = View.GONE
        }
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
    }

    companion object {

        private const val TOUR_ID_EXTRA = "intentTourIdExtra"

        fun createIntent(context: Context, tourId: String): Intent {
            return Intent(context, MapActivity::class.java).apply {
                putExtra(TOUR_ID_EXTRA, tourId)
            }
        }
    }
}