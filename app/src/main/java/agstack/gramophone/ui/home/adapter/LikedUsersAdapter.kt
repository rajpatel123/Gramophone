package agstack.gramophone.ui.home.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFollowBinding
import agstack.gramophone.ui.home.view.fragments.community.model.likes.DataX
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class LikedUsersAdapter(private val dataList: List<DataX>) :
    RecyclerView.Adapter<LikedUsersAdapter.DeveloperViewHolder>() {
    var followClicked: ((DataX) -> Unit)? = null
    lateinit var setImage: SetImage

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemFollowBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var likedUsers: DataX = dataList[position]!!
        holder.binding.setVariable(BR.model, likedUsers)
        val mBinding = holder.binding

        if (likedUsers.author!=null && likedUsers.author.photoUrl!=null)
        setImage.onImageSet(likedUsers.author.photoUrl, holder.binding.imageView
        )
        mBinding.tvFollow.setOnClickListener {
            followClicked?.invoke(likedUsers)
        }



    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root)
}