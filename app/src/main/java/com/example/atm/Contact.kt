package com.example.atm

data class Contact(var name: String,
                   var phone: String){
    //空建構子,未來使用空建構子會自動連結到以上的建構屬性
    constructor() :this("","")
}
