package com.ani.e_canteen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ani.e_canteen.R
import com.ani.e_canteen.database.entitas.Note
import com.ani.e_canteen.model.MakananModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class SelesaiViewHolder(
    private val notesList: MutableList<MakananModels>,
    private val context: Context
) : RecyclerView.Adapter<SelesaiViewHolder.ViewHolder>() {

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

    fun setdata(list: List<Note>) {
        notesList.clear()
        notesList.addAll(notesList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var nama: TextView
        internal var jumlah : TextView
        internal var foto: ImageView


        init {
            nama = view.findViewById(R.id.txt_namamakanan)
            foto = view.findViewById(R.id.img_foto)
            jumlah = view.findViewById(R.id.txt_jumlah)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_selesai, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        holder.jumlah.text = note.jumlah.toString()
        holder.nama.text = note.nama
        Picasso.get().load(note.foto_makanan).centerCrop()
            .fit().into(holder.foto)
        holder.itemView.setOnClickListener {
        }






    }


}



