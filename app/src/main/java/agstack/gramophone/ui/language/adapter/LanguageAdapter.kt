package agstack.gramophone.ui.language.adapter


import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.databinding.ItemLanguageBinding
import agstack.gramophone.databinding.ItemRadioProductPackingBinding
import agstack.gramophone.ui.apptour.view.AppTourActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductSkuListItem
import agstack.gramophone.ui.language.model.LanguageData
import agstack.gramophone.ui.language.model.languagelist.Language
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * binding will be replaced later
 */
class LanguageAdapter(private val languageList: List<Language>) :
    RecyclerView.Adapter<LanguageAdapter.DeveloperViewHolder>() {
    var selectedLanguage: ((Language) -> Unit)? = null
    var lastSelectPosition: Int = 0
    var mLanguageList = languageList

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        return DeveloperViewHolder(
            ItemLanguageBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var language: Language = mLanguageList[position]!!
        holder.binding.setVariable(BR.model, language)
        val mBinding = holder.binding as ItemLanguageBinding
        mBinding.llLanguageLinearLayout.setOnClickListener {
            mLanguageList[lastSelectPosition]?.selected = false
            lastSelectPosition = position
            language.selected = true
            notifyDataSetChanged()
            selectedLanguage?.invoke(language)
        }

    }

    override fun getItemCount(): Int {
        return mLanguageList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)
}