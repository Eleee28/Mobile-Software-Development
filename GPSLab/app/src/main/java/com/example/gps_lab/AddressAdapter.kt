package com.example.gps_lab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AddressAdapter(private val addressList: MutableList<String>) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

        class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val addressText: TextView = itemView.findViewById(R.id.addressText)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.address_item, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.addressText.text = addressList[position]
    }

    override fun getItemCount(): Int = addressList.size

    fun addAddress(address: String) {
        addressList.add(address)
        notifyDataSetChanged()
    }
}