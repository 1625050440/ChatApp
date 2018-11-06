package com.example.enpit_p13.chatapp.room_chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.enpit_p13.chatapp.Message
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity
import com.example.enpit_p13.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_room_chat_.*

class Room_chat_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_chat_)
        val userdata = intent.getParcelableExtra<Room_chat_messager>(LatestMessagesActivity.USER_KE)
        supportActionBar?.title = userdata.kadaimeiText
        explain_textview.text = userdata.messageText
        //createFirebaseListener()
        send_Button_room_chat.setOnClickListener{
            if (!room_chat_edittext.text.toString().isEmpty()){
                sendData()
                //createFirebaseListener()
                room_chat_edittext.setText("")

            } else {

                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()

            }

        }
    }
    private fun sendData() {

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val userdata = intent.getParcelableExtra<Room_chat_messager>(LatestMessagesActivity.USER_KE)
                    val userData = data.getValue<User>(User::class.java)
                    var i = 0
                    val user = userData?.let { it } ?: continue
                    if(user?.uid == FirebaseAuth.getInstance().uid){
                        val reference = FirebaseDatabase.getInstance().getReference()?.child("/${userdata.Uid}/${userdata.kadaimeiText}").push()
                        reference.setValue(Message(mainActivityEditText.text.toString(),user?.username.toString()))

                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }


}


