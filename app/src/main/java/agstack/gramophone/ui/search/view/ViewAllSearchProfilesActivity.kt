package agstack.gramophone.ui.search.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.base.BaseViewModel
import agstack.gramophone.databinding.ActivityViewAllSearchProfilesBinding
import agstack.gramophone.ui.othersporfile.view.OtherUserProfileActivity
import agstack.gramophone.ui.search.adapter.ProfilesAdapter
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.utils.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.amnix.xtension.extensions.toCamelCase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewAllSearchProfilesActivity :
    BaseActivityWrapper<ActivityViewAllSearchProfilesBinding, BaseNavigator, BaseViewModel<BaseNavigator>>(),
    BaseNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataList = getBundle()?.getParcelable<Data>("dataList")
        dataList?.type?.replace('_', ' ')?.toCamelCase()?.trim()?.let { setToolbarTitle(it) }

        val items = dataList?.items
        items?.let {
            val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            viewDataBinding.recyclerViewProfiles.layoutManager = gridLayoutManager
            viewDataBinding.recyclerViewProfiles.adapter = ProfilesAdapter(items){id, authorId, authorUuid ->
                openActivity(this@ViewAllSearchProfilesActivity,
                    OtherUserProfileActivity::class.java,
                    Bundle().apply {
                        putString(Constants.POST_ID, id)
                        putString(Constants.AUTHER_ID, authorId)
                        putString(Constants.AUTHER_UUID, authorUuid)
                    })
            }
        }
    }

    fun getBundle(): Bundle? {
        return intent.extras
    }

    fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_cross)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_view_all_search_profiles
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): BaseViewModel<BaseNavigator> {
        val viewModel: BaseViewModel<BaseNavigator> by viewModels()
        return viewModel
    }

    fun <T> openActivity(context: Context, cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {
            if (extras != null)
                putExtras(extras)
            context.startActivity(this)
        }
    }
}