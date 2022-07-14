package agstack.gramophone.ui.apptour.view

import agstack.gramophone.R
import agstack.gramophone.BR
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityApptourBinding
import agstack.gramophone.ui.apptour.AppTourNavigator
import agstack.gramophone.ui.apptour.adapter.DotIndicatorPager2Adapter
import agstack.gramophone.ui.apptour.model.Card
import agstack.gramophone.ui.apptour.viewmodel.AppTourViewModel
import agstack.gramophone.ui.login.view.LoginActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_apptour.*
import java.util.*

class AppTourActivity : BaseActivityWrapper<ActivityApptourBinding,AppTourNavigator,AppTourViewModel>(), AppTourNavigator {
    private lateinit var scrollImageRunable: Runnable
    private lateinit var mainHandler: Handler
    private  val appTourViewModel: AppTourViewModel by viewModels()
    private lateinit var items: ArrayList<Card>
    var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.statusBarColor = ContextCompat.getColor(this, R.color.app_tour_bg)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        initCards()
        val adapter = DotIndicatorPager2Adapter(items)
        view_pager.adapter = adapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        view_pager.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        dots_indicator.attachTo(view_pager)
        setupUi()
        scrollImages()
    }

    private fun scrollImages() {
        mainHandler = Handler(Looper.getMainLooper())
        scrollImageRunable = Runnable {
            if (currentPage === items.size) {
                currentPage = 0
            }
            view_pager.setCurrentItem(currentPage++, true)
            mainHandler.postDelayed(scrollImageRunable, 3*1000L)
        }
        mainHandler.post(scrollImageRunable)

    }

    private fun initCards() {
        items = ArrayList<Card>()

        val cardConnected = Card(
            R.drawable.connected,
            getString(R.string.connected),
            getString(R.string.connected_sub_msg)
        )
        items.add(cardConnected)

        val cardDelivevry = Card(
            R.drawable.delivery,
            getString(R.string.delivery),
            getString(R.string.delivery_sub_msg)
        )
        items.add(cardDelivevry)

        val cardUpdates =
            Card(R.drawable.updates, getString(R.string.midea), getString(R.string.midea_sub_msg))
        items.add(cardUpdates)
    }


    private fun setupUi() {
        next.setOnClickListener {
            mainHandler?.removeCallbacks(scrollImageRunable)
            openAndFinishActivity(LoginActivity::class.java, null)

        }



    }

    override fun getLayoutID(): Int {
        return R.layout.activity_apptour
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AppTourViewModel {
        return appTourViewModel
    }

    override fun onError(message: String?) {
        TODO("Not yet implemented")
    }

    override fun <T> onSuccess(cls: Class<T>) {
        TODO("Not yet implemented")
    }

    override fun onLoading() {
        TODO("Not yet implemented")
    }


}