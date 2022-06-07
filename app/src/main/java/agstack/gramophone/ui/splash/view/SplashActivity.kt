package agstack.gramophone.ui.splash.view

import agstack.gramophone.R
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityLoginBinding
import agstack.gramophone.databinding.ActivitySplashBinding
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.login.LoginViewModel
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.ui.splash.viewmodel.SplashViewModel
import agstack.gramophone.utils.Resource
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
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
        splashViewModel.getConfig()
    }


    private fun setupObservers() {
        splashViewModel.splashViewModel.observe(this, Observer{
            when (it) {
                is Resource.Success -> {
                    binding.progress.visibility = View.GONE
                    val intent = Intent(this, LanguageActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                is Resource.Error -> {
                    binding.progress.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(this@SplashActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }
        })


    }
}