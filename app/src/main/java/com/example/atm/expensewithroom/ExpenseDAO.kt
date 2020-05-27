package com.example.atm.expensewithroom

import androidx.room.*

@Dao
interface ExpenseDAO {
    //@新增(後面是當key重複時該如何應對)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(expense: Expense)

    @Query("select * from expense")
    fun getAll() : List<Expense>

//    @Update
//    fun update()
//
//    @Delete
//    fun delete()
}