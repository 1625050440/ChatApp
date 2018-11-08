package com.example.enpit_p13.chatapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.enpit_p13.chatapp.messages.ChatToItem
import com.example.enpit_p13.chatapp.messages.ChatfromItem
import com.example.enpit_p13.chatapp.models.User
import com.example.enpit_p13.chatapp.quetion.QuestiontempActivity_from_chat_all
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

class Activity_chat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
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
                recycle_chat.scrollToPosition(recycle_chat.adapter.itemCount-1)
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
