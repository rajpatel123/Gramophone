package agstack.gramophone.ui.verifyotp.view

import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.databinding.ActivityVerifyOtpBinding
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.verifyotp.VerifyOTPNavigator
import agstack.gramophone.ui.verifyotp.viewmodel.VerifyOtpViewModel
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class VerifyOtpActivity : BaseActivity<ActivityVerifyOtpBinding, VerifyOTPNavigator, VerifyOtpViewModel>(),
    VerifyOTPNavigator {

    private lateinit var binding: ActivityVerifyOtpBinding
    private lateinit var viewModel: VerifyOtpViewModel

    companion object {
        fun start(activity: LoginActivity) {
            val intent = Intent(activity, VerifyOtpActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        setupObservers()
    }

    private fun setupObservers() {

    }


    private fun setupUi() {
        binding.changeNumberTxt.setOnClickListener { onBackPressed() }
        binding.verifyOtpBtn.setOnClickListener {
           openActivity(HomeActivity::class.java)

        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        LoginActivity.start(this@VerifyOtpActivity)
    }
}