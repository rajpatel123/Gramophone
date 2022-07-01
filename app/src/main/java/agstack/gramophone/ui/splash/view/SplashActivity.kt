package agstack.gramophone.ui.splash.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivitySplashBinding
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.splash.SplashNavigator
import agstack.gramophone.ui.splash.viewmodel.SplashViewModel
import agstack.gramophone.utils.Resource
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivityWrapper<ActivitySplashBinding,SplashNavigator,SplashViewModel>(), SplashNavigator {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        startApp()
    }

    private fun startApp() {
        /*splashViewModel.getConfig()*/
        splashViewModel.initSplash()
    }


    private fun setupObservers() {
        splashViewModel.splashViewModel.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    progress.visibility = View.GONE
                    openActivity(LanguageActivity::class.java,null)

                }
                is Resource.Error -> {
                    progress.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(this@SplashActivity, message, Toast.LENGTH_LONG).show()
                    }
                 }
                is Resource.Loading -> {
                    progress.visibility = View.VISIBLE
                }
            }
        })

        splashViewModel.splashLiveData.observe(this, Observer {
            openActivity(LanguageActivity::class.java,null)
        })
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_splash
    }

    override fun getBindingVariable(): Int {
       return BR.viewModel
    }

    override fun getViewModel(): SplashViewModel {
        return splashViewModel
    }
}