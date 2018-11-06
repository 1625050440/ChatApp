package com.example.enpit_p13.chatapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.chat_to_row.view.*

class MessageAdapter_to_row(val messages: ArrayList<Message>, val itemClick: (Message) -> Unit) :

        RecyclerView.Adapter<MessageAdapter_to_row.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_to_row, parent, false)

        return ViewHolder(view, itemClick)

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindForecast(messages[position])

    }



    override fun getItemCount() = messages.size



    class ViewHolder(view: View, val itemClick: (Message) -> Unit) : RecyclerView.ViewHolder(view) {



        fun bindForecast(message: Message) {

            with(message) {

                itemView.textView_chat_to_row.text = message.text

                itemView.setOnClickListener { itemClick(this) }

            }

        }

    }

}