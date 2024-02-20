package com.sbd.gbd.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sbd.gbd.Model.BusinessModel
import com.sbd.gbd.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class SearchFirebaseAdaptor (itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val itemNameTextView: TextView = itemView.findViewById(R.id.product_type1)

    fun bind(item: BusinessModel) {
        // Bind data to views
        itemNameTextView.text = item.Businessname
    }
}

// Adapter for the RecyclerView
class ItemAdapter(options: FirebaseRecyclerOptions<BusinessModel>) :
    FirebaseRecyclerAdapter<BusinessModel, SearchFirebaseAdaptor>(options) {

    override fun onBindViewHolder(holder: SearchFirebaseAdaptor, position: Int, model: BusinessModel) {
        // Bind data to ViewHolder
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFirebaseAdaptor {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_items_layout, parent, false)
        return SearchFirebaseAdaptor(view)
    }
}