package com.example.capstone2.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone2.data.AppDatabase
import com.example.capstone2.data.Purchase
import com.example.capstone2.data.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val userDao = db.userDao()
    private val purchaseDao = db.purchaseDao()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()

    init {
        viewModelScope.launch {
            val admin = userDao.getUserByEmail("admin@kdfgc.org")
            if (admin == null) {
                userDao.insertUser(
                    User(
                        email = "admin@kdfgc.org",
                        password = "admin123",
                        fullName = "KDFGC Admin",
                        memberNumber = "ADMIN001",
                        isAdmin = true
                    )
                )
            }
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        val user = userDao.login(email, password)
        _currentUser.value = user
        return user != null
    }

    suspend fun register(email: String, password: String, fullName: String, memberNumber: String): Boolean {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) return false

        val userId = userDao.insertUser(
            User(
                email = email,
                password = password,
                fullName = fullName,
                memberNumber = memberNumber,
                isAdmin = false
            )
        )
        _currentUser.value = userDao.getUserById(userId.toInt())
        return true
    }

    fun logout() {
        _currentUser.value = null
    }

    fun searchMembers(name: String) {
        viewModelScope.launch {
            userDao.searchMembers(name).collect { users ->
                _searchResults.value = users
            }
        }
    }

    fun getAllMembers() {
        viewModelScope.launch {
            userDao.getAllMembers().collect { users ->
                _searchResults.value = users
            }
        }
    }

    fun getPurchasesForUser(userId: Int): Flow<List<Purchase>> {
        return purchaseDao.getActivePurchases(userId)
    }

    fun addPurchase(userId: Int, itemName: String, quantity: Int, price: String) {
        viewModelScope.launch {
            purchaseDao.insertPurchase(
                Purchase(
                    userId = userId,
                    itemName = itemName,
                    totalQuantity = quantity,
                    remainingQuantity = quantity,
                    price = price
                )
            )
        }
    }

    fun deductPurchase(purchaseId: Int) {
        viewModelScope.launch {
            purchaseDao.deductOne(purchaseId)
        }
    }
}