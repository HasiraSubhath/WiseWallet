package com.github.deviants.wisewallet.presentation

import android.app.Application
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val app: Application
): ViewModelProvider.Factory {
/* TODO()
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val viewModel = when(modelClass) {
                TransactionsViewModel::class.java -> {
                    TransactionsViewModel(transactionDao = TransactionDao)
                }
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/
}