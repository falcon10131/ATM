package com.example.atm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.jsonrow.view.*
import kotlinx.android.synthetic.main.rowcontact.view.*
import org.json.JSONArray

class JSONAdapter(private val contacts : MutableList<TransactionItem>): RecyclerView.Adapter<JSONAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jsonrow,parent,false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    //上畫面都會問你要上什麼 控制要長什麼樣子
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.txt_Account.text = contacts[position].account
        holder.txt_date.text = contacts[position].date
        holder.txt_amount.setText(contacts[position].amount)
        holder.txt_Type.setText(contacts[position].type)
    }

    //ContactViewHolder接收到的view會傳給父類別RecyclerView.ViewHolder
    inner class ContactViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txt_Account = view.txt_Account
        val txt_date = view.txt_date
        val txt_amount = view.txt_amount
        val txt_Type = view.txt_Type
    }
}

