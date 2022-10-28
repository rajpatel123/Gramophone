package agstack.gramophone.ui.search.adapter

import agstack.gramophone.databinding.*
import agstack.gramophone.ui.search.model.Data
import agstack.gramophone.ui.search.view.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnix.xtension.extensions.toCamelCase

class SearchResultAdapter(
    val list: List<Data>,
    private val listener: (String) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val empty = 1000
    private val products = 1001
    private val crops = 1002
    private val profiles = 1003
    private val cropProblems = 1004
    private val companies = 1005
    private val productCategory = 1006
    private val cropCategory = 1007
    private val posts = 1008

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        when (viewType) {
            empty -> return EmptyViewHolder(ItemHomeEmptyBinding.inflate(inflater))
            products -> return ProductsViewHolder(ItemSearchProductsBinding.inflate(inflater))
            crops -> return CropsViewHolder(ItemSearchCropsBinding.inflate(inflater))
            profiles -> return ProfilesViewHolder(ItemSearchProfilesBinding.inflate(inflater))
            cropProblems -> return CropProblemsViewHolder(ItemSearchCropProblemsBinding.inflate(inflater))
            companies -> return CompaniesViewHolder(ItemSearchCompaniesBinding.inflate(inflater))
            productCategory -> return ProductCategoryViewHolder(ItemSearchProductCategoryBinding.inflate(inflater))
            cropCategory -> return CropCategoryViewHolder(ItemSearchCropCategoryBinding.inflate(inflater))
            posts -> return PostsViewHolder(ItemSearchPostsBinding.inflate(inflater))
        }
        return EmptyViewHolder(ItemHomeEmptyBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductsViewHolder -> {
                holder.binding.txtTile.text = list[position].type?.replace('_', ' ')?.toCamelCase()?.trim()
                val productList = list[position].items
                holder.binding.recyclerViewProducts.adapter = ProductsAdapter(productList){
                    listener.invoke(it.toString())
                }
                holder.binding.viewAllProducts.setOnClickListener {
                    openActivity(
                        holder.itemView.context,
                        ViewAllSearchProductsActivity::class.java,
                        Bundle().apply {
                            putParcelable("dataList", list[position])
                        })
                }
            }
            is CropsViewHolder ->{
                holder.binding.txtTile.text = list[position].type?.replace('_', ' ')?.toCamelCase()?.trim()
                val dataList = list[position].items
                holder.binding.rvCrops.adapter = CropsAdapter(dataList){
                    listener.invoke(it)
                }
                holder.binding.viewAllCrops.setOnClickListener {
                    openActivity(
                        holder.itemView.context,
                        ViewAllSearchCropsActivity::class.java,
                        Bundle().apply {
                            putParcelable("dataList", list[position])
                        })
                }
            }
            is ProfilesViewHolder -> {
                holder.binding.txtHeaderProfile.text = list[position].type?.replace('_', ' ')?.toCamelCase()?.trim()
                val profileList = list[position].items
                holder.binding.recyclerViewProfiles.adapter = ProfilesAdapter(profileList){
                    listener.invoke(it)
                }
                holder.binding.viewAllProfiles.setOnClickListener {
                    openActivity(
                        holder.itemView.context,
                        ViewAllSearchProfilesActivity::class.java,
                        Bundle().apply {
                            putParcelable("dataList", list[position])
                        })
                }
            }
            is CropProblemsViewHolder ->{
                holder.binding.txtTitle.text = list[position].type?.replace('_', ' ')?.toCamelCase()?.trim()
                val dataList = list[position].items
                holder.binding.rvCropProblems.adapter = CropProblemsAdapter(dataList){
                    listener.invoke(it)
                }
                holder.binding.viewAll.setOnClickListener {
                    openActivity(
                        holder.itemView.context,
                        ViewAllSearchCropProblemsActivity::class.java,
                        Bundle().apply {
                            putParcelable("dataList", list[position])
                        })
                }
            }
            is CompaniesViewHolder-> {
                holder.binding.txtTitle.text = list[position].type?.replace('_', ' ')?.toCamelCase()?.trim()
                val dataList = list[position].items
                holder.binding.rvCompanies.adapter = CompaniesAdapter(dataList){
                    listener.invoke(it)
                }
                holder.binding.viewAll.setOnClickListener {
                    openActivity(
                        holder.itemView.context,
                        ViewAllSearchCompaniesActivity::class.java,
                        Bundle().apply {
                            putParcelable("dataList", list[position])
                        })
                }
            }
            is ProductCategoryViewHolder ->{
                holder.binding.tvTitleProductCategory.text = list[position].type?.replace('_', ' ')?.toCamelCase()?.trim()
                val dataList = list[position].items
                holder.binding.rvProductCategory.adapter = ProductCategoryAdapter(dataList){
                    listener.invoke(it)
                }
                holder.binding.viewAll.setOnClickListener {
                    openActivity(
                        holder.itemView.context,
                        ViewAllSearchProductCategoriesActivity::class.java,
                        Bundle().apply {
                            putParcelable("dataList", list[position])
                        })
                }
            }
            is CropCategoryViewHolder-> {
                holder.binding.tvTitle.text = list[position].type?.replace('_', ' ')?.toCamelCase()?.trim()
                val dataList = list[position].items
                holder.binding.rvCropCategory.adapter = CropsCategoriesAdapter(dataList){
                    listener.invoke(it)
                }
                holder.binding.viewAll.setOnClickListener {
                    openActivity(
                        holder.itemView.context,
                        ViewAllSearchCropCategoriesActivity::class.java,
                        Bundle().apply {
                            putParcelable("dataList", list[position])
                        })
                }
            }
            is PostsViewHolder -> {
                holder.binding.txtTile.text = list[position].type?.replace('_', ' ')?.toCamelCase()?.trim()
                var postList = list[position].items
                if(postList.size > 3){
                    postList = postList.subList(0, 3)
                }
                holder.binding.recyclerViewPosts.adapter = PostsAdapter(postList){
                    listener.invoke(it)
                }
                holder.binding.viewAllPosts.setOnClickListener {
                    openActivity(
                        holder.itemView.context,
                        ViewAllSearchPostsActivity::class.java,
                        Bundle().apply {
                            putParcelable("dataList", list[position])
                        })
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        when (list[position].type?.lowercase()) {
            "products".lowercase() -> return products
            "crops".lowercase() -> return crops
            "profiles".lowercase() -> return profiles
            "crop_problems".lowercase() -> return cropProblems
            "companies".lowercase() -> return companies
            "product_categories".lowercase() -> return productCategory
            "crop_categories".lowercase() -> return cropCategory
            "posts".lowercase() -> return posts
        }
        return empty
    }
    override fun getItemId(position: Int): Long {
        when (list[position].type?.lowercase()) {
            "products".lowercase() -> return products.toLong()
            "crops".lowercase() -> return crops.toLong()
            "profiles".lowercase() -> return profiles.toLong()
            "crop_problems".lowercase() -> return cropProblems.toLong()
            "companies".lowercase() -> return companies.toLong()
            "product_categories".lowercase() -> return productCategory.toLong()
            "crop_categories".lowercase() -> return cropCategory.toLong()
            "posts".lowercase() -> return posts.toLong()
        }
        return empty.toLong()
    }

    fun <T> openActivity(context: Context, cls: Class<T>, extras: Bundle?) {
        Intent(context, cls).apply {
            if (extras != null)
                putExtras(extras)
            context.startActivity(this)
        }
    }

    inner class EmptyViewHolder(var binding: ItemHomeEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ProductsViewHolder(var binding: ItemSearchProductsBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CropsViewHolder(var binding: ItemSearchCropsBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ProfilesViewHolder(var binding: ItemSearchProfilesBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CropProblemsViewHolder(var binding: ItemSearchCropProblemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CompaniesViewHolder(var binding: ItemSearchCompaniesBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ProductCategoryViewHolder(var binding: ItemSearchProductCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CropCategoryViewHolder(var binding: ItemSearchCropCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PostsViewHolder(var binding: ItemSearchPostsBinding) :
        RecyclerView.ViewHolder(binding.root)
}