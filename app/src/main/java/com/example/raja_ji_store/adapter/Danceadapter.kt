package com.example.raja_ji_store.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.raja_ji_store.DetailActivity
import com.example.raja_ji_store.model.Dancerdata
import com.example.raja_ji_store.R


class danceadapter(var nList:List<Dancerdata>,private val context: Context): RecyclerView.Adapter<danceadapter.DancerItemHolder>() {
    inner class DancerItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val bandpic: ImageView =itemView.findViewById(R.id.bandpicimage)
        val banname: TextView =itemView.findViewById(R.id.namee)
        val bandaddres: TextView =itemView.findViewById(R.id.addres)
        val bandrating: TextView =itemView.findViewById(R.id.ratting)
        val price: TextView =itemView.findViewById(R.id.pricee)
    }

    fun setFilteredList(nList: List<Dancerdata>){
        this.nList=nList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DancerItemHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.danceitem,parent,false)

        return  DancerItemHolder(view)
    }

    override fun getItemCount(): Int {
        return nList.size
    }

    override fun onBindViewHolder(holder: DancerItemHolder, position: Int) {
        val carbetData=nList[position]
        Glide.with(context)
            .load(carbetData.image) // Assuming image is a URL string
            .placeholder(R.drawable.like) // Add a placeholder image
            .error(R.drawable.banner1) // Add an error image
            .into(holder.bandpic)
        holder.banname.text=carbetData.companyname
        holder.bandaddres.text=carbetData.companyAdress
        holder.bandrating.text=carbetData.rating
        holder.price.text=carbetData.price
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("location", carbetData.companyAdress);
                putExtra("price", carbetData.price);
                putExtra("name" ,carbetData.companyname)
                putExtra("rating",carbetData.rating)
                putExtra("image", carbetData.image)

            }
            context.startActivity(intent);
        }
    }

}

