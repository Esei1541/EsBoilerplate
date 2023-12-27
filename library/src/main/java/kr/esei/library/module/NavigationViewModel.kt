package kr.esei.library.module

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import kotlinx.coroutines.CoroutineDispatcher
import kr.esei.library.constant.LiveEvent
import kr.esei.library.constant.MutableLiveEvent
import kr.esei.library.model.NavEventModel
import kr.esei.library.util.Event

/**
 * An abstract class that adds features optimized for the Navigation Component, based on BaseViewModel.
 * @param dispatcher If the Coroutine Dispatcher is not set, this Dispatcher is used. Can be accessed through the scope variable in the child class. In the test environment, this variable is used to inject TestDispatcher instead of the default Dispatcher. If the value is null, it is set to viewModelScope, so it is recommended to implement it in the form of dispatcher: CoroutineDispatcher? = null in non-test environments.
 */
public abstract class NavigationViewModel(dispatcher: CoroutineDispatcher? = null): BaseViewModel(dispatcher) {

    private val _navigate: MutableLiveEvent<NavEventModel> = MutableLiveData()
    public val navigate : LiveEvent<NavEventModel> get() = _navigate

    private val _backStack: MutableLiveEvent<Unit> = MutableLiveData()
    public val backStack: LiveEvent<Unit> get() = _backStack

    protected var arguments: Bundle? = null

    /**
     * View에서 NavController의 Fragment를 전환하도록 하기 위한 Event를 호출한다.
     */
    public fun handleNavigateEvent(@IdRes destinationId: Int, bundle: Bundle? = null, navOptions: NavOptions? = null) {
        _navigate.value = Event(NavEventModel(destinationId, bundle, navOptions))
    }

    public fun handleNavigateEvent(@IdRes destinationId: Int, navOptions: NavOptions? = null) {
        _navigate.value = Event(NavEventModel(destinationId, null, navOptions))
    }

    public fun handleNavigateEvent(@IdRes destinationId: Int, bundle: Bundle? = null) {
        _navigate.value = Event(NavEventModel(destinationId, bundle, null))
    }

    public fun handleNavigateEvent(@IdRes destinationId: Int) {
        _navigate.value = Event(NavEventModel(destinationId, null, null))
    }

    public fun handleBackStackEvent() {
        _backStack.value = Event(Unit)
    }

    public fun initFragmentArguments(arguments: Bundle?) {
        this.arguments = arguments
    }

}