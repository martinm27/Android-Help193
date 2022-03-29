package hr.fer.help193.vehicle.app.features.onboarding.ui

import hr.fer.help193.vehicle.app.base.BaseView
import hr.fer.help193.vehicle.app.base.ViewPresenter

interface OnboardingContract {

    interface View : BaseView

    interface Presenter: ViewPresenter<View, OnboardingViewState> {

        fun startApp()
    }
}
