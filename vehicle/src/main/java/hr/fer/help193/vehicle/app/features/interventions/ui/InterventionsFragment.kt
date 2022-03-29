package hr.fer.help193.vehicle.app.features.interventions.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import hr.fer.help193.vehicle.R
import hr.fer.help193.vehicle.app.base.BaseFragment
import hr.fer.help193.vehicle.app.features.interventions.di.INTERVENTIONS_SCOPE
import hr.fer.help193.vehicle.utils.ALPHA_HALF
import hr.fer.help193.vehicle.utils.ALPHA_OPAQUE
import hr.fer.help193.vehicle.app.view.VerticalItemDecoration
import kotlinx.android.synthetic.main.fragment_interventions.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class InterventionsFragment : BaseFragment<InterventionsViewState>(), InterventionsContract.View {

    companion object {
        const val LAYOUT = R.layout.fragment_interventions
        const val TAG: String = "InterventionsFragment"

        fun newInstance(): Fragment = InterventionsFragment()
    }

    private val presenter: InterventionsContract.Presenter by scopedInject()
    private val interventionsAdapter: InterventionsAdapter by inject(parameters = { parametersOf(layoutInflater, presenter::onSelectedChanged) })

    override fun getLayoutResource(): Int = LAYOUT
    override fun getScopeName(): String = INTERVENTIONS_SCOPE
    override fun getViewPresenter() = presenter

    override fun initialiseView(view: View, savedInstanceState: Bundle?) {
        interventions_back.setOnClickListener { presenter.exit() }
        interventions_confirm.setOnClickListener { presenter.chooseIntervention() }

        with(interventions_recyclerView) {
            layoutManager = get<GridLayoutManager>(parameters = { parametersOf(context!!) })
            context?.let {
                addItemDecoration(get<VerticalItemDecoration>(parameters = { parametersOf(it, resources.getDimension(R.dimen.common_section_element_padding)) }))
            }
            adapter = interventionsAdapter
            (itemAnimator as SimpleItemAnimator).run { supportsChangeAnimations = false }
        }
    }

    override fun render(viewState: InterventionsViewState) {
        interventions_confirm.alpha = viewState.interventions.find {
            if (it is InterventionViewModel) {
                it.isSelected
            } else {
                false
            }
        }?.let {
            ALPHA_OPAQUE
        } ?: ALPHA_HALF
        interventionsAdapter.submitList(viewState.interventions)
    }
}
