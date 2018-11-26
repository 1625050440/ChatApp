package com.example.enpit_p13.chatapp.room_chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.enpit_p13.chatapp.Message
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.messages.ChatToItem
import com.example.enpit_p13.chatapp.messages.ChatfromItem
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity
import com.example.enpit_p13.chatapp.models.Check_online
import com.example.enpit_p13.chatapp.models.User
import com.example.enpit_p13.chatapp.quetion.QuestiontempActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_room_chat_.*
import org.jetbrains.anko.startActivity

class Room_chat_Activity : AppCompatActivity() {
    companion object {
        val USER_KEY = "USER_KEY"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_chat_)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)



        val ref = FirebaseDatabase.getInstance().getReference("/Room_Chat")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
            for (data in p0.children) {
                val userData = data.getValue<Room_chat_messager>(Room_chat_messager::class.java)
                val user = userData?.let { it } ?: continue
                //  val userdata = intent.getParcelableExtra<Room_chat_messager>(Room_chat_Activity.USER_KEY)
                //val textkadai = intent.getStringExtra(LatestMessagesActivity.USER_KEY)
                if (user.uid.toString() == FirebaseAuth.getInstance().uid.toString()) {
                 supportActionBar?.title = user.kadaimeiText.toString()
                    explain_textview.text = user.messageText.toString()
                    //sendAddress
                    FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
                    .setValue(Check_online(user.kadaimeiText.toString()))
                    val key =  user.kadaimeiText.toString()
                    val ref = FirebaseDatabase.getInstance().getReference()?.child("/Address/")
                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            var count = p0.childrenCount
                            for (data in p0.children) {
                                val userData = data.getValue<Check_online>(Check_online::class.java)
                                val user = userData?.let { it } ?: continue
                                if (user.uid_check_online.toString() != key ) {
                                    count--
                                }

                            }
                                    myroom_count_textview.text= "在室中：$count"
                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })
                    createFirebaseListener(user)
                    send_Button_room_chat.setOnClickListener {
                        template_button.visibility = View.INVISIBLE
                        template_button.isClickable = false
                        if (!room_chat_edittext.text.toString().isEmpty()) {
                            sendData(user)
                            //createFirebaseListener(userdata)
                        } else {

                            Toast.makeText(this@Room_chat_Activity, "Please enter a message", Toast.LENGTH_SHORT).show()

                        }
                    }
                }

            }

        }
        override fun onCancelled(p0: DatabaseError) {

        }
    })

        send_Button_room_chat.setOnLongClickListener() {
                template()
            }

            template_button.setOnClickListener {
                template_button.visibility = View.INVISIBLE
                template_button.isClickable = false
                intent = Intent(this, QuestiontempActivity::class.java)
              //  intent.putExtra(USER_KEY, userdata)
                startActivity(intent)
            }
        //recycler_chat_room.adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        startActivity<LatestMessagesActivity>()
    }

    private fun sendData(userdata : Room_chat_messager) {

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    //val userdata = intent.getParcelableExtra<Room_chat_messager>(LatestMessagesActivity.USER_KE)
                    val userData = data.getValue<User>(User::class.java)
                    var i = 0
                    val user = userData?.let { it } ?: continue
                    if(user?.uid == FirebaseAuth.getInstance().uid){
                        val reference = FirebaseDatabase.getInstance().getReference("/Room_Chat/${userdata.kadaimeiText.toString()}").push()
                        reference.setValue(Message(room_chat_edittext.text.toString(),user?.username.toString()))
                                .addOnSuccessListener {
                                    room_chat_edittext.text.clear()
                                    recycler_chat_room.scrollToPosition(recycler_chat_room.adapter!!.itemCount-1)
                                }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun createFirebaseListener(userdata: Room_chat_messager) {
        //val userdata = intent.getParcelableExtra<Room_chat_messager>(LatestMessagesActivity.USER_KE)
        Log.d("Main","kaidaimei ${userdata.uid}${userdata.kadaimeiText}")
        val ref =  FirebaseDatabase.getInstance().getReference()?.child("/Room_Chat/${userdata.kadaimeiText.toString()}")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                // val toReturn: ArrayList<Message> = ArrayList();

                for (data in dataSnapshot.children) {

                    val messageData = data.getValue<Message>(Message::class.java)
                    val message = messageData?.let { it } ?: continue

                    if (message.Uid == FirebaseAuth.getInstance().uid) {

                            adapter.add(ChatToItem(message.text!!))

                    }
                    else {
                            adapter.add(ChatfromItem(message.text!!,message.username!!))
                    }

                }


                recycler_chat_room.adapter = adapter
                // setupAdapter(toReturn)
                recycler_chat_room.scrollToPosition(adapter.itemCount-1)
            }


            override fun onCancelled(databaseError: DatabaseError) {
                //log error
            }
        })


    }
    private fun template(): Boolean {
        template_button.visibility = View.VISIBLE
        template_button.isClickable = true
        return false
    }
}





