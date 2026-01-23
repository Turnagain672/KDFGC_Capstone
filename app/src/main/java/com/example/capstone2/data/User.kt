package com.example.capstone2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val password: String,
    val fullName: String = "",
    val name: String = "",
    val memberNumber: String = "",
    val membershipType: String = "Pending",
    val isAdmin: Boolean = false,
    val phone: String = "",
    val palNumber: String = "",
    val registrationDate: Long = System.currentTimeMillis(),
    val isApproved: Boolean = false,
    val membershipExpiry: Long = 0L,
    val hasCFSC: Boolean = false,
    val cfscIssueDate: Long = 0L,
    val hasCRFSC: Boolean = false,
    val crfscIssueDate: Long = 0L,
    val hasNewMemberOrientation: Boolean = false,
    val orientationDate: Long = 0L,
    val hasRangeSafety: Boolean = false,
    val rangeSafetyDate: Long = 0L,
    val hasPAL: Boolean = false,
    val palIssueDate: Long = 0L,
    val palExpiry: Long = 0L,
    val hasRPAL: Boolean = false,
    val rpalIssueDate: Long = 0L,
    val rpalExpiry: Long = 0L,
    val hasHandgunSafetyQual: Boolean = false,
    val handgunSafetyExpiry: Long = 0L,
    val hasPistolQual: Boolean = false,
    val pistolQualExpiry: Long = 0L,
    val hasLiabilityWaiver: Boolean = false,
    val liabilityWaiverExpiry: Long = 0L,
    val hasRangeRulesAgreement: Boolean = false,
    val rangeRulesExpiry: Long = 0L,
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = ""
)

@Entity(tableName = "purchases")
data class Purchase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val itemName: String,
    val totalQuantity: Int,
    val remainingQuantity: Int,
    val price: String,
    val purchaseDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val documentType: String,
    val fileName: String,
    val filePath: String,
    val uploadDate: Long = System.currentTimeMillis(),
    val isVerified: Boolean = false,
    val verifiedByAdminId: Int? = null
)