package com.example.bmbautos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyGarageAdapter(
    private val carList: List<Car>,
    private val onBookServiceClick: (Car) -> Unit
) : RecyclerView.Adapter<MyGarageAdapter.GarageViewHolder>() {

    class GarageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCar: ImageView = itemView.findViewById(R.id.imgCar)
        val tvStockNumber: TextView = itemView.findViewById(R.id.tvStockNumber)
        val tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        val tvCarPrice: TextView = itemView.findViewById(R.id.tvCarPrice)
        val tvCarColor: TextView = itemView.findViewById(R.id.tvCarColor)
        val tvCarStock: TextView = itemView.findViewById(R.id.tvCarStock)
        val tvCarMileage: TextView = itemView.findViewById(R.id.tvCarMileage)
        val tvCarStatus: TextView = itemView.findViewById(R.id.tvCarStatus)
        val btnBuy: Button = itemView.findViewById(R.id.btnBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return GarageViewHolder(view)
    }

    override fun onBindViewHolder(holder: GarageViewHolder, position: Int) {
        val car = carList[position]

        holder.tvStockNumber.text = car.stock_number
        holder.tvCarName.text = "${car.year} ${car.make} ${car.model}"
        holder.tvCarPrice.text = "Purchased"
        holder.tvCarColor.text = "🎨 Color: ${car.color}"
        holder.tvCarStock.text = "📦 Stock: ${car.stock_quantity}"
        holder.tvCarMileage.text = String.format("📍 Mileage: %s", car.mileage)
        holder.tvCarStatus.text = "Owned"

        Glide.with(holder.itemView.context)
            .load(car.image)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.imgCar)

        // Repurpose Buy button to "Book Service"
        holder.btnBuy.text = "Book Service"
        holder.btnBuy.setOnClickListener {
            onBookServiceClick(car)
        }
    }

    override fun getItemCount() = carList.size
}
