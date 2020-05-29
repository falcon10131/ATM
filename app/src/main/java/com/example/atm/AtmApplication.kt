package com.example.atm

import android.app.Application
//繼承Application 這邊可以用全域型變數
class AtmApplication : Application(){
    var data = 55
    var user: User = User()
}

class User {
    var userid: String? = null
    var nickname: String? = null
    var age: Int? = null
    var gender: Int? = null
    fun isLogon(): Boolean {
        if (userid == null) return false else return true

    }
}