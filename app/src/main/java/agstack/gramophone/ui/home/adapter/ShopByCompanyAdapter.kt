package agstack.gramophone.ui.home.adapter


import agstack.gramophone.databinding.ItemShopByCompanyBinding
import agstack.gramophone.ui.home.view.fragments.market.model.CompanyData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ShopByCompanyAdapter(private val companyList: List<CompanyData>?,
                           private val listener: (String) -> Unit) :
    RecyclerView.Adapter<ShopByCompanyAdapter.CustomViewHolder>() {
    var onItemClicked: ((id: String) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemShopByCompanyBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, i: Int) {
        holder.binding.model = companyList?.get(i)
        holder.binding.flCompany.setOnClickListener {
            /*onItemClicked?.invoke(companyList?.get(i)?.companyId.toString())*/
            listener.invoke(companyList?.get(i)?.companyId.toString())
        }
    }

    override fun getItemCount(): Int {
        return companyList?.size!!
    }

    inner class CustomViewHolder(var binding: ItemShopByCompanyBinding) :
        RecyclerView.ViewHolder(binding.root)
}