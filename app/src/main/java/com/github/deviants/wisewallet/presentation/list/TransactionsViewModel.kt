package com.github.deviants.wisewallet.presentation.list

import androidx.lifecycle.*
import com.github.deviants.wisewallet.data.transaction.Transaction
import com.github.deviants.wisewallet.data.transaction.Transaction.Companion.EXPENSE
import com.github.deviants.wisewallet.data.transaction.Transaction.Companion.INCOME
import com.github.deviants.wisewallet.data.transaction.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class TransactionsViewModel(private val transactionDao: TransactionDao) : ViewModel() {

    var allTransactions: LiveData<List<Transaction>> = transactionDao.getItems().asLiveData()


    var allIncomes: LiveData<List<Transaction>> = getByType(INCOME).asLiveData()
    var allExpenses: LiveData<List<Transaction>> = getByType(EXPENSE).asLiveData()

    private fun insertItem(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.insert(transaction)
        }
    }

    private fun getNewItemEntry(
        transactionTransactionType: String,
        transactionCategory: String,
        transactionPrice: String,
        isCompulsory: String,
        date: String,
        transactionDescription: String,
    ): Transaction {
        return Transaction(
            transactionType = transactionTransactionType,
            transactionCategory = transactionCategory,
            transactionPrice = transactionPrice.toDouble(),
            isCompulsory = isCompulsory.toBoolean(),
            date = date.toLong(),
            transactionDescription = transactionDescription
        )
    }

    fun addNewItem(
        transactionTransactionType: String,
        transactionCategory: String,
        transactionPrice: String,
        transactionCount: String,
        date: String,
        transactionDescription: String,
    ) {
        val transaction = getNewItemEntry(
            transactionTransactionType,
            transactionCategory,
            transactionPrice,
            transactionCount,
            date,
            if(transactionDescription.trim().isNotBlank()) transactionDescription else transactionCategory
        ).also {
            insertItem(it)
        }
    }

    fun isEntryValid(transactionCategory: String, transactionPrice: String, date: String): Boolean {
        if (transactionCategory.isBlank() || transactionPrice.isBlank() || date.isBlank()
            || transactionPrice.length > 9 || transactionPrice == ".") {
            return false
        }
        return true
    }

    fun retrieveItem(id: Int): LiveData<Transaction> {
        return transactionDao.getItem(id).asLiveData()
    }

    private fun getByType(type: String): Flow<List<Transaction>> {
        return transactionDao.getItemsByType(type)
    }

    private fun updateItem(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.update(transaction)
        }
    }

    fun deleteItem(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.delete(transaction)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.deleteAll()
        }
    }

    private fun getUpdatedItemEntry(
        transactionId: Int,
        transactionTransactionType: String,
        transactionCategory: String,
        transactionPrice: String,
        isCompulsory: String,
        date: String,
        transactionDescription: String,
    ): Transaction {
        return Transaction(
            id = transactionId,
            transactionType = transactionTransactionType,
            transactionCategory = transactionCategory,
            transactionPrice = transactionPrice.toDouble(),
            isCompulsory = isCompulsory.toBoolean(),
            date = date.toLong(),
            transactionDescription = transactionDescription
        )
    }

    fun updateItem(
        transactionId: Int,
        transactionType: String,
        transactionCategory: String,
        transactionPrice: String,
        transactionCount: String,
        date: String,
        transactionDescription: String,
    ) {
        val updatedItem = getUpdatedItemEntry(
            transactionId,
            transactionType,
            transactionCategory,
            transactionPrice,
            transactionCount,
            date,
            transactionDescription
        ).also {
            updateItem(it)
        }
    }

    fun replaceList(type: String): LiveData<List<Transaction>> {
        return when(type) {
            INCOME -> allIncomes
            EXPENSE -> allExpenses
            else -> allTransactions
        }
    }

    fun correctDataTitle(data: List<Transaction>?): MutableList<Transaction> {
        var tempData = -1L
        val newList: MutableList<Transaction> = mutableListOf()
        data?.let {
            for (item in data) {
                if (item.date / 86400000 * 86400000 != tempData) {
                    newList.add(item.copy(isFirstInDay = true))
                    tempData = item.date / 86400000 * 86400000
                } else newList.add(item.copy())
            }
        }
        return newList
    }
}


class ExpensesViewModelFactory(private val transactionDao: TransactionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}