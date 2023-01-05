package kr.esei.library.module

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * RecyclerView의 Adapter에서 필수적으로 구현해야 할 사항 및 중복 코딩이 잦은 로직을 정의한다.
 * 1. Adapter class에 해당 class를 RecyclerView.Adapter 대신 상속하여 필요한 부분을 구현해줄 것
 * 2. 이후 RecyclerView가 들어가는 Activity 또는 Fragment에 RecyclerViewParentController를 상속한 뒤,
 * 3. 이 class 구현체의 생성자 또는 setController 함수를 통해 controller 변수에 넣어주면 OK
 * @param MODEL: List Item의 데이터가 정의된 Model Class Type
 * @param VB: Item Layout의 ViewBinding Class Type
 * @param VH: RecyclerView.ViewHolder를 상속하여 구현한 ViewHolder Class type
 */
public abstract class RecyclerViewBaseAdapter<MODEL, VB: ViewBinding, VH: RecyclerView.ViewHolder>: RecyclerView.Adapter<VH>() {

    protected open val TAG: String = javaClass.simpleName

    private var _dataList: ArrayList<MODEL> = ArrayList()
    private var _responseCode = -1  // 한 화면에 2개 이상의 RecyclerView가 들어갈 경우 RecyclerViewParentController의 onClick Event를 발생시킬 때 각 RecyclerView를 구분하기 위해 사용한다.

    protected lateinit var controller: RecyclerViewParentController
    public val dataList: List<MODEL> get() = _dataList
    public val responseCode: Int get() = _responseCode

    //region ** RecyclerView.Adapter의 필수 구현 함수들 **
    // 다른 동작이 필요할 경우 override해서 사용할 것

    /**
     * 상속 Class에서 직접 구현
     */
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    /**
     * ViewHolder의 bind()를 호출해 해당 position의 Model을 전달하도록 구현
     */
    abstract override fun onBindViewHolder(holder: VH, position: Int)

    /**
     * dataList의 size를 반환하도록 구현
     */
    override fun getItemCount(): Int { return _dataList.size }
    //endregion

    //region ** Adapter 관련 설정 함수들 **
    /**
     * Controller 객체를 받아 내부 변수에 할당
     * 2개 이상의 Recycler가 들어갈 경우 구분을 위해 responseCode를 받는다
     */
    public fun setController(controller: RecyclerViewParentController, responseCode: Int) {
        this.controller = controller
        this._responseCode = responseCode
    }

    /**
     * 구현할 List에서 사용할 Data Model의 ArrayList를 받아 내부 변수에 할당
     */
    public open fun setDataList(list: ArrayList<MODEL>) {
        _dataList = list
    }

    /**
     * Data List의 갱신을 호출받았을 때 필요한 로직을 수행
     * 추가 로직이 필요할 경우 override하여 구현할 것
     */
    public open fun notifyAdapter() { notifyDataSetChanged() }
    //endregion

    //region ** List 내부 아이템 조작 함수들 **
    public open fun addItems(list: List<MODEL>) {
        _dataList.addAll(list)
    }

    public open fun addItem(item: MODEL) {
        _dataList.add(item)
    }

    public open fun removeItem(position: Int) {
        _dataList.removeAt(position)
    }

    public open fun removeItem(item: MODEL) {
        _dataList.remove(item)
    }

    public open fun clearItem() {
        _dataList.clear()
    }

    public open fun getItem(position: Int) : MODEL {
        return _dataList[position]
    }
    //endregion

}
