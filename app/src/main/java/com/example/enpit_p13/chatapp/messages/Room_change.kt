package com.example.enpit_p13.chatapp.messages

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.example.enpit_p13.chatapp.models.Check_online
import com.example.enpit_p13.chatapp.room_chat.Room_chat_from_ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Room_change:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context ?: return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(context).apply {
            setMessage("このルームへ移動しますか？")
            setNegativeButton("はい") { dialog,which ->
                val intent = Intent(context, Room_chat_from_ListView::class.java)
                startActivity(intent)
            }
            setPositiveButton("いいえ"){dialog,which->
               FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
                       .addValueEventListener(object :ValueEventListener{
                           override fun onDataChange(p0: DataSnapshot) {
                               val data = p0.getValue(Check_online::class.java)
                                FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
                                        .setValue(Check_online("Chat_all",data?.username.toString(),false))
                           }

                           override fun onCancelled(p0: DatabaseError) {

                           }
                       })
            }
        }
        return builder.show()
    }


}