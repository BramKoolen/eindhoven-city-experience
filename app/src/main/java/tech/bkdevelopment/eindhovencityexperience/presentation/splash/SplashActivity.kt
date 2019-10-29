package tech.bkdevelopment.eindhovencityexperience.presentation.splash

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import tech.bkdevelopment.eindhovencityexperience.R
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var presenter: SplashContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        presenter.startPresenting()
    }

    override fun onStop() {
        presenter.stopPresenting()
        presenter.close()
        super.onStop()
    }
}