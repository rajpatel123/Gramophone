package agstack.gramophone.ui.language.adapter


import agstack.gramophone.databinding.ItemLanguageBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.language.model.LanguageData
import agstack.gramophone.ui.language.view.LanguageActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LanguageAdapter :
    RecyclerView.Adapter<LanguageAdapter.DeveloperViewHolder>() {

    private var mLanguageData: ArrayList<LanguageData>? = null

    interface ItemClickListener {
        fun onLanguageClick()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {
        return DeveloperViewHolder(
            ItemLanguageBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, i: Int) {
       /* val currentStudent = mLanguageData!![i]*/

        holder.itemLanguageBinding.llLanguageSelection.setOnClickListener(View.OnClickListener {
            val intent = Intent(holder.itemView.context, HomeActivity::class.java)
           holder.itemView.context.startActivity(intent)
        })


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