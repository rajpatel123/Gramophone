package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.R
import agstack.gramophone.databinding.CardCropGroupExplorerItemBinding
import agstack.gramophone.ui.farm.model.Data
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CropGroupExplorerAdapter (
    cropList: List<Data>?,
    private val headerListener: (Data) -> Unit,
    private val contentListener: (Data) -> Unit,
    private val footerListener: (Data) -> Unit,
    private val isOldFarms : Boolean = false,
    private val isCustomerFarms : Boolean = false,
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CropGroupExplorerViewHolder, position: Int) {
        holder.binding.item = list[position]
        holder.binding.isOldFarms = isOldFarms

        holder.binding.headerLayout.setOnClickListener {
            headerListener.invoke(list[position])
        }

        holder.binding.contentLayout.setOnClickListener {
            contentListener.invoke(list[position])
        }

        holder.binding.footerLayout.setOnClickListener {
            footerListener.invoke(list[position])
        }

        holder.binding.txtAddFarm.setTextColor(holder.binding.root.context.resources.getColor(R.color.orange))
        if(/*isOldFarms && */ isCustomerFarms && list[position].is_crop_cycle_completed!!){
            if(list[position].harvested_quantity.isNullOrEmpty()){
                holder.binding.txtAddFarm.text = holder.binding.root.context.getString(R.string.give_your_crop_details)
            }else{
                holder.binding.txtAddFarm.setTextColor(holder.binding.root.context.resources.getColor(R.color.black))
                holder.binding.txtAddFarm.text = holder.binding.root.context.getString(R.string.farm_crop) + " " + list[position].harvested_quantity + " " + list[position].harvested_quantity_unit
            }
        }else{
            if(isOldFarms){
                holder.binding.txtAddFarm.text = ""
            }else{
                holder.binding.txtAddFarm.text = holder.binding.root.context.getString(R.string.add_your_farm)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CropGroupExplorerViewHolder(var binding: CardCropGroupExplorerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}