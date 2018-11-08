package com.example.enpit_p13.chatapp.quetion

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import com.example.enpit_p13.chatapp.Message
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.models.User
import com.example.enpit_p13.chatapp.room_chat.Room_chat_Activity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_messager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_question.*


class QuestiontempActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.apply{
            val editText1 = getString("TEXT1", "")
            val editText2 = getString("TEXT2", "")
            val editText3 = getString("TEXT3", "")

            text1.setText(editText1)
            text2.setText(editText2)
         //   text3.setText(editText3)
        }
        spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val spinner = parent as? Spinner
                        val item = spinner?.selectedItem as? String
                        item?.let{
                            if (it.isNotEmpty()) spin.text = it
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

        sendButton.setOnClickListener {
            Toast.makeText(this,"送信しました。",Toast.LENGTH_LONG).show()
            send()
        }

    }

    private fun send(){

        val message= (spin.text.toString() + "\n" + texttemplate1.text.toString() + text1.text.toString()
                + texttemplate2.text.toString()  + "\n" + texttemplate3.text.toString() + text2.text.toString() + "\n" + texttemplate4.text.toString())
        Log.d("mess",message)

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {

                    val userdata = intent.getParcelableExtra<Room_chat_messager>(Room_chat_Activity.USER_KEY)
                    val userData = data.getValue<User>(User::class.java)
                    var i = 0
                    val user = userData?.let { it } ?: continue
                    if(user?.uid == FirebaseAuth.getInstance().uid){
                        val reference = FirebaseDatabase.getInstance().getReference()?.child("/Room_Chat/${userdata.uid.toString()}/${userdata.kadaimeiText.toString()}").push()
                        reference.setValue(Message(message,user?.username.toString()))
                                .addOnSuccessListener {
                                    intent = Intent(this@QuestiontempActivity,Room_chat_Activity::class.java)
                                    startActivity(intent)
                                }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        //mDatabase.setValue(Message(message,"dasdfasdfas"))

    }
}
