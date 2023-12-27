package kr.esei.library.module

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

/**
 * Defines the required items for implementing DiffUtill and ListAdapter and defines the logic that is often duplicated.
 * 1. Inherit this class instead of RecyclerView.Adapter and implement the necessary parts.
 * 2. Inherit RecyclerViewParentController in the Activity or Fragment that contains the RecyclerView.
 * 3. Specify the controller through the constructor or setController function.
 * @param MODEL: Model Class Type that defines the data of the List Item
 * @param VH: ViewHolder Class type implemented by inheriting RecyclerView.ViewHolder
 * @param diffCallback: Implement the DiffUtil.ItemCallback to be used in ListAdapter and pass it
 * @see RecyclerViewParentController
 */
public abstract class BaseListAdapter<MODEL, VH: RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<MODEL>): ListAdapter<MODEL, VH>(diffCallback) {

    protected lateinit var controller: RecyclerViewParentController

    public var responseCode: Int = -1  // 한 화면에 2개 이상의 RecyclerView가 들어갈 경우 RecyclerViewParentController의 onClick Event를 발생시킬 때 각 RecyclerView를 구분하기 위해 사용한다.
        private set

    public fun setController(controller: RecyclerViewParentController, responseCode: Int) {
        this.controller = controller
        this.responseCode = responseCode
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    abstract override fun onBindViewHolder(holder: VH, position: Int)

    public open fun moveItem(fromPos: Int, toPos: Int) {
        val newList = currentList.toMutableList()
        Collections.swap(newList, fromPos, toPos)
        submitList(newList)
    }

    public open fun removeItem(pos: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(pos)
        submitList(newList)
    }

}