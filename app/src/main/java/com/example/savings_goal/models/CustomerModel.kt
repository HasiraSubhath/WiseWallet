package com.example.savings_goal.models

data class CustomerModel(
    var id: String? = null,
    var saveGoal: String? = null,
    var moneyGoal: String? = null,
    var date: String? = null,
    var note: String? = null
)