package com.example.atm.expensewithroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.atm.ContactAdapter
import com.example.atm.R
import kotlinx.android.synthetic.main.activity_expense.*
import kotlinx.android.synthetic.main.row_expense.view.*
import kotlinx.coroutines.*

class ExpenseActivity : AppCompatActivity() {
    val job = Job()
    private lateinit var adapter: ExpenseRecyclerViewAdapter
    private lateinit var expenseDatabase: ExpenseDatabase
    private var expenses = listOf<Expense>()
    private var testData = listOf<Expense>(
        Expense("2020-05-24","早餐",70),
        Expense("2020-05-25","停車費",20),
        Expense("2020-05-27","約會",750),
        Expense("2020-05-29","咖啡",50)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        //建立一個DataBase,
        expenseDatabase = Room.databaseBuilder(
            this@ExpenseActivity,
            ExpenseDatabase::class.java,
            "expense.db")
            .build()
        fab.setOnClickListener {
            CoroutineScope(Dispatchers.IO + job).launch {
                //從it->expenses 變成expense->expenses
                testData.forEach {expense ->
                    expenseDatabase.expenseDAO()
                        .insert(expense)
                    delay(3000)
                }
            }
        }//fab.setOnClickListener
        CoroutineScope(Dispatchers.IO).launch {
            //expenses = expenseDatabase.expenseDAO().getAll()
            withContext(Dispatchers.Main){
                rcv.setHasFixedSize(true)
                rcv.layoutManager = LinearLayoutManager(this@ExpenseActivity)
                adapter = ExpenseRecyclerViewAdapter()
                rcv.adapter = adapter
            }
        }
    }
    //RecyclerAdapter
    inner class ExpenseRecyclerViewAdapter :RecyclerView.Adapter<ExpenseHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseHolder {
            return ExpenseHolder(layoutInflater.inflate(R.layout.row_expense,parent,false))
        }

        override fun getItemCount(): Int {
            return if (testData.isNotEmpty()) testData.size else 0
        }

        override fun onBindViewHolder(holder: ExpenseHolder, position: Int) {
            holder.bindTo(expenses.get(position))
            //Log.d("show","${testData[position].date} ${testData[position].info} ${testData[position].amount}")
        }
    }


   inner class ExpenseHolder(view:View) : RecyclerView.ViewHolder(view){
       fun bindTo(expense: Expense){
            dateText.setText(expense.date)
            infoText.setText(expense.info)
            amountText.setText(expense.amount)

       }
       val dateText = view.extxt_date
        val infoText = view.extxt_info
        val amountText = view.extxt_amount
    }
}
