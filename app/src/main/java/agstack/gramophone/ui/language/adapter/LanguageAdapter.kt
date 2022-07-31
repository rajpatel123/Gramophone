package agstack.gramophone.ui.language.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemLanguageBinding
import agstack.gramophone.ui.language.model.languagelist.Language
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * binding will be replaced later
 */
class LanguageAdapter(private val languageList: List<Language>) :
    RecyclerView.Adapter<LanguageAdapter.DeveloperViewHolder>() {
    private var langaugeCode: String? = null
    var selectedLanguage: ((Language) -> Unit)? = null
    var lastSelectPosition: Int = 0
    var mLanguageList = languageList
     var onLanguageSelection: OnLanguageSelection? = null

    interface OnLanguageSelection {
        fun onLanguageSelect(language: Language)
    }

    fun setLanguageSelectedListener(onLanguageSelection: OnLanguageSelection) {
        this.onLanguageSelection=onLanguageSelection
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemLanguageBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var language: Language = mLanguageList[position]!!
        holder.binding.setVariable(BR.model, language)
        val mBinding = holder.binding

        mBinding.llLanguageLinearLayout.setOnClickListener {
            mLanguageList[lastSelectPosition]?.selected = false
            mLanguageList.forEach {
                it.selected=false
            }
            lastSelectPosition = position
            language.selected = true
            notifyDataSetChanged()
            selectedLanguage?.invoke(language)
            onLanguageSelection?.onLanguageSelect(language)
        }

    }

    override fun getItemCount(): Int {
        return mLanguageList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)
}