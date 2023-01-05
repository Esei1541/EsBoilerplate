package kr.esei.library.module

/**
 * RecyclerView의 부모 View에서 필수적으로 구현해야 할 사항을 정의하고 controller를 RecyclerViewAdapter interface에 넘겨줄 수 있게 해주는 interface
 * RecyclerView를 포함하는 Activity 또는 Fragment에 상속하여 구현해줄 것
 */
public interface RecyclerViewParentController {

    // List Item Click 시 해당 item의 position을 받아온다
    public fun onClickListItem(pos: Int, responseCode: Int)

    // List Item 내부의 View를 Click했을 때 position과 클릭한 view의 id를 전달
    public fun onClickInnerItem(pos: Int, id: Int, responseCode: Int)

}