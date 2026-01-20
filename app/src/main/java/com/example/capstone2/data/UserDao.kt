package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM users WHERE fullName LIKE '%' || :name || '%' AND isAdmin = 0")
    fun searchMembers(name: String): Flow<List<User>>

    @Query("SELECT * FROM users WHERE isAdmin = 0")
    fun getAllMembers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)
}

@Dao
interface PurchaseDao {
    @Query("SELECT * FROM purchases WHERE userId = :userId")
    fun getPurchasesByUser(userId: Int): Flow<List<Purchase>>

    @Query("SELECT * FROM purchases WHERE userId = :userId AND remainingQuantity > 0")
    fun getActivePurchases(userId: Int): Flow<List<Purchase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: Purchase): Long

    @Update
    suspend fun updatePurchase(purchase: Purchase)

    @Query("UPDATE purchases SET remainingQuantity = remainingQuantity - 1 WHERE id = :purchaseId AND remainingQuantity > 0")
    suspend fun deductOne(purchaseId: Int)
}