package agstack.gramophone.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndlessRecyclerScrollListener(val mLinearLayoutManager: LinearLayoutManager, val onLoadMore: (Int, (()->Unit)?) -> Unit) :
    RecyclerView.OnScrollListener() {

    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    var firstVisibleItem: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0

    private var current_page = 1


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = mLinearLayoutManager.getItemCount()
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount - 1 > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= firstVisibleItem && totalItemCount != visibleItemCount && totalItemCount%10==0) {
            current_page++
            loading = true
            onLoadMore(totalItemCount/3){
                this@EndlessRecyclerScrollListener.loading=false
            }
        }
    }

    fun reset() {
        previousTotal = 0
        loading = true
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
        current_page=1
    }

}