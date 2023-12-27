package kr.esei.library.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kr.esei.library.constant.LiveEvent
import kr.esei.library.constant.MutableLiveEvent
import kr.esei.library.model.ActivityBundleEventModel
import kr.esei.library.model.ActivityResultEventModel
import kr.esei.library.model.EventDataBundle
import kr.esei.library.util.Event

/**
 * Defines the commonalities of ViewModel classes.
 * Inherit this class instead of ViewModel Class.
 * @param dispatcher If the Coroutine Dispatcher is not set, this Dispatcher is used. Can be accessed through the scope variable in the child class. In the test environment, this variable is used to inject TestDispatcher instead of the default Dispatcher. If the value is null, it is set to viewModelScope, so it is recommended to implement it in the form of dispatcher: CoroutineDispatcher? = null in non-test environments.
 */
public abstract class BaseViewModel(dispatcher: CoroutineDispatcher? = null) : ViewModel() {

    protected open val TAG: String = javaClass.simpleName

    protected val scope: CoroutineScope = if (dispatcher == null) viewModelScope else CoroutineScope(dispatcher)

    /**
     * Registers the callback to be used by ActivityResultLauncher.
     * If you inherit this Map and define a callback, the ActivityResultLauncher is initialized in the onCreate (onViewCreated) lifecycle of the BaseActivity/BaseFragment Class connected to this ViewModel.
     * The registered ActivityResultLauncher can be called through the launcherKey of handleLauncherEvent.
     * @see BaseActivity.activityResultLaunchers
     * @see BaseFragment.activityResultLaunchers
     */
    public open val activityResultCallbacks: Map<String, (ActivityResult) -> Unit> = mapOf()

    private val _toast: MutableLiveEvent<String> = MutableLiveData()
    public val toast: LiveEvent<String> get() = _toast

    private val _toastRes: MutableLiveEvent<Int> = MutableLiveData()
    public val toastRes: LiveEvent<Int> get() = _toastRes

    private val _finish: MutableLiveEvent<Unit> = MutableLiveData()
    public val finish: LiveEvent<Unit> get() = _finish

    private val _removeTask: MutableLiveEvent<Unit> = MutableLiveData()
    public val removeTask: LiveEvent<Unit> get() = _removeTask

    private val _launcher: MutableLiveEvent<Pair<Intent, String>> = MutableLiveData()
    public val launcher: LiveEvent<Pair<Intent, String>> get() = _launcher

    private val _startActivity: MutableLiveEvent<Class<*>> = MutableLiveData()
    public val startActivity: LiveEvent<Class<*>> get() = _startActivity

    private val _startActivityWithBundle: MutableLiveEvent<ActivityBundleEventModel> = MutableLiveData()
    public val startActivityWithBundle: LiveEvent<ActivityBundleEventModel> get() = _startActivityWithBundle

    private val _finishWithResult: MutableLiveEvent<ActivityResultEventModel> = MutableLiveData()
    public val finishWithResult: LiveEvent<ActivityResultEventModel> get() = _finishWithResult

    /**
     * Stores data received by an Activity launched with startActivityWithBundle from the previous Activity.
     * Initialized when the initBundle() function is called in the onCreate() of BaseActivity.
     * @see initBundle
     */
    protected var bundle: EventDataBundle? = null
        private set

    /**
     * Calls the Event to display Toast in the View.
     * @param message Message to display
     */
    public fun handleToastEvent(message: String) {
        _toast.value = Event(message)
    }

    /**
     * Calls the Event to display Toast in the View.
     * @param res Resource ID of the message to display
     */
    public fun handleToastEvent(@StringRes res: Int) {
        _toastRes.value = Event(res)
    }

    /**
     * Calls the Event to finish the Activity.
     */
    public fun handleFinishEvent() {
        _finish.value = Event(Unit)
    }

    /**
     * Calls the Event to finish the Activity.
     * @param resultCode Activity Result Code
     * @param data Bundle of data to be sent to the callback. To retrieve data in the callback, access it using activityResult.data?.getBundleExtra(BaseActivity.RESULT_BUNDLE_KEY).
     */
    public fun handleFinishEvent(resultCode: Int, data: Bundle? = null) {
        Log.e(TAG, "handleFinishEvent: finish with Result data: ${Gson().toJson(data)})}")
        val model = ActivityResultEventModel(resultCode, data)

        _finishWithResult.value = Event(model)
    }

