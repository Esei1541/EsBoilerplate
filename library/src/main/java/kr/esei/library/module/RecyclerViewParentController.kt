package kr.esei.library.module

/**
 * Defines the essentials that must be implemented in View Classes containing RecyclerView.
 * Inherit and implement in Activity or Fragment connected to BaseListAdapter.
 * @see BaseListAdapter
 */
public interface RecyclerViewParentController {

    /**
     * When a List Item is clicked, it passes the position of that Item.
     * When implementing BaseListAdapter, this function should be called from within the onClickListener.
     */
    public fun onClickListItem(pos: Int, responseCode: Int)

    /**
     * When a specific View inside a List Item is clicked, it passes the position of the Item and the ID of the clicked View.
     * When implementing BaseListAdapter, this function should be called from within the onClickListener.
     */
    public fun onClickInnerItem(pos: Int, id: Int, responseCode: Int)

}