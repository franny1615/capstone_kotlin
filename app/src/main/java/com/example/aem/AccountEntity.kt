package com.example.aem

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_account_table")
class AccountEntity {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var itemId: String? = null
    var accessToken: String? = null
    var institution: String? = null
}