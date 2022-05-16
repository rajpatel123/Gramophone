package agstack.gramophone.ui.language.adapter


import agstack.gramophone.databinding.ItemLanguageBinding
import agstack.gramophone.ui.language.model.LanguageData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LanguageAdapter :
    RecyclerView.Adapter<LanguageAdapter.DeveloperViewHolder>() {

    private var mLanguageData: ArrayList<LanguageData>? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        return DeveloperViewHolder(
            ItemLanguageBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(mDeveloperViewHolder: DeveloperViewHolder, i: Int) {
       /* val currentStudent = mLanguageData!![i]*/




    }

    override fun getItemCount(): Int {
       /* return if (mLanguageData != null) {
            mLanguageData!!.size
        } else {
            0
        }*/
        return 10
    }

    fun setDeveloperList(languageData: ArrayList<LanguageData>) {
        this.mLanguageData = languageData
        notifyDataSetChanged()
    }

    inner class DeveloperViewHolder(var itemLanguageBinding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(itemLanguageBinding.root)
}