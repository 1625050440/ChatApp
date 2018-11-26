package com.example.enpit_p13.chatapp.messages

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast
import com.example.enpit_p13.chatapp.quetion.TemplateQuestionnaireActivity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_messager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_latest_messages.*
import org.jetbrains.anko.toast

class DeleteConfirmDialog : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context ?: return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(context).apply {
            setMessage("本当に削除しますか？")
            setNegativeButton("はい") { dialog,which ->
                context.toast("ルームが削除されました。")
                sendData("","",false) //reset data
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