package agstack.gramophone.ui.followings


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFollowBinding
import agstack.gramophone.databinding.ItemFollowerBinding
import agstack.gramophone.ui.followings.model.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class FollowsAdapter(private val dataList: List<Data>) :
    RecyclerView.Adapter<FollowsAdapter.DeveloperViewHolder>() {
    var followClicked: ((Data) -> Unit)? = null
    var profileClicked: ((Data) -> Unit)? = null
    lateinit var setImage: SetImage

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemFollowerBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var user: Data = dataList[position]!!
        holder.binding.setVariable(BR.model, user)
        val mBinding = holder.binding

        if (user!=null && user.photoUrl!=null)
        setImage.onImageSet(user.photoUrl, holder.binding.imageView
        )
        mBinding.tvFollow.setOnClickListener {
            followClicked?.invoke(user)
        }

        mBinding.imageView.setOnClickListener{
            profileClicked?.invoke(user)
        }

        mBinding.name.setOnClickListener{
            profileClicked?.invoke(user)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemFollowerBinding) :
        RecyclerView.ViewHolder(binding.root)
}