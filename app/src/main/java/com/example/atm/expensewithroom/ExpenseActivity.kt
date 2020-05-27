package com.example.atm.expensewithroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.example.atm.R
import kotlinx.android.synthetic.main.activity_expense.*

class ExpenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        fab.setOnClickListener {
            Thread {
                //建立一個DataBase,
                val expenseDatabase = Room.databaseBuilder(
                    this@ExpenseActivity,
                    ExpenseDatabase::class.java,
                    "expense.db")
                    .build()
                expenseDatabase.expenseDAO().insert(Expense("2020-05-27","Breakfast",80))
                Log.d("expense","exOK")
            }.start() //Thread
        }//fab.setOnClickListener
    }
}
