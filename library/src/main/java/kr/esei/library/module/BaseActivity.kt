package kr.esei.library.module

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import kr.esei.library.constant.fromJson
import kr.esei.library.constant.observeEvent
import kr.esei.library.constant.toast
import kr.esei.library.model.EventDataBundle

/**
 * Abstract class defining the commonalities of MVVM Activities.
 * Sets up the DataBinding Class and ViewModel received as parameters, and defines additional functionalities.
 * Includes all features of the AppCompatActivity Class.
 * Specific requirements in each Activity Class should be directly override.
 * @param B DataBinding Class for Activity
 * @param VM BaseViewModel Class
 * @param layoutResId Resource ID of the layout XML
 */
public abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(private val layoutResId: Int) : AppCompatActivity(), RecyclerViewParentController {

    public companion object {
        public const val BASE_BUNDLE_KEY: String = "baseActivityBundle"
        public const val RESULT_BUNDLE_KEY: String = "resultBundle"
    }

    protected open val TAG: String = javaClass.simpleName

    protected lateinit var binding: B

    // Override the viewModel in the implementation class.
    // Recommended to use through dependency injection libraries (like Koin).
    protected abstract val activityViewModel: VM

    private val _activityResultLaunchers: MutableMap<String, ActivityResultLauncher<Intent>> = mutableMapOf()
    public val activityResultLaunchers: Map<String, ActivityResultLauncher<Intent>> get() = _activityResultLaunchers

    // startActivityWithBundle Event를 통해 Activity가 실행되었을 경우, 전달받은 Bundle 객체를 가져온다.
    protected val bundle: EventDataBundle? by lazy {
        val bundle = intent.getStringExtra(BASE_BUNDLE_KEY)?.let { Gson().fromJson<EventDataBundle>(it) }
        bundle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this
        observeEvent()
        initActivityResultLauncher()
        activityViewModel.initBundle(bundle)
        activityViewModel.onActivityCreate()
    }

    override fun onStart() {
        super.onStart()
        activityViewModel.onViewStart()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.onViewResume()
    }

    override fun onPause() {
        super.onPause()
        activityViewModel.onViewPause()
    }

    override fun onStop() {
        super.onStop()
        activityViewModel.onViewStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.onViewDestroy()
    }

    /**
     * Initializes the ActivityResultLauncher via the ActivityResultCallback declared in the ViewModel.
     * The initialized ActivityResultLauncher is stored in activityResultLaunchers.
     * When the startActivityWithBundle Event occurs and a launcherKey is passed, the Activity is launched using the launcher corresponding to the provided key.
     */
    private fun initActivityResultLauncher() {
        activityViewModel.activityResultCallbacks.forEach { (key, callback) ->
            _activityResultLaunchers[key] = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
        }
    }

    /**
     * Observes and defines event callback in the ViewModel.
     */
    protected open fun observeEvent() {
        activityViewModel.apply {
            toast.observeEvent(this@BaseActivity) { event ->
                toast(event)
            }
            toastRes.observeEvent(this@BaseActivity) { event ->
                toast(event)
            }
            finish.observeEvent(this@BaseActivity) {
                finish()
            }
            finishWithResult.observeEvent(this@BaseActivity) { result ->
                result.apply {
                    val intent = Intent()
                    val resultBundle = result.data
                    resultBundle?.let { intent.putExtra(RESULT_BUNDLE_KEY, it) }
                    setResult(resultCode, intent)
                }
                finish()
            }
            removeTask.observeEvent(this@BaseActivity) {
                finishAndRemoveTask()
            }
            launcher.observeEvent(this@BaseActivity) { (intent, launcherKey) ->
                _activityResultLaunchers[launcherKey]?.launch(intent)
            }
            startActivity.observeEvent(this@BaseActivity) { clazz ->
                startActivity(Intent(this@BaseActivity, clazz))
            }
            startActivityWithBundle.observeEvent(this@BaseActivity) { event ->
                val intent = Intent(this@BaseActivity, event.activityClass).apply {
                    putExtra(BASE_BUNDLE_KEY, Gson().toJson(event.bundle))
                }

                if (event.launcherKey != null) {
                    _activityResultLaunchers[event.launcherKey]?.launch(intent)
                } else {
                    startActivity(intent)
                }
            }
        }
    }

    //region On click event callback when implementing RecyclerView via RecyclerViewBaseAdapter. Override if necessary.
    override fun onClickListItem(pos: Int, responseCode: Int) {}

    override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int) {}
    //endregion

}
