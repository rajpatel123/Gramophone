package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.databinding.CardViewAllFarmsBinding
import agstack.gramophone.ui.farm.model.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViewAllFarmsAdapter(
    farmItemList: List<List<Data>>?,
    private val headerListener: (List<Data>) -> Unit,
    private val contentListener: (List<Data>) -> Unit,
    private val footerListener: (List<Data>) -> Unit,
    private val isOldFarms : Boolean = false,
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

    override fun onBindViewHolder(holder: ViewAllFarmViewHolder, position: Int) {
        holder.binding.item = farmList[position]
        holder.binding.isOldFarms = isOldFarms

        holder.binding.headerLayout.setOnClickListener {
            headerListener.invoke(farmList[position])
        }

        holder.binding.contentLayout.setOnClickListener {
            contentListener.invoke(farmList[position])
        }

        holder.binding.footerLayout.setOnClickListener {
            footerListener.invoke(farmList[position])
        }
    }

    override fun getItemCount(): Int {
        return farmList.size
    }

    inner class ViewAllFarmViewHolder(var binding: CardViewAllFarmsBinding) :
        RecyclerView.ViewHolder(binding.root)
}
