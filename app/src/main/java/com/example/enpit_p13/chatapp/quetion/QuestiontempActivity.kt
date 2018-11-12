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
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.messages.LatestMessagesActivity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_Activity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_messager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.activity_questiontemp_from_chat_all.*


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
    companion object {
        val USER_KEY = "USER_KEY"
    }
    private fun send(){

        val message= (spin.text.toString() + "\n" + texttemplate1.text.toString() + text1.text.toString()
                + texttemplate2.text.toString()  + "\n" + texttemplate3.text.toString() + text2.text.toString()
                + "\n" + texttemplate4.text.toString() + editText_chat_all.text.toString())
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
                                    refe.setValue(Room_chat_messager(message, user?.kadaimeiText.toString(), FirebaseAuth.getInstance().uid.toString(), user.check))
                                            .addOnSuccessListener {

                                        if (user?.messageText.toString().isEmpty()) {
                                            intent = Intent(this@QuestiontempActivity, LatestMessagesActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            intent = Intent(this@QuestiontempActivity, Room_chat_Activity::class.java)
                                            startActivity(intent)
                                        }
                                    }

                                }




                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
