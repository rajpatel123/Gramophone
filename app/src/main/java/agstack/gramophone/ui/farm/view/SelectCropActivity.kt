package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivitySelectCropBinding
import agstack.gramophone.ui.farm.adapter.SelectCropAdapter
import agstack.gramophone.ui.farm.model.FarmEvent
import agstack.gramophone.ui.farm.navigator.SelectCropNavigator
import agstack.gramophone.ui.farm.viewmodel.SelectCropViewModel
import agstack.gramophone.ui.search.view.GlobalSearchActivity
import agstack.gramophone.utils.EventBus
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

@AndroidEntryPoint
class SelectCropActivity :
    BaseActivityWrapper<ActivitySelectCropBinding, SelectCropNavigator, SelectCropViewModel>(),
    SelectCropNavigator {

    private var disposable: Disposable? = null

    companion object {
        fun start(activity : AppCompatActivity){
            activity.startActivity(Intent(activity, SelectCropActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getMessage(R.string.add_tag_title))
        getViewModel().getCrops()

        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            getViewModel().clearSelection()
            getViewModel().getCrops()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }

        disposable =  EventBus.subscribe<FarmEvent>()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    finishActivity()
                }


    }

    override fun setToolbarTitle(title: String) {
        val toolbar = viewDataBinding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title
        toolbar.setNavigationIcon(R.drawable.ic_cross)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.elevation = 24f
        toolbar.title = title

        toolbar.inflateMenu(R.menu.menu_search)
        toolbar.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_global_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_search -> {
                openActivity(GlobalSearchActivity::class.java, Bundle().apply {
                    putBoolean("searchInCommunity", false)
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setSelectCropAdapter(selectCropAdapter: SelectCropAdapter) {
        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (selectCropAdapter.getItemViewType(position) == selectCropAdapter.viewTypeHeader) 3 else 1
            }
        }
        viewDataBinding.rvSelectCrop.layoutManager = layoutManager
        viewDataBinding.rvSelectCrop.setHasFixedSize(true)
        viewDataBinding.rvSelectCrop.adapter = selectCropAdapter
    }

    override fun notifyAdapter(position : Int) {
        viewDataBinding.rvSelectCrop.adapter?.notifyItemChanged(position)
    }

    override fun finishActivity() {
        finish()
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_select_crop
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): SelectCropViewModel {
        val viewModel: SelectCropViewModel by viewModels()
        return viewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let { if(!it.isDisposed) it.dispose() }
    }

}