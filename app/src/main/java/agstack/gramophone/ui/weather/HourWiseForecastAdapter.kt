package agstack.gramophone.ui.weather


import agstack.gramophone.databinding.ItemHourWiseForecastBinding
import agstack.gramophone.ui.weather.model.Day
import agstack.gramophone.ui.weather.model.Time
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Singleton

@Singleton
class HourWiseForecastAdapter(private val hourWiseForecastList: List<Time>) :
    RecyclerView.Adapter<HourWiseForecastAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        return CustomViewHolder(
            ItemHourWiseForecastBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.binding.model = hourWiseForecastList[position]
    }

    override fun getItemCount(): Int {
        return hourWiseForecastList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CustomViewHolder(var binding: ItemHourWiseForecastBinding) :
        RecyclerView.ViewHolder(binding.root)
}