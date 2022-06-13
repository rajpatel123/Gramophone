package agstack.gramophone.ui.home.view

import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityProfileBinding
import agstack.gramophone.ui.home.repository.HomeViewModel
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: HomeViewModel

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
    }



    private fun setupUi() {

    }


}