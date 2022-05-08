package agstack.gramophone.splash.adapter

import agstack.gramophone.R
import agstack.gramophone.splash.model.AppConfig
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(private val users: ArrayList<AppConfig>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false))

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        //holder.bind(users[position])
    }

    fun addUsers(users: List<AppConfig>) {
        this.users.apply {
            clear()
            addAll(users)
        }

    }
}