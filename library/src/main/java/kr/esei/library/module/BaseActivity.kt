package kr.esei.library.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kr.esei.library.constant.toast

/**
 * 모든 MVVM Activity의 공통 사항을 정의하는 추상 클래스.
 * Parameter로 받은 DataBinding Class 및 ViewModel의 세팅과 추가 기능을 정의한다.
 * AppCompatActivity Class의 모든 기능과 ViewModel과의 연결을 담당할 ActivityNavigator Class, RecyclerViewBaseAdapter 사용 시 Callback을 정의할 ParentController의 기능을 포함한다.
 * 개별 Activity에서 필요한 사항은 override하여 사용할 것.
 * @param B 해당 Activity의 DataBinding Class
 * @param VM ViewModel Class
 * @param layoutResId Layout xml의 resource ID
 */
public abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(private val layoutResId: Int) : AppCompatActivity(), ActivityNavigator, RecyclerViewParentController, BaseActivityListenerSetter {

    protected open val TAG: String = javaClass.simpleName

    protected lateinit var binding: B

    // 구현 클래스에서 viewModel을 override하여 사용할 것
    // Koin 라이브러리 등으로 의존성을 주입받아 사용하는 것을 추천
    protected abstract val viewModel: VM

    override val context: Context get() = this
    override val activity: AppCompatActivity get() = this

    private lateinit var rootViewGroup: ViewGroup
    private var onRequestPermissionResultListener: ((requestCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this
    }

    override fun clearFocus() {
        val root = binding.root as ViewGroup
        root.clearFocus()
    }

    //region navigator에서 activity 또는 context의 기능에 빠르게 접근하기 위한 shortcut function을 정의
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
    }

    override fun finishAffinity() {
        super.finishAffinity()
    }

    override fun toast(res: Int) {
        context.toast(getString(res))
    }

    override fun toast(string: String) {
        context.toast(string)
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        super.startActivity(intent, options)
    }
    //endregion

    //region activity 외부에서 event를 설정할 수 있도록 setOnListener 함수를 정의
    override fun setOnRequestPermissionsResultListener(listener: (requestCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit) {
        onRequestPermissionResultListener = listener
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionResultListener?.invoke(requestCode, permissions, grantResults)
    }
    //endregion

    //region RecyclerViewBaseAdapter를 통해 RecyclerView 구현 시 click event callback. 필요할 경우 override 할 것.
    override fun onClickListItem(pos: Int, responseCode: Int) {}

    override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int) {}
    //endregion

}
