package com.example.enpit_p13.chatapp.messages

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment

class Room_change:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context ?: return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(context).apply {
            setMessage("このルームは終了されました。チャットページへ移動します。")
            setNegativeButton("はい") { dialog,which ->
                val intent = Intent(context, LatestMessagesActivity::class.java)
                startActivity(intent)
            }


        }
        return builder.show()
    }


}