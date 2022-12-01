package agstack.gramophone.ui.farm.adapter

import agstack.gramophone.R
import agstack.gramophone.databinding.CardFarmsBinding
import agstack.gramophone.ui.farm.model.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FarmAdapter(
    farmItemList: List<List<Data>>?,
    private val headerListener: (List<Data>) -> Unit,
    private val contentListener: (List<Data>) -> Unit,
    private val footerListener: (List<Data>) -> Unit
) : RecyclerView.Adapter<FarmAdapter.FarmViewHolder>() {
    private var farmList = ArrayList<List<Data>>()
    init {
        if(farmItemList?.isNotEmpty() == true){
            farmList.addAll(farmItemList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): FarmViewHolder {
        return FarmViewHolder(
            CardFarmsBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: FarmViewHolder, position: Int) {
        holder.binding.item = farmList[position]

        holder.binding.contentLayoutLL.setOnClickListener {
            headerListener.invoke(farmList[position])
        }

        holder.binding.contentLayoutLL.setOnClickListener {
            contentListener.invoke(farmList[position])
        }

        holder.binding.txtAddFarm.setOnClickListener {
            footerListener.invoke(farmList[position])
        }

        holder.binding.txtAddedFarm.setOnClickListener {
            contentListener.invoke(farmList[position])
        }
    }

    override fun getItemCount(): Int {
        return farmList.size
    }

    inner class FarmViewHolder(var binding: CardFarmsBinding) :
        RecyclerView.ViewHolder(binding.root)
}

