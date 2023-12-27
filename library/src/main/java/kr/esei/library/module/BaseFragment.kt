package kr.esei.library.module

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import kr.esei.library.constant.observeEvent
import kr.esei.library.constant.toast

/**
 * Abstract class defining the commonalities of MVVM Fragments.
 * Sets up the DataBinding Class and ViewModel received as parameters, and defines additional functionalities.
 * Includes all features of the Fragment Class.
 * Specific requirements in each Fragment Class should be directly override.
 * @param B DataBinding Class for Fragment
 * @param VM BaseViewModel Class
 * @param layoutResId Resource ID of the layout XML
 */
public abstract class BaseFragment<B: ViewDataBinding, VM: BaseViewModel>(private val layoutResId: Int): Fragment(), RecyclerViewParentController {

    protected open val TAG: String = javaClass.simpleName
    private var _binding: B? = null
    protected val binding: B get() = _binding!!

    /**
     * Defines the ViewModel of the fragment.
     * After the onCleared lifecycle, if the Fragment is reloaded, using Koin Library's by viewModel to inject dependencies may cause issues with the viewModelScope's lifecycle.
     * (A scenario where the viewModelScope block does not operate when reloading the Fragment)
     * If such issues arise, it's recommended to inject the ViewModel using getViewModel<VM>() in onCreateView instead of by viewModel.
     */
    protected abstract val fragmentViewModel: VM

    private val _activityResultLaunchers: MutableMap<String, ActivityResultLauncher<Intent>> = mutableMapOf()
    public val activityResultLaunchers: Map<String, ActivityResultLauncher<Intent>> get() = _activityResultLaunchers

    /**
     * Initializes the ActivityResultLauncher through the ActivityResultCallback defined in the ViewModel.
     * The initialized Launcher is stored in the _activityResultLaunchers Map, and can be called through the key of the ActivityResultCallback.
     * This function is called in onViewCreated.
     * @see BaseViewModel.activityResultCallbacks
     */
    private fun initActivityResultLauncher() {
        fragmentViewModel.activityResultCallbacks.forEach { (key, callback) ->
            _activityResultLaunchers[key] = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    //region Calls the ViewModel's Lifecycle function to match the Fragment's Lifecycle.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActivityResultLauncher()
        observeEvent()
        fragmentViewModel.onFragmentCreated()
    }

    override fun onStart() {
        super.onStart()
        fragmentViewModel.onViewStart()
    }
    override fun onResume() {
        super.onResume()

        fragmentViewModel.onViewResume()
    }

    override fun onPause() {
        super.onPause()
        fragmentViewModel.onViewPause()
    }

    override fun onStop() {
        super.onStop()
        fragmentViewModel.onViewStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewModel.onViewDestroy()
        _binding = null
    }
    //endregion

    /**
     * Observes and defines event callback in the ViewModel.
     */
    protected open fun observeEvent() {
        fragmentViewModel.apply {
            toast.observeEvent(viewLifecycleOwner) { event ->
                requireActivity().toast(event)
            }
            toastRes.observeEvent(viewLifecycleOwner) { event ->
                requireActivity().toast(event)
            }
            finish.observeEvent(viewLifecycleOwner) {
                // Finish the parent Activity.
                requireActivity().finish()
            }
            finishWithResult.observeEvent(viewLifecycleOwner) { result ->
                // Finish the parent Activity with the result data.
                result.apply {
                    val intent = Intent()
                    val resultBundle = result.data
                    resultBundle?.let { intent.putExtra(BaseActivity.RESULT_BUNDLE_KEY, it) }
                    requireActivity().setResult(resultCode, intent)
                }
                requireActivity().finish()
            }
            removeTask.observeEvent(viewLifecycleOwner) {
                requireActivity().finishAndRemoveTask()
            }
            launcher.observeEvent(viewLifecycleOwner) { (intent, launcherKey) ->
                activityResultLaunchers[launcherKey]?.launch(intent)
            }
            startActivity.observeEvent(viewLifecycleOwner) { event ->
                startActivity(Intent(requireActivity(), event))
            }
            startActivityWithBundle.observeEvent(viewLifecycleOwner) { event ->
                val intent = Intent(requireActivity(), event.activityClass).apply {
                    putExtra(BaseActivity.BASE_BUNDLE_KEY, Gson().toJson(event.bundle))
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