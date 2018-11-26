package com.example.enpit_p13.chatapp.quetion

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity

class ThanksDialog:DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context ?: return super.onCreateDialog(savedInstanceState)

        val builder = AlertDialog.Builder(context).apply {
            setMessage("ご協力ありがとうございました。")
            setPositiveButton("ok"){dialog,which->
                val intent = Intent(context,LatestMessagesActivity::class.java)
                startActivity(intent)
            }
        }
        return builder.create()
    }
}