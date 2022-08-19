package agstack.gramophone.ui.dialog

import agstack.gramophone.R
import agstack.gramophone.databinding.UpdateLanguageBottomSheetBinding
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.languagelist.GpApiResponseData
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageBottomSheetFragment : BottomSheetDialogFragment(),
    LanguageAdapter.OnLanguageSelection {
    var binding: UpdateLanguageBottomSheetBinding? = null
    var listener: LanguageUpdateListener? = null
    var language: Language? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UpdateLanguageBottomSheetBinding.inflate(inflater)

        setupUi()
        return binding!!.root
    }

    private fun setupUi() {

        var gpApiResponseData =
            SharedPreferencesHelper.instance?.getParcelable(
                SharedPreferencesKeys.languageList
            , GpApiResponseData::class.java)

        val languageAdapter = LanguageAdapter(gpApiResponseData?.language_list!!)
        languageAdapter.setLanguageSelectedListener(this)
        binding?.rvLanguage?.setHasFixedSize(true)
        binding?.rvLanguage?.adapter = languageAdapter

        binding?.ivCloseDialog?.setOnClickListener {
            dismiss()
        }

        binding?.tvContinue?.setOnClickListener {
            if (language != null) {
                val langIsoCode = language?.language_code
                if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
                    LocaleManagerClass.updateLocale(langIsoCode, activity?.resources)
                }
                listener?.onLanguageUpdate()
                dismiss()
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.select_language_msg_english),
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setLanguageListener(listener: LanguageUpdateListener?) {
        this.listener = listener
    }

    interface LanguageUpdateListener {
        fun onLanguageUpdate()
    }

    override fun onLanguageSelect(language: Language) {
        this.language = language
    }
}