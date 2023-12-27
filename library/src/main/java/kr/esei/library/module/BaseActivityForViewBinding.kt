package kr.esei.library.module

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kr.esei.library.constant.ActivityInflater

/**
 * Abstract class defining common aspects of Activity withs ViewBinding.
 * Includes all functionalities of the AppCompatActivity Class and the ability to define ParentController functionalities for RecyclerViewBaseAdapter Callbacks.
 * Specific requirements in each Activity Class should be directly override.
 * @param B ViewBinding Class of the Activity
 * @param inflate Function to inflate ViewBinding (pass ActivityXXXBinding::inflate)
 */
public abstract class BaseActivityForViewBinding<B : ViewBinding>(private val inflate: ActivityInflater<B>) : AppCompatActivity(),
    RecyclerViewParentController {

    protected open val TAG: String = javaClass.simpleName
    protected val binding: B by lazy { inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    //region RecyclerViewBaseAdapter를 통해 RecyclerView 구현 시 click event callback. 필요할 경우 override 할 것.
    override fun onClickListItem(pos: Int, responseCode: Int) {}

    override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int) {}
    //endregion

}