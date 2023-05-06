package com.example.kotlin_bill.models

data class BillModel(
    var billId: String? = null,
    var billType: String? = null,
    var billAmount: String? = null,
    var billNotes: String? = null
)