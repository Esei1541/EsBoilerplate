package kr.esei.library.module

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kr.esei.library.constant.ActivityInflater

/**
 * ViewBinding 기반 Activity의 공통 사항을 정의하는 추상 클래스.
 * AppCompatActivity Class의 모든 기능과 RecyclerViewBaseAdapter 사용 시 Callback을 정의할 ParentController의 기능을 포함한다.
 * 개별 Activity에서 필요한 사항은 override하여 사용할 것.
 * @param B 해당 Activity의 ViewBinding Class
 * @param inflate ViewBinding을 inflate하는 함수 (ActivityXXX::inflate를 넘겨주면 됨)
 */
public abstract class BaseActivityForViewBinding<B : ViewBinding>(private val inflate: ActivityInflater<B>) : AppCompatActivity(),
    RecyclerViewParentController, BaseActivityListenerSetter {

    protected open val TAG: String = javaClass.simpleName
    protected val binding: B by lazy { inflate(layoutInflater) }

    private var onRequestPermissionResultListener: ((requestCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

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