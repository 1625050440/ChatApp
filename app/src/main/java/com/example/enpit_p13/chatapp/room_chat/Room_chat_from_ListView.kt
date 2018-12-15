package com.example.enpit_p13.chatapp.room_chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.enpit_p13.chatapp.Activity_chat
import com.example.enpit_p13.chatapp.Message
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.messages.ChatToItem
import com.example.enpit_p13.chatapp.messages.ChatfromItem
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity
import com.example.enpit_p13.chatapp.messages.NewMessageActivity
import com.example.enpit_p13.chatapp.models.Check_online
import com.example.enpit_p13.chatapp.models.User
import com.example.enpit_p13.chatapp.toppage.TopPageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_room_chat_from__list_view.*
import org.jetbrains.anko.startActivity
class Room_chat_from_ListView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_chat_from__list_view)
        /*val userdata = intent.getParcelableExtra<Room_chat_messager>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = userdata.kadaimeiText.toString()
        explain_textview_from_listview.text = userdata.messageText.toString()

        FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
        .setValue(Check_online(userdata.kadaimeiText.toString()))
        Log.d("Chat","user from Room_Chat ${userdata.uid.toString()}")*/
        FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        Log.d("Check_data","$p0")
                            val data = p0.getValue(Check_online::class.java)
                            FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
                                    .setValue(Check_online(data?.uid_check_online.toString(),data?.username.toString(),false))
                                FirebaseDatabase.getInstance().getReference("/Room_Chat/${data?.uid_check_online}")
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(p0: DataSnapshot) {
                                                    val user = p0.getValue(Room_chat_messager::class.java)

                                                    if(user?.check == false)
                                                    {
                                                        room_de_confirm.visibility = View.VISIBLE
                                                        return_button.visibility = View.VISIBLE
                                                        return_button.isClickable = true
                                                        return_button.setOnClickListener {
                                                            room_de_confirm.visibility = View.INVISIBLE
                                                            return_button.visibility = View.INVISIBLE
                                                            return_button.isClickable = false
                                                            startActivity<LatestMessagesActivity>()
                                                        }

                                                    }
                                                        FirebaseDatabase.getInstance().getReference("/users/${user?.uid.toString()}")
                                                                .addValueEventListener(object :ValueEventListener {
                                                                    override fun onDataChange(p0: DataSnapshot) {
                                                                        val myuser = p0.getValue(User::class.java)
                                                                        supportActionBar?.title = "${myuser?.username} ${user?.kadaimeiText}"
                                                                        explain_textview_from_listview.text = user?.messageText
                                                                        val get_uid = myuser?.uid.toString()
                                                                        Log.d("Check_online", "$get_uid")
                                                                        val ref = FirebaseDatabase.getInstance().getReference()?.child("/Address/")
                                                                        ref.addValueEventListener(object : ValueEventListener {
                                                                            override fun onDataChange(p0: DataSnapshot) {
                                                                                var count = p0.childrenCount
                                                                                for (data in p0.children) {
                                                                                    val userData = data.getValue<Check_online>(Check_online::class.java)
                                                                                    val user = userData?.let { it } ?: continue
                                                                                    FirebaseDatabase.getInstance().getReference("/Address/${user.my_uid.toString()}")
                                                                                            .onDisconnect()
                                                                                            .setValue(Check_online("OFF",user.username.toString(),false))
                                                                                    if (user.uid_check_online.toString() != get_uid) {
                                                                                        count--
                                                                                    }

                                                                                }
                                                                                thisroom_count_textview.text= "在室中：$count"
                                                                            }

                                                                            override fun onCancelled(p0: DatabaseError) {

                                                                            }
                                                                        })
                                                                        createFirebaseListener(get_uid)
                                                                        send_Button_room_chat_from_listview.setOnClickListener{
                                                                            if (!room_chat_edittext_from_listview.text.toString().isEmpty()){
                                                                                sendData(get_uid)
                                                                                // createFirebaseListener()


                                                                            } else {

                                                                                Toast.makeText(this@Room_chat_from_ListView, "Please enter a message", Toast.LENGTH_SHORT).show()

                                                                            }

                                                                        }
                                                                    }

                                                                    override fun onCancelled(p0: DatabaseError) {

                                                                    }
                                                    })
                                                                                                          }
                                            override fun onCancelled(p0: DatabaseError) {

                                            }
                                        })


                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
        home_chat_guess.setOnClickListener {
            startActivity<LatestMessagesActivity>()
        }
        chat_all_guess.setOnClickListener {
            startActivity<Activity_chat>()
        }
        room_view_guess.setOnClickListener {
            startActivity<NewMessageActivity>()
        }
        toppage_list_room.setOnClickListener {
            startActivity<TopPageActivity>()
        }

    }

    override fun onBackPressed() {
        startActivity<LatestMessagesActivity>()
    }

    private fun sendData(check_uid: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {

                    val userData = data.getValue<User>(User::class.java)
                    val user = userData?.let { it } ?: continue
                    if(user.uid == FirebaseAuth.getInstance().uid) {
                        val reference = FirebaseDatabase.getInstance().getReference("/Room/$check_uid").push()
                        reference.setValue(Message(room_chat_edittext_from_listview.text.toString(), user?.username.toString()))
                                .addOnSuccessListener {
                                    room_chat_edittext_from_listview.setText("")
                                    recycler_chat_room_from_listroom.scrollToPosition(recycler_chat_room_from_listroom.adapter!!.itemCount-1)
                                }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun createFirebaseListener(check_uid : String) {

      //  Log.d("Main","${userdata.uid}${userdata.kadaimeiText}")
        val reference = FirebaseDatabase.getInstance().getReference("/Room_Chat")
        reference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach() {
                    val check = it.getValue(Room_chat_messager::class.java)
                    if (check?.uid == check_uid  ) {
                        val ref = FirebaseDatabase.getInstance().getReference("/Room/$check_uid")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val adapter = GroupAdapter<ViewHolder>()
                                for (data in dataSnapshot.children) {
                                    val messageData = data.getValue<Message>(Message::class.java)
                                    val message = messageData?.let { it } ?: continue
                                    Log.d("Chat", message.text.toString())

                                        if (message.Uid == FirebaseAuth.getInstance().uid) {

                                            adapter.add(ChatToItem(message.text!!))
                                        } else {
                                            adapter.add(ChatfromItem(message.text!!, message.username!!,false,"",false))
                                        }
                                    }
                                recycler_chat_room_from_listroom.adapter = adapter
                                // setupAdapter(toReturn)
                                recycler_chat_room_from_listroom.scrollToPosition(adapter.itemCount - 1)
                            }
                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {}
        })
        }

}
