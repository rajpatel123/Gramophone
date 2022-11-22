package agstack.gramophone.ui.farm.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivitySelectCropBinding
import agstack.gramophone.ui.farm.adapter.SelectCropAdapter
import agstack.gramophone.ui.farm.model.FarmEvent
import agstack.gramophone.ui.farm.navigator.SelectCropNavigator
import agstack.gramophone.ui.farm.viewmodel.SelectCropViewModel
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.utils.EventBus
import agstack.gramophone.utils.RxSearchObservable
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.amnix.xtension.extensions.isNotNullOrEmpty
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SelectCropActivity :
    BaseActivityWrapper<ActivitySelectCropBinding, SelectCropNavigator, SelectCropViewModel>(),
    SelectCropNavigator {

    private var disposable: Disposable? = null
    private val originalList = arrayListOf<CropData>()
    private val filterList = arrayListOf<CropData>()
    private var disableSearchForAWhile = false
    private val handler =  Handler(Looper.getMainLooper())
    var lastCheckedPosition = -1

    companion object {
        fun start(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, SelectCropActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarTitle(getMessage(R.string.add_tag_title))
        getViewModel().getCrops()

        searchInit()
        setSelectCropAdapter()

        viewDataBinding.swipeRefresh.setColorSchemeResources(R.color.blue)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            getViewModel().clearSelection()
            getViewModel().getCrops()
            viewDataBinding.swipeRefresh.isRefreshing = false
        }

        disposable = EventBus.subscribe<FarmEvent>()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.text == "farm_added")
                    finishActivity()
            }
    }

    override fun setToolbarTitle(title: String) {
        setUpToolBar(true, title, R.drawable.ic_cross, true)
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
                getViewModel().onSearchMenuItemClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setSelectCropAdapter() {
        val adapter = SelectCropAdapter(filterList) { cropData ->
            filterList.forEach {
                it.isSelected = false
            }
            getViewModel().selectedCrop = cropData;
            getViewModel().selectedCrop?.isSelected = true

            val copyOfLastCheckedPosition = lastCheckedPosition
            lastCheckedPosition = filterList.indexOf(cropData)

            notifyAdapter(copyOfLastCheckedPosition)
            notifyAdapter(lastCheckedPosition)
        }

        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == adapter.viewTypeHeader) 3 else 1
            }
        }
        viewDataBinding.rvSelectCrop.layoutManager = layoutManager
        viewDataBinding.rvSelectCrop.setHasFixedSize(true)
        viewDataBinding.rvSelectCrop.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun updateCropAdapter(cropList: List<CropData>) {
        originalList.clear()
        originalList.addAll(cropList)

        filterList.clear()
        filterList.addAll(cropList)

        viewDataBinding.rvSelectCrop.adapter?.notifyDataSetChanged()
    }

    override fun notifyAdapter(position: Int) {
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
        disposable?.let { if (!it.isDisposed) it.dispose() }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchInit() {
        RxSearchObservable.fromView(viewDataBinding.edtSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .filter { it.isNotNullOrEmpty() }
            .map { s -> s.toString().lowercase(Locale.getDefault()).trim() }
            .switchMap { Flowable.just(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                filterList.forEach {
                    it.isSelected = false
                }
                filterList.clear()

                originalList.filter { it.cropName == text }.forEach {
                    filterList.add(it)
                }
                viewDataBinding.rvSelectCrop.adapter?.notifyDataSetChanged()
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSearchViewClearClick() {
        viewDataBinding.edtSearch.text?.clear()
        filterList.clear()
        filterList.addAll(originalList)
        viewDataBinding.rvSelectCrop.adapter?.notifyDataSetChanged()
    }

    private fun disableSearchForAWhile(){
        disableSearchForAWhile = true
        handler.postDelayed({
            disableSearchForAWhile = false
        }, 1000)
    }
}