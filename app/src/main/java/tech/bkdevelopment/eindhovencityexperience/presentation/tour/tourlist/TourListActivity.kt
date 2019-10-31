package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_tour_list.*
import kotlinx.android.synthetic.main.view_internet_error_state.*
import tech.bkdevelopment.eindhovencityexperience.R
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

    override fun onStart() {
        super.onStart()
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

    override fun showLoadingPlaceHolder() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoadingPlaceHolder() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TourListActivity::class.java)
        }
    }
}