package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.databinding.CardCropGroupExplorerItemBinding
import agstack.gramophone.ui.farm.model.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CropGroupExplorerAdapter (
    cropList: List<Data>?,
    private val headerListener: (Data) -> Unit,
    private val contentListener: (Data) -> Unit,
    private val footerListener: (Data) -> Unit,
) : RecyclerView.Adapter<CropGroupExplorerAdapter.CropGroupExplorerViewHolder>() {
    private var list = ArrayList<Data>()

    init {
        if(cropList?.isNotEmpty() == true){
            list.addAll(cropList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): CropGroupExplorerViewHolder {
        return CropGroupExplorerViewHolder(
            CardCropGroupExplorerItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CropGroupExplorerViewHolder, position: Int) {
        holder.binding.item = list[position]

        holder.binding.headerLayout.setOnClickListener {
            headerListener.invoke(list[position])
        }

        holder.binding.contentLayout.setOnClickListener {
            contentListener.invoke(list[position])
        }

        holder.binding.footerLayout.setOnClickListener {
            footerListener.invoke(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CropGroupExplorerViewHolder(var binding: CardCropGroupExplorerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}