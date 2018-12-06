package com.example.enpit_p13.chatapp.quetion

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import com.example.enpit_p13.chatapp.Activity_chat
import com.example.enpit_p13.chatapp.Message_all
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_room_introduce.*

class RoomIntroduceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_introduce)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        setText1()

        spinner1.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val spinner = parent as? Spinner
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

        sendButton_room_introduce.setOnClickListener {
            Toast.makeText(this,"送信しました。", Toast.LENGTH_LONG).show()
            send()
        }

    }

    private fun send(){

        val message= (text1.text.toString() + text2.text.toString() +
                spinner1.selectedItem.toString() + text3.text.toString() + text4.text.toString())

        Log.d("mess",message)

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val userData = data.getValue<User>(User::class.java)
                    val user = userData?.let { it } ?: continue
                    if(user.uid == FirebaseAuth.getInstance().uid){
                        val reference = FirebaseDatabase.getInstance().getReference().child("/messages").push()
                        reference.setValue(Message_all(message, user.username.toString(),true))
                                .addOnSuccessListener {
                                    val intent = Intent(this@RoomIntroduceActivity, Activity_chat::class.java)
                                    startActivity(intent)
                                }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun setText1(){
        val ref = FirebaseDatabase.getInstance().reference.child("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                for(data in p0.children)
                {
                    val userdata = data.getValue<User>(User::class.java)
                    val users = userdata?.let { it } ?:continue

                    Log.d("AuthUid","${FirebaseAuth.getInstance().uid.toString()}\n")
                    Log.d("getUid",users.uid.toString())

                    if(users.uid.toString() == FirebaseAuth.getInstance().uid.toString()) {
                        text1.text = "私は、\"" + users.username + "\"と申します。\n"
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}
