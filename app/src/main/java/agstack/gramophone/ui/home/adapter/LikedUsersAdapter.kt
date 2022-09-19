package agstack.gramophone.ui.home.adapter


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFollowBinding
import agstack.gramophone.ui.home.view.fragments.community.model.likes.LikedUsers
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class LikedUsersAdapter(private val dataList: ArrayList<LikedUsers>) :
    RecyclerView.Adapter<LikedUsersAdapter.DeveloperViewHolder>() {
    var followClicked: ((LikedUsers) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemFollowBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var likedUsers: LikedUsers = dataList[position]!!
        holder.binding.setVariable(BR.model, likedUsers)
        val mBinding = holder.binding

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