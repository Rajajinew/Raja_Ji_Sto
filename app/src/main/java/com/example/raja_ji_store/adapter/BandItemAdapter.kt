package com.example.raja_ji_store.adapter
//
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Import Glide for loading images
import com.example.raja_ji_store.DetailActivity // Adjust package name
import com.example.raja_ji_store.R
import com.example.raja_ji_store.model.Banddata
class BandItemAdapter(var nList: List<Banddata>, private val context: Context) :
    RecyclerView.Adapter<BandItemAdapter.Bandholder>() {

    inner class Bandholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bandpic: ImageView = itemView.findViewById(R.id.bandpicimage)
        val banname: TextView = itemView.findViewById(R.id.namee)
        val bandaddres: TextView = itemView.findViewById(R.id.addres)
        val bandrating: TextView = itemView.findViewById(R.id.ratting)
        val price: TextView = itemView.findViewById(R.id.pricee)
        val likebutton: ImageButton = itemView.findViewById(R.id.likebutton)
    }

    fun setFilteredList(nList: List<Banddata>) {
        this.nList = nList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Bandholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.banditem, parent, false)
        return Bandholder(view)
    }

    override fun getItemCount(): Int {
        return nList.size
    }

    override fun onBindViewHolder(holder: Bandholder, position: Int) {
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
