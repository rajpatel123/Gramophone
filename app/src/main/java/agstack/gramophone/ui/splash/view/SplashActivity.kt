package agstack.gramophone.ui.splash.view

import agstack.gramophone.R
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.splash.model.SplashModel
import agstack.gramophone.ui.splash.viewmodel.SplashViewModel
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer

class SplashActivity : BaseActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupUi()
        setupObservers()
    }


    private fun setupUi() {

    }

    private fun setupObservers() {

        val observer = Observer<SplashModel> {
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}