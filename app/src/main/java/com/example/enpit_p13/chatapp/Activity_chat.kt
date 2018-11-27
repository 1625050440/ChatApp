package com.example.enpit_p13.chatapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.enpit_p13.chatapp.messages.ChatToItem
import com.example.enpit_p13.chatapp.messages.ChatfromItem
import com.example.enpit_p13.chatapp.models.Check_online
import com.example.enpit_p13.chatapp.models.User
import com.example.enpit_p13.chatapp.quetion.QuestiontempActivity_from_chat_all
import com.example.enpit_p13.chatapp.quetion.RoomIntroduceActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.startActivity

class Activity_chat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        FirebaseDatabase.getInstance().getReference()?.child("/users/")
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        p0.children.forEach {
                            val data = it?.getValue(User::class.java)
                            if(data?.uid.toString() == FirebaseAuth.getInstance().uid.toString()){
                            val reference =FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
                            reference.setValue(Check_online("Chat_all",data?.username.toString()))
                        }}
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
        val ref = FirebaseDatabase.getInstance().getReference()?.child("/Address/")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var count = p0.childrenCount
                for (data in p0.children) {
                    val userData = data.getValue<Check_online>(Check_online::class.java)
                    val user = userData?.let { it } ?: continue
                    if (user.uid_check_online.toString() != "Chat_all") {
                        count--

                    }

                }
                online_count_text_view.text= "在室中：${count}"
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        createFirebaseListener()
        send_Button.setOnLongClickListener() {
            template()
        }
        send_Button.setOnClickListener {

            if (!mainActivityEditText.text.toString().isEmpty()) {

                sendData()
                // createFirebaseListener()
                //clear the text


            } else {

                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()

            }

        }
        template_button_from_chat_all.setOnClickListener {
            template_button_from_chat_all.visibility = View.INVISIBLE
            template_button_from_chat_all.isClickable = false
            intent = Intent(this, QuestiontempActivity_from_chat_all::class.java)
            startActivity(intent)
        }

        room_intro_button.setOnClickListener {
            room_intro_button.visibility = View.INVISIBLE
            room_intro_button.isClickable = false
            startActivity<RoomIntroduceActivity>()
        }

    }

    private fun sendData() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val userData = data.getValue<User>(User::class.java)
                    var i = 0
                    val user = userData?.let { it } ?: continue
                    if (user?.uid == FirebaseAuth.getInstance().uid) {
                        val reference = FirebaseDatabase.getInstance().getReference()?.child("/messages").push()
                        reference.setValue(Message(mainActivityEditText.text.toString(), user?.username.toString()))
                                .addOnSuccessListener {
                                    mainActivityEditText.text.clear()

                                }

                    }
                }
            }


            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }

    /**

     * Set listener for Firebase

     */

    private fun createFirebaseListener() {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                // val toReturn: ArrayList<Message> = ArrayList();

                for (data in dataSnapshot.children) {

                    val messageData = data.getValue<Message>(Message::class.java)
                    //unwrap
                    val message = messageData?.let { it } ?: continue

                    if (message.Uid == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatToItem(message.text!!))

                    } else {

                        adapter.add(ChatfromItem(message.text!!, message.username!!))
                    }
                }
                recycle_chat.adapter = adapter
                recycle_chat.scrollToPosition(recycle_chat.adapter!!.itemCount-1)
            }


        override fun onCancelled(databaseError: DatabaseError) {
            //log error
        }
    }

        FirebaseDatabase.getInstance().getReference()?.child("messages")?.addValueEventListener(postListener)
    }
    private fun template(): Boolean {
        template_button_from_chat_all.visibility = View.VISIBLE
        template_button_from_chat_all.isClickable = true
        room_intro_button.visibility = View.VISIBLE
        room_intro_button.isClickable = true
        return false
    }
}
   /* private fun setupAdapter(data: ArrayList<Message>){
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        val linearLayoutManager = LinearLayoutManager(this)


        recycle_chat.adapter = MessageAdapter(data) {

            Toast.makeText(this, "${it.text} clicked", Toast.LENGTH_SHORT).show()

        }
        recycle_chat.adapter = MessageAdapter_to_row(data) {

            Toast.makeText(this, "${it.text} clicked", Toast.LENGTH_SHORT).show()

        }
        //scroll to bottom

        recycle_chat.scrollToPosition(data.size - 1)

    }
}*/
