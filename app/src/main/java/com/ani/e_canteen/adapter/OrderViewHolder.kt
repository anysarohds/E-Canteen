package com.ani.e_canteen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ani.e_canteen.R
import com.ani.e_canteen.database.entitas.Note
import com.ani.e_canteen.model.MakananModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class OrderViewHolder(
    private val notesList: ArrayList<Note>,
    private val context: Context
) : RecyclerView.Adapter<OrderViewHolder.ViewHolder>() {

    //database
    lateinit var firestore: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    var userId: String? = null
    private var dialog: Dialog? = null


    interface Dialog {
        fun onClick(position: Int)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    fun setdata(list: List<Note>){
        notesList.clear()
        notesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var nama: TextView
        internal var nama_kantin: TextView


        init {
            nama = view.findViewById(R.id.nama)
            nama_kantin = view.findViewById(R.id.nama_kantin)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cart, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val note = notesList[position]
        holder.nama.text = note.nama
        holder.nama_kantin.text = note.nama_kantin
        holder.itemView.setOnClickListener {
            if(dialog != null){
                dialog!!.onClick(holder.layoutPosition)
            }
        }
    }

}