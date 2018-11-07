package com.example.enpit_p13.chatapp.room_chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.enpit_p13.chatapp.Message
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.messages.ChatToItem
import com.example.enpit_p13.chatapp.messages.ChatfromItem
import com.example.enpit_p13.chatapp.messages.NewMessageActivity
import com.example.enpit_p13.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_room_chat_from__list_view.*

class Room_chat_from_ListView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_chat_from__list_view)
        val userdata = intent.getParcelableExtra<Room_chat_messager>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = userdata.kadaimeiText.toString()
        explain_textview_from_listview.text = userdata.messageText.toString()
        Log.d("Chat","user from Room_Chat ${userdata.uid.toString()}")
        createFirebaseListener()
        send_Button_room_chat_from_listview.setOnClickListener{
            if (!room_chat_edittext_from_listview.text.toString().isEmpty()){
                sendData()
                createFirebaseListener()


            } else {

                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()

            }

        }
    }
    private fun sendData() {

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            val userdata = intent.getParcelableExtra<Room_chat_messager>(NewMessageActivity.USER_KEY)
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {

                    Log.d("Chat","userdata ${userdata.uid.toString()}")
                    val userData = data.getValue<User>(User::class.java)
                    var i = 0
                    val user = userData?.let { it } ?: continue
                    if(user.uid == FirebaseAuth.getInstance().uid){
                        val reference = FirebaseDatabase.getInstance().getReference("/Room_Chat/${userdata.uid.toString()}/${userdata.kadaimeiText.toString()}").push()
                        reference.setValue(Message(room_chat_edittext_from_listview.text.toString(),user?.username.toString()))
                        room_chat_edittext_from_listview.setText("")
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun createFirebaseListener() {
        val userdata = intent.getParcelableExtra<Room_chat_messager>(NewMessageActivity.USER_KEY)
        Log.d("Main","${userdata.uid}${userdata.kadaimeiText}")
        val ref =  FirebaseDatabase.getInstance().getReference()?.child("/Room_Chat/${userdata.uid.toString()}/${userdata.kadaimeiText.toString()}")
        ref?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var count = 0;

                val adapter = GroupAdapter<ViewHolder>()

                // val toReturn: ArrayList<Message> = ArrayList();

                for (data in dataSnapshot.children) {

                    val messageData = data.getValue<Message>(Message::class.java)
                    val message = messageData?.let { it } ?: continue
                    Log.d("Chat",message.text.toString())
                    if (message.Uid == FirebaseAuth.getInstance().uid) {
                        count += 1
                        adapter.add(ChatToItem(message.text!!))
                    }
                    else {
                        count += 1
                        adapter.add(ChatfromItem(message.text!!,message.username!!))
                    }

                }


                recycler_chat_room_from_listroom.adapter = adapter
                // setupAdapter(toReturn)
                recycler_chat_room_from_listroom.scrollToPosition(count-1)
            }


            override fun onCancelled(databaseError: DatabaseError) {
                //log error
            }
        })

    }
}
