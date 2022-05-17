package agstack.gramophone.ui.language.adapter


import agstack.gramophone.R
import agstack.gramophone.databinding.ItemLanguageBinding
import agstack.gramophone.ui.apptour.view.AppTourActivity
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.language.model.LanguageData
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.login.view.LoginActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
        if (languageList[i].isSelected) {
            holder.binding.llLanguageSelection.setBackgroundResource(R.drawable.rounded_corner_16_solid_blue_stroke_no)
            holder.binding.tvLanguageTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            holder.binding.tvLanguage.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else{
            holder.binding.llLanguageSelection.setBackgroundResource(R.drawable.rounded_corner_16_solid_white_stroke_gray)
            holder.binding.tvLanguageTitle.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.binding.tvLanguage.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
        }
        holder.binding.tvLanguageTitle.text = languageList[i].title
        holder.binding.tvLanguage.text = languageList[i].value
        holder.binding.llLanguageSelection.setOnClickListener(View.OnClickListener {
            val intent = Intent(holder.itemView.context, AppTourActivity::class.java)
           holder.itemView.context.startActivity(intent)
        })


    }

    override fun getItemCount(): Int {
        return languageList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)
}