    /**
     * Function to initialize a Bundle object in the View class.
     * Recommended to be called in onCreate().
     */
    public fun initBundle(bundle: EventDataBundle?) {
        this.bundle = bundle
    }


    /**
     * Call Event to launch the ActivityResultLauncher registered in the View Class.
     * @param intent Intent to pass to the Launcher
     * @param launcherKey Key value of the Launcher to be executed
     */
    public fun handleLauncherEvent(intent: Intent, launcherKey: String) {
        _launcher.value = Event(Pair(intent, launcherKey))
    }

    /**
     * Call Event to remove application tasks.
     */
    public fun handleRemoveTaskEvent() {
        _removeTask.value = Event(Unit)
    }

    /**
     * Call Event to launch another Activity.
     * @param cls Class of the Activity to be launched
     */
    public fun handleStartActivityEvent(cls: Class<*>) {
        _startActivity.value = Event(cls)
    }

    /**
     * Call Event to launch another Activity.
     * @param cls Class of the Activity to be launched
     * @param data Data to be passed to the Activity being launched <Key, Value>
     */
    public fun handleStartActivityEvent(cls: Class<*>, vararg data: Pair<String, Any>) {
        val model = ActivityBundleEventModel(cls)

        data.forEach { (key, value) ->
            model.put(key, value)
        }

        _startActivityWithBundle.value = Event(model)
    }

    /**
     * Calls an event to start an Activity.
     * Can receive the result of the Activity through callbacks registered in activityResultCallbacks.
     * @param cls Activity class to be executed.
     * @param launcherKey Executes the Activity with the activityResultLauncher associated with the specified key. Upon the Activity's termination, the registered callback in activityResultCallbacks is invoked.
     */
    public fun handleStartActivityEvent(cls: Class<*>, launcherKey: String) {
        val model = ActivityBundleEventModel(cls, launcherKey)

        _startActivityWithBundle.value = Event(model)
    }

    /**
     * Calls an event to start an Activity.
     * Can receive the result of the Activity through callbacks registered in activityResultCallbacks.
     * @param cls Activity class to be executed.
     * @param launcherKey Executes the Activity with the activityResultLauncher associated with the specified key. Upon the Activity's termination, the registered callback in activityResultCallbacks is invoked.
     * @param data Data to be passed to the Activity being launched <Key, Value>
     */
    public fun handleStartActivityEvent(cls: Class<*>, launcherKey: String, vararg data: Pair<String, Any>) {
        val model = ActivityBundleEventModel(cls, launcherKey)

        data.forEach { (key, value) ->
            model.put(key, value)
        }

        _startActivityWithBundle.value = Event(model)
    }


    //region View LifeCycle Functions
    /**
     * A function designed to perform tasks according to the lifecycle of the View.
     * It should be called in the onCreate() of Activity Class.
     * This function is not called in Fragment. Use onFragmentViewCreated() if necessary.
     * @see BaseActivity.onCreate
     */
    public open fun onActivityCreate() {}

    /**
     * A function designed to perform tasks according to the lifecycle of the View.
     * It should be called in the onViewCreated() of Fragment Class.
     * This function is not called in Activity.
     * @see BaseActivity.onCreate
     */
    public open fun onFragmentCreated() {}

    /**
     * A function designed to perform tasks according to the lifecycle of the View.
     * It should be called in the onStart() of View Class.
     * @see BaseActivity.onStart
     * @see BaseFragment.onStart
     */
    public open fun onViewStart() {}

    /**
     * A function designed to perform tasks according to the lifecycle of the View.
     * It should be called in the onResume() of View Class.
     * @see BaseActivity.onResume
     * @see BaseFragment.onResume
     */
    public open fun onViewResume() {}

    /**
     * A function designed to perform tasks according to the lifecycle of the View.
     * It should be called in the onPause() of View Class.
     * @see BaseActivity.onPause
     * @see BaseFragment.onPause
     */
    public open fun onViewPause() {}

    /**
     * A function designed to perform tasks according to the lifecycle of the View.
     * It should be called in the onStop() of View Class.
     * @see BaseActivity.onStop
     * @see BaseFragment.onStop
     */
    public open fun onViewStop() {}

    /**
     * A function designed to perform tasks according to the lifecycle of the View.
     * It should be called in the onDestroy() of View Class.
     * @see BaseActivity.onDestroy
     * @see BaseFragment.onDestroy
     */
    public open fun onViewDestroy() {}
    //endregion

}