package com.example.enpit_p13.chatapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import com.example.enpit_p13.chatapp.analyzesheet.Quetion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_select_own_analyze.*
import java.sql.Date
import java.text.SimpleDateFormat
import com.google.firebase.database.ValueEventListener



class SelectOwnAnalyzeActivity : AppCompatActivity() {


    val list: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_own_analyze)

        get_listitem()
        val item = list
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item)
        analyze_listview.adapter = adapter
    }

    fun get_listitem() {
        val ref = FirebaseDatabase.getInstance().reference.child("AnalyzeSheet/${FirebaseAuth.getInstance().uid}")

        val listener = object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val timedata = p0.getValue<Quetion>(Quetion::class.java)
                val str = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(timedata?.timestamp!!))
                list.add(str.toString())
                Log.d("time_child", str)
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
            override fun onCancelled(p0: DatabaseError) {}
        }
        ref.equalTo("1542810854076","timestamp").addChildEventListener(listener)
    }
}
