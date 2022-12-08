package agstack.gramophone.ui.notification


import agstack.gramophone.BR
import agstack.gramophone.databinding.ItemFollowBinding
import agstack.gramophone.databinding.ItemNotificationBinding
import agstack.gramophone.ui.home.view.fragments.community.model.likes.DataX
import agstack.gramophone.ui.notification.model.Data
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class NotificationsAdapter(private val dataList: List<Data>) :
    RecyclerView.Adapter<NotificationsAdapter.DeveloperViewHolder>() {
    var notificationClicked: ((Data) -> Unit)? = null
    lateinit var setImage: SetImage

    interface SetImage {
        fun onImageSet(imageUrl: String, iv: ImageView)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DeveloperViewHolder {

        return DeveloperViewHolder(
            ItemNotificationBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        var data: Data = dataList[position]!!
        holder.binding.setVariable(BR.model, data)
        val mBinding = holder.binding

    }

    override fun getItemCount(): Int {
        return dataList.size ?: 0
    }

    inner class DeveloperViewHolder(var binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)
}