package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.R
import agstack.gramophone.databinding.CardViewAllFarmsBinding
import agstack.gramophone.ui.farm.model.Data
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViewAllFarmsAdapter(
    farmItemList: List<List<Data>>?,
    private val headerListener: (List<Data>) -> Unit,
    private val contentListener: (List<Data>) -> Unit,
    private val footerListener: (List<Data>) -> Unit,
    private val isOldFarms : Boolean = false,
    private val isCustomerFarm : Boolean = false,
) : RecyclerView.Adapter<ViewAllFarmsAdapter.ViewAllFarmViewHolder>() {
    private var farmList = ArrayList<List<Data>>()

    init {
        if(farmItemList?.isNotEmpty() == true){
            farmList.addAll(farmItemList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): ViewAllFarmViewHolder {
        return ViewAllFarmViewHolder(
            CardViewAllFarmsBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewAllFarmViewHolder, position: Int) {
        holder.binding.item = farmList[position]
        holder.binding.isOldFarms = isOldFarms
        holder.binding.isCustomerFarm = isCustomerFarm

        holder.binding.headerLayout.setOnClickListener {
            headerListener.invoke(farmList[position])
        }

        holder.binding.contentLayout.setOnClickListener {
            contentListener.invoke(farmList[position])
        }

        holder.binding.footerLayout.setOnClickListener {
            footerListener.invoke(farmList[position])
        }

        holder.binding.txtAddFarm.setTextColor(holder.binding.root.context.resources.getColor(R.color.orange))
        if(/*isOldFarms &&*/ isCustomerFarm &&  farmList[position].size == 1 && farmList[position][0].is_crop_cycle_completed!!){
            if(farmList[position][0].harvested_quantity.isNullOrEmpty()){
                holder.binding.txtAddFarm.text = holder.binding.root.context.getString(R.string.give_your_crop_details)
            }else{
                holder.binding.txtAddFarm.setTextColor(holder.binding.root.context.resources.getColor(R.color.black))
                holder.binding.txtAddFarm.text = holder.binding.root.context.getString(R.string.farm_crop) + " " + farmList[position][0].harvested_quantity + " " + farmList[position][0].harvested_quantity_unit
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
        return farmList.size
    }

    inner class ViewAllFarmViewHolder(var binding: CardViewAllFarmsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
