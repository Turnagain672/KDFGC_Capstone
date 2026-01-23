package com.example.capstone2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents WHERE userId = :userId ORDER BY uploadDate DESC")
    fun getDocumentsForUser(userId: Int): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE isVerified = 0 ORDER BY uploadDate DESC")
    fun getUnverifiedDocuments(): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE documentType = :type AND userId = :userId")
    suspend fun getDocumentByType(userId: Int, type: String): Document?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document): Long

    @Query("UPDATE documents SET isVerified = 1, verifiedByAdminId = :adminId WHERE id = :docId")
    suspend fun verifyDocument(docId: Int, adminId: Int)

    @Delete
    suspend fun deleteDocument(document: Document)
}