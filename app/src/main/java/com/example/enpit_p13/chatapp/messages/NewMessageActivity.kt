package com.example.enpit_p13.chatapp.messages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.enpit_p13.chatapp.Activity_chat
import com.example.enpit_p13.chatapp.R
import com.example.enpit_p13.chatapp.models.Check_online
import com.example.enpit_p13.chatapp.models.User
import com.example.enpit_p13.chatapp.room_chat.Room_chat_Activity
import com.example.enpit_p13.chatapp.room_chat.Room_chat_from_ListView
import com.example.enpit_p13.chatapp.room_chat.Room_chat_messager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.room_chat.view.*
import org.jetbrains.anko.startActivity
import java.sql.Date
import java.text.SimpleDateFormat

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select Room"
        FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().uid.toString()}")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        val data = p0.getValue(User::class.java)
                        FirebaseDatabase.getInstance().getReference("/Address/${data?.uid.toString()}")
                                .setValue(Check_online("List_Room",data?.username.toString(),false))
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })

        fetchUsers()
        home_chat_list.setOnClickListener {
            startActivity<LatestMessagesActivity>()
        }
        chat_all_list.setOnClickListener {
            startActivity<Activity_chat>()
        }

    }

    override fun onBackPressed() {
        startActivity<LatestMessagesActivity>()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }
    val check : ArrayList<Room_chat_messager> = ArrayList()

    private fun fetchUsers() {
        val adapter = GroupAdapter<ViewHolder>()
        val ref = FirebaseDatabase.getInstance().getReference().child("/Room_Chat/")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var count = p0.childrenCount
                for (data in p0.children) {
                    val datauser = data.getValue<Room_chat_messager>(Room_chat_messager::class.java)
                    val user = datauser?.let { it } ?: continue
                    Log.d("Main", count.toString())
                    if (!user.check) {
                        count--
                    } else {
                        if (user.kadaimeiText != "") {
                            Log.d("Chat", "user from New ${user.uid.toString()}")

                            check.add(user)
                            Log.d("Data", "11 ${check.toString()} ")

                            check.sortByDescending { it -> it.timestamp }
                            Log.d("Data", "11 ${check.toString()} ")
                            Log.d("Data", "11 ${check.size} ")
                        }
                    }
                }
                Log.d("Data", "11 ${check.toString()} ")
                Log.d("Data", "11 ${count} ")

                Log.d("Data", check.toString())
                if (check.size.toLong() == count) {
                    for (data in check) {
                        Log.d("Data", data.kadaimeiText.toString())

                        adapter.add(UserItem(data))
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            if(item.user.uid.toString()==FirebaseAuth.getInstance().uid.toString())
            {
                val intent = Intent(view.context, Room_chat_Activity::class.java)
                Log.d("Chat", "user from New b ${item.user.uid.toString()}")
                startActivity(intent)
            }
            else{
                FirebaseDatabase.getInstance().getReference().child("/users/")
                        .addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(p0: DataSnapshot) {
                                p0.children.forEach {
                                    val data = it?.getValue(User::class.java)
                                    if (data?.uid.toString() == FirebaseAuth.getInstance().uid.toString()) {
                                        val reference = FirebaseDatabase.getInstance().getReference("/Address/${FirebaseAuth.getInstance().uid.toString()}")
                                        reference.setValue(Check_online(userItem.user.uid.toString(), data?.username.toString(),false))
                                    }
                                }
                            }

                            override fun onCancelled(p0: DatabaseError) {

                            }
                        })
            val intent = Intent(view.context, Room_chat_from_ListView::class.java)

            startActivity(intent)
            }
        }

        recyclemessage_chat_list.adapter= adapter

    }

}

class UserItem(val user: Room_chat_messager):Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        FirebaseDatabase.getInstance().getReference("/users")
                .addValueEventListener(object :ValueEventListener{
                    @SuppressLint("SimpleDateFormat", "SetTextI18n")
                    override fun onDataChange(p0: DataSnapshot) {
                        for (data in p0.children) {

                            val check_user = data?.getValue<User>(User::class.java)
                                if(check_user?.uid.toString() == user.uid.toString()) {
                                    val time = SimpleDateFormat("yyyy年MM月dd日").format(Date(user.timestamp))
                                    viewHolder.itemView.kadaimei_textview.text = "作成者：${check_user?.username.toString()}\t\t課題名：${user.kadaimeiText.toString()}\t\t作成日：${time}"
                                }
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
    }

    override fun getLayout(): Int {
        return R.layout.room_chat
    }
}

//this is super tedious
    //class CustomAdapter: RecyclerView.Adapter<ViewModel>{
    //override fun onBindViewHolder(holder: ViewModel, position: Int) {
    //    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  //  }
//}
