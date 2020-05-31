package com.example.atm.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import com.example.atm.R
import com.example.atm.services.HelloService
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
//2020-05-31- 23:25
class MainActivity : AppCompatActivity() {
    companion object{
        val REQUEST_LOGIN = 100
        val LOGIN_SUCCESS = 101
        val SET_NICKNAME = 102
        val SELECT_GANDER = 103
        val SET_AGE = 104
        val AGE_OK = 105
    }
    var logon = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getSharedPreferences("nickshare", Context.MODE_PRIVATE)
            .getString("nickname","")?.run { txt_shownick.text = this }
        getSharedPreferences("gendershare", Context.MODE_PRIVATE)
            .getString("gender","")?.run {
                txt_showgender.text = this }
        getSharedPreferences("ageshare", Context.MODE_PRIVATE)
            .getString("age","")?.run { txt_showage.text = this }
//        getSharedPreferences("setok", Context.MODE_PRIVATE)
//            .getBoolean("ok",false)?.run { logon = true }

//        if(!logon) {
////            txt_notsee.visibility = View.GONE
////            txt_hello.visibility = View.VISIBLE
//            val intentToLogin = Intent(this, LoginActivity::class.java)
//            startActivityForResult(intentToLogin,REQUEST_LOGIN)
//        } else {
////            txt_notsee.visibility = View.VISIBLE
////            txt_hello.visibility = View.GONE
//            println("12321")
//        }

        txt_helloworld.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_contacts){
            Toast.makeText(this,"been selected",Toast.LENGTH_SHORT)
        }
        return super.onOptionsItemSelected(item)
    }
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN){
            if (resultCode == Activity.RESULT_OK){
            }
        } else {
            finish()
        }

        if (requestCode == SET_NICKNAME){
            if (resultCode == Activity.RESULT_OK){

            } else {
                Toast.makeText(this,"U should enter U NickName",Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        if (requestCode == SELECT_GANDER){
            if (resultCode == Activity.RESULT_OK){
            } else {
                Toast.makeText(this,"U forgot select U Gender",Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        if (requestCode == SET_AGE){
            if (resultCode == Activity.RESULT_OK){


            } else {
                Toast.makeText(this,"You don't set U Age",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        if (requestCode == AGE_OK){
            if (resultCode == Activity.RESULT_OK){
                getSharedPreferences("nickshare", Context.MODE_PRIVATE)
                    .getString("nickname","")?.run { txt_shownick.text = this }

            } else {
                Toast.makeText(this,"You don't set U Age",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    */
}
