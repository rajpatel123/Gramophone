package agstack.gramophone.ui.apptour.view

import agstack.gramophone.R
import agstack.gramophone.base.BaseActivity
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
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import java.util.*

class AppTourActivity : BaseActivity<ActivityApptourBinding,AppTourNavigator,AppTourViewModel>(), AppTourNavigator {
    private lateinit var scrollImageRunable: Runnable
    private lateinit var mainHandler: Handler
    private lateinit var binding: ActivityApptourBinding
    private lateinit var viewModel: AppTourViewModel
    private lateinit var items: ArrayList<Card>
    private lateinit var viewPager2: ViewPager2
    var cardIndex: Int = 0;
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed

    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_tour_bg)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityApptourBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initCards()
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        dotsIndicator.setOnClickListener { }
        viewPager2 = findViewById<ViewPager2>(R.id.view_pager)
        //viewPager2.setUserInputEnabled(false);
        val adapter = DotIndicatorPager2Adapter(items)
        viewPager2.adapter = adapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        viewPager2.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        dotsIndicator.attachTo(viewPager2)
        setupUi()
        scrollImages()
    }

    private fun scrollImages() {
        mainHandler = Handler(Looper.getMainLooper())
        scrollImageRunable = Runnable {
            if (currentPage === items.size) {
                currentPage = 0
            }
            viewPager2.setCurrentItem(currentPage++, true)
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
        binding.next.setOnClickListener {
            mainHandler?.removeCallbacks(scrollImageRunable)
            openAndFinishActivity(LoginActivity::class.java, null)

        }



    }

    override fun getLayoutID(): Int {
        TODO("Not yet implemented")
    }

    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewModel(): AppTourViewModel {
        TODO("Not yet implemented")
    }


}