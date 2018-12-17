package com.example.enpit_p13.chatapp.quetion

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_Activity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_messager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_qusetiontemp2_from_chat_all.*

class Qusetiontemp2Activity_from_chat_all : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qusetiontemp2_from_chat_all)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        var  temptext = ""
        temp_create_button.setOnClickListener(){
            temptext = "私と子供との関係は、以下のような状況です。\n"
            if(t1_text.text != ""){
                temptext = temptext + "・" + t1_text.text + "\n"

            }
            if (t2_text.text != ""){
                temptext = temptext + "・" + t2_text.text + "\n"
            }
            if (t3_text.text != ""){
                temptext = temptext + "・" + t3_text.text + "\n"
            }
            if (t4_text.text != ""){
                temptext = temptext + "現在の状況について" + t4_text.text + "\n"
            }
            temptext += "そこで質問なのですが、どうやって子供と打ち解け現在の状況を脱する事ができますか？\nみなさんの意見をお聞きしたいです。"
            editTemplate.setText(temptext)
            editTemplate.isEnabled = true
            send_button.visibility = View.VISIBLE
            send_button.isClickable = true

        }

        send_button.setOnClickListener(){
            temptext = editTemplate.text.toString()
            Log.d("sendtext",temptext)
            send(temptext)
        }
    }
    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun create_temp(){
        t1_radio1.setOnClickListener(){
            t1_text.text="子供と会話できています。"
        }
        t1_radio2.setOnClickListener(){
            t1_text.text="子供と会話ができていません。"
        }
        t1_radio3.setOnClickListener(){
            t1_text.text = ""
        }

        t2_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val spinner = parent as? Spinner
                        val item = spinner?.selectedItemPosition
                        item?.let{
                            when (it) {
                                0 -> t2_text.text = ""
                                1 -> t2_text.text = "子供は、外出します。"
                                2 -> t2_text.text = "子供は、家から出ようとしません。"
                                3 -> t2_text.text = "子供は、部屋に引きこもっています。"
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

        t3_radio1.setOnClickListener(){
            t3_text.text="子供からの暴力を振るわれています。"
        }
        t3_radio2.setOnClickListener(){
            t3_text.text="子供からの暴力は振るわれていません。"
        }
        t3_radio3.setOnClickListener(){
            t3_text.text = ""
        }

        t4_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val spinner = parent as? Spinner
                        val item = spinner?.selectedItemPosition
                        item?.let{
                            when (it) {
                                0 -> t4_text.text = ""
                                1 -> t4_text.text = "支援機関への相談をしたことがあります。"
                                2 -> t4_text.text = "親戚や友達に相談をしたことがあります。"
                                3 -> t4_text.text = "誰も身近に相談する相手がいません。"
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

    }

    private fun send(sendtext:String){

        val message= sendtext
        Log.d("mess",message)
        val ref = FirebaseDatabase.getInstance().getReference("/Room_Chat")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val userData = data.getValue<Room_chat_messager>(Room_chat_messager::class.java)
                    val user = userData?.let { it } ?: continue
                    //  val userdata = intent.getParcelableExtra<Room_chat_messager>(Room_chat_Activity.USER_KEY)
                    //val textkadai = intent.getStringExtra(LatestMessagesActivity.USER_KEY)
                    if (user.uid.toString() == FirebaseAuth.getInstance().uid.toString()) {

                        val refe = FirebaseDatabase.getInstance().getReference("/Room_Chat/${FirebaseAuth.getInstance().uid.toString()}")
                        refe.setValue(Room_chat_messager(message, user.kadaimeiText.toString(), FirebaseAuth.getInstance().uid.toString(), user.check))
                                .addOnSuccessListener {

                                    if (user.messageText.toString().isEmpty()) {
                                        intent = Intent(this@Qusetiontemp2Activity_from_chat_all, LatestMessagesActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        intent = Intent(this@Qusetiontemp2Activity_from_chat_all, Room_chat_Activity::class.java)
                                        startActivity(intent)
                                    }
                                }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}
