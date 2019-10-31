package tech.bkdevelopment.eindhovencityexperience.presentation.tour.tourlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity;
import tech.bkdevelopment.eindhovencityexperience.R
import javax.inject.Inject

class TourListActivity : DaggerAppCompatActivity(), TourListContract.View {

    @Inject
    lateinit var presenter: TourListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_list)
    }

    override fun onStart() {
        super.onStart()
        presenter.startPresenting()
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
    }

    override fun showLoadingPlaceHolder() {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoadingPlaceHolder() {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorStateList() {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideErrorStateList() {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTours(tourViewModels: List<TourViewModel>) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TourListActivity::class.java)
        }
    }
}