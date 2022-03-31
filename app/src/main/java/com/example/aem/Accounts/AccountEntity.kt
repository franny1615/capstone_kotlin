package com.example.aem.Accounts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_account_table")
class AccountEntity(val itemId: String, val accessToken: String, val institution: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}