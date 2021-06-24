package com.ani.e_canteen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ani.e_canteen.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.HashMap

class ChatActivity : AppCompatActivity(),AnkoLogger {
    val adapter = GroupAdapter<ViewHolder>()
    lateinit var auth: FirebaseAuth
    var userID: String? = null
    var dataFrom: String? = null
    var uid_kantin :String? = null
    var nama_kantin :String? = null
    lateinit var reference: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val bundle: Bundle? = intent.extras
        uid_kantin = bundle!!.getString("uid_kantin")
        nama_kantin = bundle.getString("nama_kantin")
        info { "dinda $uid_kantin" }

        auth = FirebaseAuth.getInstance()
        userID = auth.currentUser!!.uid

        swiperefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.colorAccent)
        )
        recyclerview_chat_log.adapter = adapter
        reference = FirebaseDatabase.getInstance()

        ambildata()
        txt_namakantin.text = nama_kantin.toString()

        btn_send.setOnClickListener {
            performSendMessage()
        }

        imageView4.setOnClickListener {
            finish()
        }


    }

    private fun ambildata() {
        swiperefresh.isEnabled = true
        swiperefresh.isRefreshing = true

        val fromId = FirebaseAuth.getInstance().uid ?: return
        val toId = uid_kantin
        val ref = FirebaseDatabase.getInstance().getReference("/chat/user-messages/$fromId/$toId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (!dataSnapshot.hasChildren()) {
                    swiperefresh.isRefreshing = false
                    swiperefresh.isEnabled = false
                }
            }
        })

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                dataSnapshot.getValue(ChatMessage::class.java)?.let {
                    if (it.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatToItem(it.text, it.timestamp))

                    } else {
                        adapter.add(ChatFromItem(it.text, it.timestamp))

                    }
                }
                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
                swiperefresh.isRefreshing = false
                swiperefresh.isEnabled = false
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

        })
    }

    private fun performSendMessage() {
        val text = edt_send.text.toString()
        if (text.isEmpty()) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val fromId = FirebaseAuth.getInstance().uid ?: return
        val toId = uid_kantin.toString()

        val notificationMessage: MutableMap<String, Any> =
            HashMap()
        notificationMessage["message"] = text
        notificationMessage["from"] = userID!!

        val reference = FirebaseDatabase.getInstance().getReference("/chat/user-messages/$userID/$uid_kantin").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/chat/user-messages/$uid_kantin/$userID").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                edt_send.text.clear()
                recyclerview_chat_log.smoothScrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)


        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/chat/statusnotif/$uid_kantin")
        latestMessageRef.child("status").setValue(true)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/chat/latest-messages/$uid_kantin/$userID")
        latestMessageToRef.setValue(chatMessage)
    }
}


class ChatFromItem(val text: String, val timestamp: Long) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {


        viewHolder.itemView.txt_chatkantin.text = text

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

}


class ChatToItem(val text: String, val timestamp: Long) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txt_chatuser.text = text

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}
