package agstack.gramophone.ui.home.view

import agstack.gramophone.R
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.menu.BottomNavigationView
import agstack.gramophone.menu.OnNavigationItemChangeListener
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class HomeActivity : BaseActivity() {

    private lateinit var viewModel: HomeViewModel

    companion object {
        fun start(activity: VerifyOtpActivity) {
            val intent = Intent(activity, HomeActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupViewModel()
        setupUi()
        setupObservers()
    }

    private fun setupObservers() {

    }


    private fun setupUi() {
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomMenu?.setOnNavigationItemChangedListener(object : OnNavigationItemChangeListener {
            override fun onNavigationItemChanged(navigationItem: BottomNavigationView.NavigationItem) {
                Toast.makeText(this@HomeActivity,
                    "Selected item at index ${navigationItem.position}",
                    Toast.LENGTH_SHORT).show()
            }
        })
        /* If you want to change active navigation item programmatically
         * bottomMenu.setActiveNavigationIndex(2)
         */
    }

    private fun setupViewModel() {
        // With ViewModelFactory
    }
}