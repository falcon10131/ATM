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
import kotlinx.android.synthetic.main.activity_age.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_nickname.*

class NicknameActivity : AppCompatActivity() {
    val SELECT_GANDER = 103
    var isEmpty = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        bt_connickname.setOnClickListener {
               enternickname()
        }

        getSharedPreferences("nickshare", Context.MODE_PRIVATE)
            .getString("nickname","")?.run {
                et_nickname.setText(this)
            }

//        if (et_nickname.text.toString() != "")
//        {
//            setResult(Activity.RESULT_OK)
//            val toGenger = Intent(this@NicknameActivity,GenderActivity::class.java)
//            startActivityForResult(toGenger,SELECT_GANDER)
//            finish()
//        }
    }

    private fun enternickname() {
        val nickname = et_nickname.text.toString()
        if (nickname != null) {
            getSharedPreferences("nickshare", Context.MODE_PRIVATE).edit()
                .putString("nickname", nickname)
                .apply()
            //Toast.makeText(this,"Nickname enter success", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            val toGenger = Intent(this@NicknameActivity,GenderActivity::class.java)
            startActivityForResult(toGenger,SELECT_GANDER)
            finish()
        }else{
            //if not enter nick name
            AlertDialog.Builder(this)
                .setTitle("NickName")
                .setMessage("Plz enter U Nickname")
                .setPositiveButton("OK",null)
                .show()
        }
    }
}
