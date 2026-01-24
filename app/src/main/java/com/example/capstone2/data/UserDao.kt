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

    @Query("SELECT * FROM users WHERE isAdmin = 0")
    suspend fun getAllMembersList(): List<User>

    @Query("SELECT * FROM users ORDER BY fullName ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE role = :role")
    fun getUsersByRole(role: String): Flow<List<User>>

    @Query("SELECT * FROM users WHERE role = 'moderator'")
    fun getModerators(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE isAdmin = 1 OR role = 'admin'")
    fun getAdmins(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    @Query("UPDATE users SET role = :role WHERE id = :userId")
    suspend fun updateUserRole(userId: Int, role: String)

    @Query("UPDATE users SET isAdmin = :isAdmin, role = :role WHERE id = :userId")
    suspend fun setAdminStatus(userId: Int, isAdmin: Boolean, role: String)

    @Query("UPDATE users SET membershipType = :status WHERE id = :userId")
    suspend fun updateMembershipType(userId: Int, status: String)

    @Query("UPDATE users SET phone = :phone, palNumber = :palNumber WHERE id = :userId")
    suspend fun updateMemberInfo(userId: Int, phone: String, palNumber: String)

    @Query("UPDATE users SET hasPAL = :value, palIssueDate = :issueDate, palExpiry = :expiry WHERE id = :userId")
    suspend fun updatePAL(userId: Int, value: Boolean, issueDate: Long, expiry: Long)

    @Query("UPDATE users SET hasRPAL = :value, rpalIssueDate = :issueDate, rpalExpiry = :expiry WHERE id = :userId")
    suspend fun updateRPAL(userId: Int, value: Boolean, issueDate: Long, expiry: Long)

    @Query("UPDATE users SET hasCFSC = :value, cfscIssueDate = :issueDate WHERE id = :userId")
    suspend fun updateCFSC(userId: Int, value: Boolean, issueDate: Long)

    @Query("UPDATE users SET hasCRFSC = :value, crfscIssueDate = :issueDate WHERE id = :userId")
    suspend fun updateCRFSC(userId: Int, value: Boolean, issueDate: Long)

    @Query("UPDATE users SET hasNewMemberOrientation = :value, orientationDate = :issueDate WHERE id = :userId")
    suspend fun updateOrientation(userId: Int, value: Boolean, issueDate: Long)

    @Query("UPDATE users SET hasRangeSafety = :value, rangeSafetyDate = :issueDate WHERE id = :userId")
    suspend fun updateRangeSafety(userId: Int, value: Boolean, issueDate: Long)

    @Query("UPDATE users SET hasHandgunSafetyQual = :value, handgunSafetyExpiry = :expiry WHERE id = :userId")
    suspend fun updateHandgunSafetyQual(userId: Int, value: Boolean, expiry: Long)

    @Query("UPDATE users SET hasPistolQual = :value, pistolQualExpiry = :expiry WHERE id = :userId")
    suspend fun updatePistolQual(userId: Int, value: Boolean, expiry: Long)

    @Query("UPDATE users SET hasLiabilityWaiver = :value, liabilityWaiverExpiry = :expiry WHERE id = :userId")
    suspend fun updateLiabilityWaiver(userId: Int, value: Boolean, expiry: Long)

    @Query("UPDATE users SET hasRangeRulesAgreement = :value, rangeRulesExpiry = :expiry WHERE id = :userId")
    suspend fun updateRangeRulesAgreement(userId: Int, value: Boolean, expiry: Long)

    @Query("UPDATE users SET membershipExpiry = :expiry WHERE id = :userId")
    suspend fun updateMembershipExpiry(userId: Int, expiry: Long)

    @Query("UPDATE users SET isApproved = :approved WHERE id = :userId")
    suspend fun updateApprovalStatus(userId: Int, approved: Boolean)

    @Query("SELECT * FROM users WHERE isAdmin = 0 AND (palExpiry > 0 AND palExpiry < :threshold)")
    fun getMembersWithExpiringPAL(threshold: Long): Flow<List<User>>

    @Query("SELECT * FROM users WHERE isAdmin = 0 AND (handgunSafetyExpiry > 0 AND handgunSafetyExpiry < :threshold)")
    fun getMembersWithExpiringHandgunSafety(threshold: Long): Flow<List<User>>

    @Query("SELECT * FROM users WHERE isAdmin = 0 AND (membershipExpiry > 0 AND membershipExpiry < :threshold)")
    fun getMembersWithExpiringMembership(threshold: Long): Flow<List<User>>
}

@Dao
interface PurchaseDao {
    @Query("SELECT * FROM purchases WHERE userId = :userId")
    fun getPurchasesByUser(userId: Int): Flow<List<Purchase>>

    @Query("SELECT * FROM purchases WHERE userId = :userId AND remainingQuantity > 0")
    fun getActivePurchases(userId: Int): Flow<List<Purchase>>

    @Query("SELECT * FROM purchases ORDER BY purchaseDate DESC")
    fun getAllPurchases(): Flow<List<Purchase>>

    @Query("SELECT * FROM purchases ORDER BY purchaseDate DESC LIMIT 20")
    fun getRecentPurchases(): Flow<List<Purchase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: Purchase): Long

    @Update
    suspend fun updatePurchase(purchase: Purchase)

    @Query("UPDATE purchases SET remainingQuantity = remainingQuantity - 1 WHERE id = :purchaseId AND remainingQuantity > 0")
    suspend fun deductOne(purchaseId: Int)

    @Delete
    suspend fun deletePurchase(purchase: Purchase)

    @Query("DELETE FROM purchases WHERE id = :purchaseId")
    suspend fun refundPurchase(purchaseId: Int)
}