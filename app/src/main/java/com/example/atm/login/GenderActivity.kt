package com.example.atm.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.atm.R
import kotlinx.android.synthetic.main.activity_gender.*
import kotlinx.android.synthetic.main.activity_nickname.*

class GenderActivity : AppCompatActivity() {
    val SET_AGE = 104
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)
        getSharedPreferences("gendershare", Context.MODE_PRIVATE)
            .getString("gender", "")?.run {
                et_gender.setText(this)
            }
//        if (et_gender.text.toString() != "")
//        {
//            setResult(Activity.RESULT_OK)
//            val toGenger = Intent(this@GenderActivity,GenderActivity::class.java)
//            startActivityForResult(toGenger,SET_AGE)
//            finish()
//        }

        bt_congender.setOnClickListener {
            selectgender()
        }

    }

    private fun selectgender(){
        var gender = et_gender.text.toString()
        if (gender == "1") {gender = "male"}else if (gender == "2") {gender = "female"}
        if (gender != null){
            getSharedPreferences("gendershare", Context.MODE_PRIVATE).edit()
                .putString("gender", gender)
                .apply()
            //Toast.makeText(this,"Nickname enter success", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            Log.d("GENDER",gender)
            val toAge = Intent(this,AgeActivity::class.java)
            startActivityForResult(toAge,SET_AGE)
            finish()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Gender")
                .setMessage("Plz select U Gender")
                .setPositiveButton("OK",null)
                .show()
        }
    }
}
