package agstack.gramophone.ui.language.view

import agstack.gramophone.R
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityLanguageBinding
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.LanguageData
import agstack.gramophone.ui.language.viewmodel.LanguageViewModel
import agstack.gramophone.ui.splash.view.SplashActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView

class LanguageActivity : BaseActivity() {

    private lateinit var binding: ActivityLanguageBinding
    private val viewModel: LanguageViewModel by viewModels()

    companion object {
        fun start(activity: SplashActivity) {
            val intent = Intent(activity, LanguageActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }

    private fun setupObservers() {

    }


    private fun setupUi() {
        /* binding?.recyclerLanguage?.layoutManager = GridLayoutManager(this, 2)
         binding?.recyclerLanguage?.setHasFixedSize(true)
         binding?.recyclerLanguage?.adapter = LanguageAdapter()*/
        val languageList = ArrayList<LanguageData>()
        languageList.add(LanguageData("English", "English", true))
        languageList.add(LanguageData("हिंदी", "Hindi", false))
        languageList.add(LanguageData("मराठी", "Marathi", false))
        languageList.add(LanguageData("ગુજરાતી", "Gujrati", false))

        val recyclerLanguage = findViewById<RecyclerView>(R.id.recycler_language)
        recyclerLanguage?.setHasFixedSize(true)
        recyclerLanguage?.adapter = LanguageAdapter(languageList)
    }


}