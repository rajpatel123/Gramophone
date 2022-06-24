package agstack.gramophone.ui.profile.view

import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityProfileBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.profile.viewmodel.ProfileViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    companion object {
        fun start(activity: HomeActivity) {
            val intent = Intent(activity, ProfileActivity::class.java)
            activity.startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }



    private fun setupUi() {
        iv_cross.setOnClickListener {
            finish()
        }
    }

    private fun setupObservers() {

    }
}