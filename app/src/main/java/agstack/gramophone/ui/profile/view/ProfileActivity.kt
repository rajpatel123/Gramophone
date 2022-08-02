package agstack.gramophone.ui.profile.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityProfileBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.login.view.LoginActivity
import agstack.gramophone.ui.profile.ProfileActivityNavigator
import agstack.gramophone.ui.profile.viewmodel.ProfileViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivityWrapper<ActivityProfileBinding, ProfileActivityNavigator, ProfileViewModel>(),ProfileActivityNavigator {

    private val profileViewModel: ProfileViewModel by viewModels()

    companion object {
        fun start(activity: HomeActivity) {
            val intent = Intent(activity, ProfileActivity::class.java)
            activity.startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        setupObservers()
    }



    private fun setupUi() {

    }

    private fun setupObservers() {

    }

    override fun getLayoutID(): Int {
       return R.layout.activity_profile
    }

    override fun getBindingVariable(): Int {
       return BR.viewModel
    }

    override fun getViewModel(): ProfileViewModel {
       return profileViewModel
    }

    override fun logout() {
        ActivityCompat.finishAffinity(this);
        openAndFinishActivity(LanguageActivity::class.java,null)
    }
}