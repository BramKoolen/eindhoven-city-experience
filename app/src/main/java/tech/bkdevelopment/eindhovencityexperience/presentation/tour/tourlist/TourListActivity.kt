package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_tour_list.*
import kotlinx.android.synthetic.main.view_internet_error_state.*
import tech.bkdevelopment.eindhovencityexperience.R
import tech.bkdevelopment.eindhovencityexperience.presentation.tour.TourViewModel
import javax.inject.Inject

class TourListActivity : DaggerAppCompatActivity(), TourListContract.View {

    @Inject
    lateinit var presenter: TourListContract.Presenter

    private val tourListAdapter by lazy {
        TourListAdapter().apply {
            onTourClicked = presenter::onTourClicked
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_list)
        initToolbar()

        tourListTourCardRecyclerView.adapter = tourListAdapter
        internetErrorStateTryAgainButton.setOnClickListener { presenter.onErrorStateButtonClicked() }
    }

    override fun onResume() {
        super.onResume()
        presenter.startPresenting()
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_tour_list, menu)
        return true
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_more) {
            presenter.onMoreButtonClicked()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoadingIndicator() {
        tourListLoadingIndicator.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        tourListLoadingIndicator.visibility = View.GONE
    }

    override fun showErrorStateList() {
        tourListView.visibility = View.GONE
        internetErrorStateView.visibility = View.VISIBLE
    }

    override fun hideErrorStateList() {
        internetErrorStateView.visibility = View.GONE
    }

    override fun showTours(tourViewModels: List<TourViewModel>) {
        tourListView.visibility = View.VISIBLE
        tourListAdapter.tourViewModels = tourViewModels
    }

    override fun showCantStartTwoToursError() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_default)
        val title = dialog.findViewById(R.id.dialogTitle) as TextView
        val body = dialog.findViewById(R.id.dialogBody) as TextView
        val buttonNo = dialog.findViewById(R.id.dialogNo) as TextView
        title.text = getString(R.string.dialog_default_title)
        body.text = getString(R.string.tour_list_dialog_body)
        buttonNo.text = getString(R.string.dialog_default_close)
        buttonNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TourListActivity::class.java)
        }
    }
}