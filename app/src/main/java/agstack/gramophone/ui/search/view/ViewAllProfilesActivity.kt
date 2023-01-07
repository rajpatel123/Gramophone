package agstack.gramophone.ui.search.view

import agstack.gramophone.R
import agstack.gramophone.databinding.ActivityViewAllSearchProfilesBinding
import agstack.gramophone.ui.othersporfile.view.OtherUserProfileActivity
import agstack.gramophone.ui.search.adapter.ProfilesAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.navigator.GlobalSearchNavigator
import agstack.gramophone.ui.search.viewmodel.GlobalSearchViewModel
import agstack.gramophone.utils.Constants
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllProfilesActivity :
    GlobalSearchBaseActivity<ActivityViewAllSearchProfilesBinding, GlobalSearchNavigator, GlobalSearchViewModel>(),
    GlobalSearchNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        viewDataBinding.recyclerViewProfiles.layoutManager = gridLayoutManager

        scrollChangeListener?.let { viewDataBinding.nestedScrollView.setOnScrollChangeListener(it) }

        viewDataBinding.recyclerViewProfiles.adapter = ProfilesAdapter(displayDataList) { id ->
            openActivity(this@ViewAllProfilesActivity,
                OtherUserProfileActivity::class.java,
                Bundle().apply {
                    putString(Constants.AUTHER_ID, id)
                    putString(Constants.AUTHER_UUID, id)
                    putString(Constants.REDIRECTION_SOURCE, "View All Profile Screen")
                })
        }

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_profiles
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifySearchResultAdapter(result: List<Data>) {
        super.notifySearchResultAdapter(result)
        val startPosition = displayDataList.size - 1
        var itemCount = 0

        result.filter { it.type?.lowercase() == "profiles" }.forEach {
            displayDataList.addAll(it.items)
            itemCount = it.items.size
        }

        try {
            viewDataBinding.recyclerViewProfiles.adapter?.notifyItemRangeInserted(startPosition,
                itemCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}