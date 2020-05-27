package com.example.atm.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.atm.R
import kotlinx.android.synthetic.main.activity_age.*

class AgeActivity : AppCompatActivity() {
    val AGE_OK = 105
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age)
        getSharedPreferences("ageshare", Context.MODE_PRIVATE)
            .getString("age", "")?.run {
              et_age.setText(this)
            }

//        if (et_age.text.toString() != "")
//        {
//            setResult(Activity.RESULT_OK)
//            val toHome = Intent(this,MainActivity::class.java)
//            startActivityForResult(toHome,AGE_OK)
//        }

        bt_conage.setOnClickListener {
            enterage()
        }
    }

    private fun enterage(){
        var age = et_age.text.toString()
        if (age != null){
            getSharedPreferences("ageshare", Context.MODE_PRIVATE).edit()
                .putString("age", age.toString())
                .apply()
            getSharedPreferences("setok", Context.MODE_PRIVATE).edit()
                .putBoolean("ok",true)
                .apply()
            Toast.makeText(this,"Age enter success", Toast.LENGTH_SHORT).show()
            val toHome = Intent(this,MainActivity::class.java)
            //startActivityForResult(toHome,AGE_OK
            setResult(Activity.RESULT_OK,toHome)
            finish()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Age")
                .setMessage("Plz enter U Age")
                .setPositiveButton("OK",null)
                .show()
        }
    }
}
