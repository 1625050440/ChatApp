package com.example.enpit_p13.chatapp.TopPage

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.example.enpit_p13.chatapp.R

import kotlinx.android.synthetic.main.activity_top_page.*

class TopPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_page)
        setSupportActionBar(toolbar)


    }

}
