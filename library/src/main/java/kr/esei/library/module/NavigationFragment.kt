package kr.esei.library.module

import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import kr.esei.library.constant.observeEvent

/**
 * An abstract class that adds features optimized for the Navigation Component, based on BaseFragment.
 * @param B DataBinding Class of the Activity
 * @param VM ViewModel Class. Use a ViewModel Class that implements NavigationViewModel.
 * @param layoutResId Resource ID of the layout XML
 * @see NavigationViewModel
 */
public abstract class NavigationFragment<B: ViewDataBinding, VM: NavigationViewModel>(private val layoutResId: Int): BaseFragment<B, VM>(layoutResId){

    override fun observeEvent() {
        super.observeEvent()
        fragmentViewModel.apply {
            navigate.observeEvent(viewLifecycleOwner) { event ->
                findNavController().navigate(event.destinationId, event.bundle, event.navOptions)
            }
            backStack.observeEvent(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
        }
    }

}