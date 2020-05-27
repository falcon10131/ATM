package com.example.atm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.atm.login.MainActivity
import kotlinx.android.synthetic.main.activity_bmi.*

class BmiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)
        bt_calulate.setOnClickListener { calculate() }
    }

    private fun calculate(){
        val h = et_height.text.toString().toInt()/100
        val w = et_weight.text.toString().toInt()
        val bmi = w / (h*h)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
