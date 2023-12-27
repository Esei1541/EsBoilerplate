package kr.esei.library.module

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import kr.esei.library.constant.observeEvent

/**
 * An abstract class that adds features optimized for the Navigation Component, based on BaseActivity.
 * @param B DataBinding Class of the Activity
 * @param VM ViewModel Class. Use a ViewModel Class that implements NavigationViewModel.
 * @param layoutResId Resource ID of the layout XML
 * @param navContainer ID of the FragmentContainerView where NavHostFragment will be inserted
 * @see NavigationViewModel
 */
public abstract class NavigationActivity<B: ViewDataBinding, VM: NavigationViewModel>(layoutResId: Int, @IdRes private val navContainer: Int): BaseActivity<B, VM>(layoutResId) {

    // NavHostFragment를 통해 Fragment를 관리하기 위한 변수
    protected val navHostFragment: NavHostFragment by lazy { supportFragmentManager.findFragmentById(navContainer) as NavHostFragment }
    protected val navController: NavController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController.addOnDestinationChangedListener(::onDestinationChanged)
    }

    public open fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {}

    override fun observeEvent() {
        super.observeEvent()
        activityViewModel.apply {
            navigate.observeEvent(this@NavigationActivity) { event ->
                navController.navigate(event.destinationId, event.bundle, event.navOptions)
            }
            backStack.observeEvent(this@NavigationActivity) {
                navController.popBackStack()
            }
        }
    }

}