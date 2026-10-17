package com.example.bmbautos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CarAdapter(private val carList: List<Car>) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCar: ImageView = itemView.findViewById(R.id.imgCar)
        val tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        val tvCarPrice: TextView = itemView.findViewById(R.id.tvCarPrice)
        val tvCarMileage: TextView = itemView.findViewById(R.id.tvCarMileage)
        val tvCarStatus: TextView = itemView.findViewById(R.id.tvCarStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]

        holder.tvCarName.text = "${car.year} ${car.make} ${car.model}"
        holder.tvCarPrice.text = car.price
        holder.tvCarMileage.text = "Mileage: ${car.mileage}"
        holder.tvCarStatus.text = car.status

        // Load image from URL using Glide
        Glide.with(holder.itemView.context)
            .load(car.image)
            .placeholder(android.R.drawable.ic_menu_gallery) // Shows while loading
            .into(holder.imgCar)
    }

    override fun getItemCount() = carList.size
}