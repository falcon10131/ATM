package com.example.atm

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rowcontact.view.*
import java.util.zip.Inflater

class ContactAdapter(private val contacts : MutableList<Contact>): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rowcontact,parent,false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    //上畫面都會問你要上什麼 控制要長什麼樣子
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.txtName.text = contacts[position].name
        holder.txtPhone.text = contacts[position].phone
    }

    //ContactViewHolder接收到的view會傳給父類別RecyclerView.ViewHolder
    inner class ContactViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtName = view.txt_contactName
        val txtPhone = view.txt_Phone
    }
}

