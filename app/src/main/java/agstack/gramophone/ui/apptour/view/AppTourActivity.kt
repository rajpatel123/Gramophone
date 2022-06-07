package agstack.gramophone.ui.apptour.view

import agstack.gramophone.R
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityApptourBinding
import agstack.gramophone.splash.viewmodel.AppTourViewModel
import agstack.gramophone.ui.apptour.adapter.DotIndicatorPager2Adapter
import agstack.gramophone.ui.apptour.model.Card
import agstack.gramophone.ui.login.view.LoginActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class AppTourActivity : BaseActivity() {
    private lateinit var binding: ActivityApptourBinding
    private lateinit var viewModel: AppTourViewModel
    private lateinit var items: ArrayList<Card>
    private lateinit var viewPager2: ViewPager2
    var cardIndex:Int =0;
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
        dotsIndicator.setOnClickListener {  }
         viewPager2 = findViewById<ViewPager2>(R.id.view_pager)
          viewPager2.setUserInputEnabled(false);
        val adapter = DotIndicatorPager2Adapter(items)
        viewPager2.adapter = adapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        viewPager2.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        dotsIndicator.attachTo(viewPager2)


        setupUi()
    }

    private fun initCards() {
         items = ArrayList<Card>()

        val cardConnected = Card(R.drawable.communication, getString(R.string.connected),getString(R.string.connected_sub_msg))
        items.add(cardConnected)

        val cardDelivevry = Card(R.drawable.delivery,getString(R.string.delivery), getString(R.string.delivery_sub_msg))
        items.add(cardDelivevry)

        val cardUpdates = Card(R.drawable.midea,getString(R.string.midea), getString(R.string.midea_sub_msg))
        items.add(cardUpdates)
    }


    private fun setupUi() {
        binding.next.setOnClickListener {
            if (cardIndex==items.size-1){
                LoginActivity.start(this@AppTourActivity)
            }else{
                cardIndex++
                viewPager2.currentItem=cardIndex
                if (cardIndex==items.size-1){
                    binding.next.text=getString(R.string.finish)
                }
            }
        }

        binding.skip.setOnClickListener {
            LoginActivity.start(this@AppTourActivity)
        }

    }


}