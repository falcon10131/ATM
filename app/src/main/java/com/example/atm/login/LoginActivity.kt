package com.example.atm.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.atm.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    val SET_NICKNAME = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (et_userid.toString() == "" || et_password.toString() == ""){
            bt_Login.setOnClickListener {
                Toast.makeText(this,"Login Fail", Toast.LENGTH_LONG).show()
            }
        }else {
            bt_Login.setOnClickListener {
                login(view)

            }
        }


        bt_register.setOnClickListener { cancel(view)
           }
        getSharedPreferences("atm", Context.MODE_PRIVATE)
            .getString("uid","")?.run {
                et_userid.setText(this)
            }

    }

    fun login(v: View) {
        val uid = et_userid.text.toString()
        val pw = et_password.text.toString()
        if (uid == "simon" && pw == "123") {
            getSharedPreferences("atm", Context.MODE_PRIVATE).edit()
                .putString("uid",uid)
                //.putBoolean("LOGON",true)
                .apply()
            Toast.makeText(this,"Login suscessfull", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            val tonick = Intent(this@LoginActivity,NicknameActivity::class.java)
            startActivityForResult(tonick,SET_NICKNAME)
            finish()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Login")
                .setMessage("You Login Fail")
                .setPositiveButton("OK",null)
                .show()
        }
    }
    fun cancel(v: View) {
        Toast.makeText(this,"Intent to Main", Toast.LENGTH_LONG).show()
        var intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
