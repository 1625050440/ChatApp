package com.example.enpit_p13.chatapp.messages

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import com.example.enpit_p13.chatapp.Message_all
import com.example.enpit_p13.chatapp.quetion.TemplateQuestionnaireActivity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_messager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.toast

class DeleteConfirmDialog : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context ?: return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(context).apply {
            setMessage("本当に削除しますか？")
            setNegativeButton("はい") { dialog,which ->
                context.toast("ルームが削除されました。")
                sendData("","",false) //reset data
                FirebaseDatabase.getInstance().getReference("/Room/${FirebaseAuth.getInstance().uid.toString()}").removeValue()
                FirebaseDatabase.getInstance().getReference("/messages/")
                        .addValueEventListener(object :ValueEventListener{
                            override fun onDataChange(p0: DataSnapshot) {
                               p0.children.forEach {
                                   val delete_room = it?.getValue(Message_all::class.java)
                                   if(delete_room?.Uid == FirebaseAuth.getInstance().uid.toString())
                                   {
                                       Log.d("Check_error","${p0.key}")
                                       FirebaseDatabase.getInstance().getReference("/messages/${it?.key}").removeValue()
                                   }
                               }
                            }
                            override fun onCancelled(p0: DatabaseError) {

                            }
                        })
                val intent = Intent(context, TemplateQuestionnaireActivity::class.java)
                startActivity(intent)
            }
            setPositiveButton("いいえ"){dialog,which->

            }
        }
        return builder.create()
    }

    private fun sendData(title:String,message:String,check:Boolean) {
        val uid = FirebaseAuth.getInstance().uid.toString()
        val reference = FirebaseDatabase.getInstance().getReference("/Room_Chat/$uid/")
        reference.setValue(Room_chat_messager(message, title, uid,check))
    }
}