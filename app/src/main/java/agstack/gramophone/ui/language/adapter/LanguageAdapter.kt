package agstack.gramophone.ui.language.adapter


import agstack.gramophone.databinding.ItemLanguageBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.language.model.LanguageData
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LanguageAdapter(private val languageList: ArrayList<LanguageData>) :
    RecyclerView.Adapter<LanguageAdapter.DeveloperViewHolder>() {

    interface ItemClickListener {
        fun onLanguageClick()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        return DeveloperViewHolder(
            ItemLanguageBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, i: Int) {
        holder.itemLanguageBinding.tvLanguageTitle.text = languageList[i].title
        holder.itemLanguageBinding.tvLanguage.text = languageList[i].value
        holder.itemLanguageBinding.llLanguageSelection.setOnClickListener(View.OnClickListener {
            val intent = Intent(holder.itemView.context, HomeActivity::class.java)
           holder.itemView.context.startActivity(intent)
        })


    }

    override fun getItemCount(): Int {
        return languageList.size ?: 0
    }

    inner class DeveloperViewHolder(var itemLanguageBinding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(itemLanguageBinding.root)
}