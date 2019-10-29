package tech.bkdevelopment.eindhovencityexperience.presentation.splash

import android.os.CountDownTimer
import javax.inject.Inject

class SplashPresenter @Inject constructor(
    private val navigator: SplashContract.Navigator
) : SplashContract.Presenter {

    var timer: CountDownTimer? = null

    override fun startPresenting() {
        countDownTimer()
        (timer as CountDownTimer).start()
    }

    override fun stopPresenting() {
        (timer as CountDownTimer).cancel()
    }

    override fun close() {
        navigator.close()
    }

    private fun countDownTimer() {

        timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // no opt
            }

            override fun onFinish() {
                navigator.navigateToTours()
            }
        }
    }
}