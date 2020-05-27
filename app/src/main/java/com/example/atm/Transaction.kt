package com.example.atm
class Transaction : ArrayList<TransactionItem>()

data class TransactionItem(var account:String,
                       var date: String,
                       var amount: Int,
                       var type: Int)