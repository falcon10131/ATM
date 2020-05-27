package com.example.atm

import android.Manifest.permission.READ_CONTACTS
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.atm.expensewithroom.ExpenseActivity

import kotlinx.android.synthetic.main.activity_material.*
import kotlinx.android.synthetic.main.content_material.*
import kotlinx.android.synthetic.main.row_transaction.view.*
import org.json.JSONArray
import java.net.URL
import java.util.concurrent.Executors

class MaterialActivity : AppCompatActivity() {
    private lateinit var trans: MutableList<TransactionItem>
    val TAG = MaterialActivity::class.java.simpleName
    companion object{
        val REQUEST_CONTACT = 200
    }
    private lateinit var adapter: ContactAdapter

    val contacts = mutableListOf<Contact>(
        Contact("Simon", "0983000448"),
        Contact("Jack", "0906548412"),
        Contact("Allen", "0944554541")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?,//TextView
                position: Int,//Index
                id: Long//Item ID
            ) {
                Log.d(TAG, "onItemSelected: $position")
                val fruitNames = resources.getStringArray(R.array.fruit_names)
                fruitNames[position]
                Log.d(TAG, "Fruitname = ${fruitNames[position]}")
            }

        }//spinner.onItemSelectedListener

        ree.setHasFixedSize(true)
        ree.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(contacts)
        ree.adapter = adapter

        val permission = ActivityCompat.checkSelfPermission(this,READ_CONTACTS)
        if(permission == PackageManager.PERMISSION_GRANTED){
            //Do what U do
            readContacts()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(READ_CONTACTS),REQUEST_CONTACT)
        }

    }//onCreate

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readContacts()
            } else {
                //TODO: message
            }
        }
    }

    private fun readContacts() {
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null)
        cursor?.run {
            contacts.clear()
            while(cursor.moveToNext()) {
                val id = cursor.getInt(
                    cursor.getColumnIndex(ContactsContract.Contacts.NAME_RAW_CONTACT_ID))
                val name = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhone = getInt(getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                val contact = Contact(name,"")
                Log.d(TAG, "$id  $name  $hasPhone")
                if (hasPhone == 1) {
                    val c2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ? ",
                        arrayOf(id.toString()), null)
                    c2?.run {
                        while(c2.moveToNext()) {
                            val phone = c2.getString(
                                c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA))
                            contact.phone = phone
                            Log.d(TAG, "\t\t $phone" )
                        }
                    }

                }
                contacts.add(contact)
            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_camera -> {
                Intent(this,CameraActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.action_expense -> {
                Intent(this, ExpenseActivity::class.java).apply {
                    startActivity(this)
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        trans = mutableListOf<TransactionItem>()

        Executors.newSingleThreadExecutor().execute {
            val url = URL("https://atm201605.appspot.com/h")
            val data: String = url.readText()
            Log.d(TAG, data)
            val array = JSONArray(data)
            for (i in 0 until array.length()) {
                var obj = array.getJSONObject(i)
                val acc = obj.getString("account")
                val date = obj.getString("date")
                val amount = obj.getInt("amount")
                val type = obj.getInt("type")
                val t = TransactionItem(acc, date, amount, type)
                trans.add(t)
            }
            JSONAdapter(trans)
            val adapter = object : RecyclerView.Adapter<TranHolder>(){
                override fun getItemCount(): Int {
                    return trans.size
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranHolder {
                    return TranHolder(layoutInflater.inflate(R.layout.row_transaction,parent,false))
                }

                override fun onBindViewHolder(holder: TranHolder, position: Int) {
                    holder.date.text = trans.get(position).date
                    holder.amount.text = trans.get(position).amount.toString()
                    holder.type.text = trans.get(position).type.toString()
                }
            }
            runOnUiThread{
                ree.setHasFixedSize(true)
                ree.layoutManager = LinearLayoutManager(this)
                ree.adapter = adapter
            }
        }
    }
    inner class TranHolder(view: View):RecyclerView.ViewHolder(view){
        val date = view.txt_date
        val amount = view.txt_amount
        val type = view.txt_type
    }
}
