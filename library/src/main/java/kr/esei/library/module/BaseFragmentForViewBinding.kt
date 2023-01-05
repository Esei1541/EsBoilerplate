package kr.esei.library.module

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kr.esei.esboilerplate.FragmentInflater

public abstract class BaseFragmentForViewBinding<B : ViewBinding>(private val inflate: FragmentInflater<B>) : Fragment(), RecyclerViewParentController {

    protected open val TAG: String = javaClass.simpleName
    private var _binding: B? = null
    protected val binding: B get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //region RecyclerViewBaseAdapter를 통해 RecyclerView 구현 시 click event callback. 필요할 경우 override 할 것.
    override fun onClickListItem(pos: Int, responseCode: Int) {}

    override fun onClickInnerItem(pos: Int, id: Int, responseCode: Int) {}
    //endregion
}