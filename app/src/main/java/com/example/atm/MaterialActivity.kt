package com.example.atm

import android.Manifest.permission.READ_CONTACTS
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.media.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.atm.expensewithroom.ExpenseActivity
import com.example.atm.fragment.BlankFragment
import com.example.atm.fragment.LoginFragment
import com.example.atm.login.LoginActivity
import com.example.atm.login.MainActivity
import com.example.atm.services.HelloService
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.android.synthetic.main.activity_material.*
import kotlinx.android.synthetic.main.content_material.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.row_transaction.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL
import java.util.concurrent.Executors

class MaterialActivity : AppCompatActivity() {
    private var logon = false



    private lateinit var trans: MutableList<TransactionItem>
    val TAG = MaterialActivity::class.java.simpleName
    companion object{
        val REQUEST_CONTACT = 200
        val REQUEST_LOGIN = 100
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
        (application as AtmApplication).data
        val msg1 = Intent(this, HelloService::class.java)
        msg1.putExtra("extra_MSG","Hello, myBabe")
        startService(msg1)
        msg1.putExtra("extra_MSG","How are U")
        startService(msg1)
        msg1.putExtra("extra_MSG","Would U want to hang out?")
        startService(msg1)

//        fmbt_Login.setOnClickListener {
//            login()
//        }

//        if(!logon) {
//            val intentToLogin = Intent(this, LoginActivity::class.java)
//            startActivityForResult(intentToLogin, MaterialActivity.REQUEST_LOGIN)
//        } else {
//            println("12321")
//        }

        fab.setOnClickListener { view ->
            val blank = supportFragmentManager.findFragmentById(R.id.frameLayout)
            supportFragmentManager.beginTransaction().apply {
//                remove(blank!!)
//                add(R.id.container,LoginFragment())
                replace(R.id.container, LoginFragment())
                commit()
            }
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

    private fun login() {
        //http://atm201605.appspot.com/login?uid=jack&pw=1234
        val uid = et_userid.text.toString()
        val pw = et_password.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(uid,pw)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    getSharedPreferences("atm", Context.MODE_PRIVATE).edit()
                        .putString("uid", uid)
                        //.putBoolean("LOGON",true)
                        .apply()
                    Toast.makeText(this@MaterialActivity, "Login suscessfull", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    AlertDialog.Builder(this@MaterialActivity)
                        .setTitle("Login")
                        .setMessage("Login Fail:${task.exception.toString()}")
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Sign-up"){ diolog, which ->
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(uid,pw)
                                .addOnCompleteListener {
                                    if (it.isSuccessful){

                                    }
                                }
                        }
                }
            }
    }

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
    //BroadcastReceiver 接收到後要做什麼事情
    private val receiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(this@MaterialActivity,"Message sent",
                Toast.LENGTH_SHORT).show()
            val manager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel("test","Testing",NotificationManager.IMPORTANCE_HIGH).apply {
                    manager.createNotificationChannel(this)
                }
            }

            val notifi = androidx.core.app.NotificationCompat.Builder(this@MaterialActivity,"test")
                .setContentTitle("Titlee")
                .setContentText("這是內容你知道的")
                .setSmallIcon(R.drawable.icon2)
                .setColor(Color.GREEN)
                .build()
            manager.notify(1,notifi)
        }

    }
    override fun onStart() {
        super.onStart()
        trans = mutableListOf<TransactionItem>()
        //註冊一個Receiver並且透過IntentFilter過濾你要接受的資訊是什麼
        registerReceiver(receiver, IntentFilter(HelloService.ACRION_MESSAGE_SEND))
        //在IO裡面跑耗時網路連線工作
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://atm201605.appspot.com/h")
            val data: String = url.readText()
            Log.d(TAG, data)
            parseGson(data)
            //parseJSON(data)
            //將UI元件的更動移動到Main執行緒
            withContext(Dispatchers.Main) {
                setupTransactions()
            }
        }
        /*
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
                    holder.date.text = trans[position].date
                    holder.amount.text = trans[position].amount.toString()
                    holder.type.text = trans[position].type.toString()
                }
            }
            runOnUiThread{
                ree.setHasFixedSize(true)
                ree.layoutManager = LinearLayoutManager(this)
                ree.adapter = adapter
            }
        }
        */
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    private fun parseGson(data: String) {
        trans = Gson().fromJson<MutableList<TransactionItem>>(
            data,
            object : TypeToken<MutableList<TransactionItem>>() {}.type
        )
    }

    private fun parseJSON(data:String) {
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
    }
    private fun setupTransactions(){
        val adapter = object : RecyclerView.Adapter<TranHolder>(){
            override fun getItemCount(): Int {
                return trans.size
            }
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranHolder {
                return TranHolder(layoutInflater.inflate(R.layout.row_transaction,parent,false))
            }
            override fun onBindViewHolder(holder: TranHolder, position: Int) {
                holder.date.text = trans[position].date
                holder.amount.text = trans[position].amount.toString()
                holder.type.text = trans[position].type.toString()
            }
        }
        ree.setHasFixedSize(true)
        ree.layoutManager = LinearLayoutManager(this@MaterialActivity)
        ree.adapter = adapter
    }

    inner class TranHolder(view: View):RecyclerView.ViewHolder(view){
        val date = view.txt_date
        val amount = view.txt_amount
        val type = view.txt_type
    }
}
