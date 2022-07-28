package agstack.gramophone.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerScrollListener(
    val mLinearLayoutManager: LinearLayoutManager,
    val onLoadMore: (Int) -> Unit
) :
    RecyclerView.OnScrollListener() {

    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    var firstVisibleItem: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var lastVisible = 0

    private var current_page = 1


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = mLinearLayoutManager.getItemCount()
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
        lastVisible = mLinearLayoutManager.findLastCompletelyVisibleItemPosition()



        if (!isLastPage()) {
            if (lastVisible != RecyclerView.NO_POSITION && lastVisible + 1 == totalItemCount)
             {

                loading = true
                onLoadMore(current_page+1)

            }
        }


    }

    abstract fun isLastPage(): Boolean

    fun onListFetched(){
        loading = false
        current_page++
    }


}