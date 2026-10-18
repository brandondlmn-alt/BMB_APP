package com.example.bmbautos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PurchasesAdapter(private val purchaseList: List<Purchase>) : RecyclerView.Adapter<PurchasesAdapter.PurchaseViewHolder>() {

    class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCar: ImageView = itemView.findViewById(R.id.imgCar)
        val tvStockNumber: TextView = itemView.findViewById(R.id.tvStockNumber)
        val tvCarName: TextView = itemView.findViewById(R.id.tvCarName)
        val tvCarPrice: TextView = itemView.findViewById(R.id.tvCarPrice)
        val tvCarMileage: TextView = itemView.findViewById(R.id.tvCarMileage)
        val tvCarStatus: TextView = itemView.findViewById(R.id.tvCarStatus)
        val btnBuy: Button = itemView.findViewById(R.id.btnBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return PurchaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val purchase = purchaseList[position]

        holder.tvStockNumber.text = purchase.stock_number
        holder.tvCarName.text = "${purchase.year} ${purchase.make} ${purchase.model}"
        holder.tvCarPrice.text = String.format("Paid: %s", purchase.amount)
        holder.tvCarMileage.text = String.format("Date: %s", purchase.created_at.substring(0, 10))
        holder.tvCarStatus.text = "Purchased"
        holder.tvCarStatus.setTextColor(android.graphics.Color.parseColor("#059669"))

        // Hide the buy button in history
        holder.btnBuy.visibility = View.GONE

        Glide.with(holder.itemView.context)
            .load(purchase.image)
            .into(holder.imgCar)
    }

    override fun getItemCount() = purchaseList.size
}