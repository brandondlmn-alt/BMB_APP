package com.example.bmbautos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookingAdapter(private val bookingList: List<Booking>) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvServiceType: TextView = itemView.findViewById(R.id.tvServiceType)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvVehicle: TextView = itemView.findViewById(R.id.tvVehicle)
        val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookingList[position]

        holder.tvServiceType.text = booking.service_type
        holder.tvStatus.text = booking.status.uppercase()
        holder.tvVehicle.text = "Vehicle: ${booking.vehicle_info}"

        // Format the time (e.g., "14:30:00" -> "14:30")
        val shortTime = if (booking.booking_time.length >= 5) booking.booking_time.substring(0, 5) else booking.booking_time
        holder.tvDateTime.text = "Date: ${booking.booking_date} at $shortTime"
    }

    override fun getItemCount() = bookingList.size
}