package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val age: Int,
    val gender: String,
    val lookingFor: String,
    val bio: String,
    val interests: String, // Comma separated (e.g. "Travel,Cooking")
    val avatarId: Int, // Refers to selected avatar asset index
    val isOnboarded: Boolean = true
)

@Entity(tableName = "candidates")
data class Candidate(
    @PrimaryKey val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val job: String,
    val location: String,
    val bio: String,
    val interests: String, // Comma separated
    val avatarId: Int,
    val compatScore: Int, // Matching compatibility %
    val readyToMatch: Boolean = true // Will trigger instant match when liked
)

@Entity(tableName = "swipes")
data class Swipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val candidateId: Int,
    val isLiked: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val candidateId: Int,
    val text: String,
    val isFromMe: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val msgType: String = "TEXT", // "TEXT", "IMAGE", "VIDEO", "VOICE", "LOCATION", "DOCUMENT"
    val mediaUri: String? = null,
    val mediaDuration: Int = 0, // In seconds (for voice or video)
    val latitude: Double? = null,
    val longitude: Double? = null,
    val locationName: String? = null
)

@Entity(tableName = "match_relationships")
data class MatchRelationship(
    @PrimaryKey val candidateId: Int,
    val matchedAt: Long = System.currentTimeMillis()
)

