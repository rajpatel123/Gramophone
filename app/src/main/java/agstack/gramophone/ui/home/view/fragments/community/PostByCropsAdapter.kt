package agstack.gramophone.ui.home.view.fragments.community

import agstack.gramophone.databinding.ItemPostCropBinding
import agstack.gramophone.databinding.SubItemCropBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CropData
import agstack.gramophone.ui.search.model.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PostByCropsAdapter(
    val list: ArrayList<CropData>,
    private val listener: (CropData) -> Unit,
) : RecyclerView.Adapter<PostByCropsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemPostCropBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cropData = list[position]
        holder.binding.item = cropData

        holder.binding.cropContainer.setOnClickListener {
            listener.invoke(cropData)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(var binding: ItemPostCropBinding) :
        RecyclerView.ViewHolder(binding.root)
}