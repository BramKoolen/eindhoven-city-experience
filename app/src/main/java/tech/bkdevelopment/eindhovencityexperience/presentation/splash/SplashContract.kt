package tech.bkdevelopment.eindhovencityexperience.presentation.splash

interface SplashContract {

    interface Presenter {

        fun startPresenting()
        fun stopPresenting()
        fun close()
    }

    interface Navigator {

        fun navigateToTours()
        fun close()
    }
}