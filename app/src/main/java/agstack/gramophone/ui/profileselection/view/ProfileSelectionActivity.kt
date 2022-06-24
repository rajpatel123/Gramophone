package agstack.gramophone.ui.profileselection.view

import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityProfileSelectionBinding
import agstack.gramophone.ui.profileselection.ProfileSelectionNavigator
import agstack.gramophone.ui.profileselection.viewmodel.ProfileSelectionViewModel
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class ProfileSelectionActivity : BaseActivity<ActivityProfileSelectionBinding,ProfileSelectionNavigator,ProfileSelectionViewModel>(),ProfileSelectionNavigator {
    private lateinit var binding: ActivityProfileSelectionBinding
    private val viewModel: ProfileSelectionViewModel by viewModels()

    companion object {
        fun start(activity: VerifyOtpActivity) {
            val intent = Intent(activity, ProfileSelectionActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        binding.continueBtn.setOnClickListener {
            // HomeActivity.start(this@ProfileSelectionActivity)
            openActivity(ProfileSelectionActivity::class.java,null)

        }
    }

    private fun setupObservers() {

    }

    override fun getLayoutID(): Int {
        TODO("Not yet implemented")
    }

    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewModel(): ProfileSelectionViewModel {
        TODO("Not yet implemented")
    }
}