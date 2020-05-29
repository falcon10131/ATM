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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        if (et_userid.toString() == "" || et_password.toString() == ""){
            fmbt_Login.setOnClickListener {
                Toast.makeText(this,"Login Fail", Toast.LENGTH_LONG).show()
            }
        }else {
            fmbt_Login.setOnClickListener {
                login(view)
            }
        }

        fmbt_register.setOnClickListener { cancel(view)
        }
        getSharedPreferences("atm", Context.MODE_PRIVATE)
            .getString("uid","")?.run {
                et_userid.setText(this)
            }

    }

    fun login(v: View) {
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
                    Toast.makeText(this@LoginActivity, "Login suscessfull", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    AlertDialog.Builder(this@LoginActivity)
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

//        var data:String = ""
//        CoroutineScope(Dispatchers.IO).launch {
//            val url = URL("http://atm201605.appspot.com/login?uid=$uid&pw=$pw")
//            data = url.readText()
//            Log.d("url",data)
//            withContext(Dispatchers.Main) {
//                if (data == "1") {
//                    getSharedPreferences("atm", Context.MODE_PRIVATE).edit()
//                        .putString("uid", uid)
//                        //.putBoolean("LOGON",true)
//                        .apply()
//                    Toast.makeText(this@LoginActivity, "Login suscessfull", Toast.LENGTH_SHORT).show()
//                    setResult(Activity.RESULT_OK)
//                    finish()
//                } else {
//                    AlertDialog.Builder(this@LoginActivity)
//                        .setTitle("Login")
//                        .setMessage("You Login Fail")
//                        .setPositiveButton("OK", null)
//                        .show()
//                }
//            }
//        }
    }
    private fun cancel(v: View) {
        Toast.makeText(this,"Intent to Main", Toast.LENGTH_LONG).show()
        var intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
