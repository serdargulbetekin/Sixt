package com.example.sixth.modules.cars


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sixth.R
import com.example.sixth.databinding.RowCarBinding

class CarsAdapter(private val onClick: (CarsItem) -> Unit) :
    RecyclerView.Adapter<CarsViewHolder>() {

    private val itemList = mutableListOf<CarsItem>()

    fun updateData(itemList: List<CarsItem>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarsViewHolder {
        return CarsViewHolder(
            RowCarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: CarsViewHolder, position: Int) {
        holder.bind(itemList[position], onClick)
    }
}

class CarsViewHolder(private val binding: RowCarBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: CarsItem, onClick: (CarsItem) -> Unit) {
        binding.apply {

            Glide.with(binding.root.context)
                .load(item.carImageUrl)
                .centerCrop()
                .placeholder(R.drawable.icon_car)
                .into(binding.imageView)

            textViewName.text = item.name
            textViewModelNameMake.text = item.modelName + " - " + item.make
            textViewFuelType.text = item.fuelType
            textViewFuelLevel.text = item.fuelLevel
            textViewCleanliness.text = item.innerCleanliness
            linearLayoutContainer.setOnClickListener {
                onClick.invoke(item)
            }

        }

    }
}