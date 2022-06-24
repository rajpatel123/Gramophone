package agstack.gramophone.ui.profileselection.view

import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityProfileSelectionBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.profileselection.ProfileSelectionNavigator
import agstack.gramophone.ui.profileselection.viewmodel.ProfileSelectionViewModel
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.Resource
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity

@AndroidEntryPoint
class ProfileSelectionActivity : BaseActivity() {
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

            //Perform validations then call api
            val hashMap = HashMap<Any, Any>()
            hashMap[Constants.PROFILE_TYPE] = Constants.FARMER

            viewModel.updateProfileType(hashMap)
        }
    }

    private fun setupObservers() {
        viewModel.updateProfileData.observe(this, Observer{
            when (it) {
                is Resource.Success -> {
                    binding.progress.visibility = View.GONE
                    openActivity(ProfileSelectionActivity::class.java,null)
                }
                is Resource.Error -> {
                    binding.progress.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(this@ProfileSelectionActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }
            }
        })

    }
}