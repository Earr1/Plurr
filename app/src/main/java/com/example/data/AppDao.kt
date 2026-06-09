package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileDirect(): UserProfile?

    // Candidates
    @Query("SELECT * FROM candidates WHERE id NOT IN (SELECT candidateId FROM swipes) ORDER BY compatScore DESC")
    fun getDiscoverableCandidates(): Flow<List<Candidate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidates(candidates: List<Candidate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidate(candidate: Candidate)

    @Query("SELECT COUNT(*) FROM candidates")
    suspend fun getCandidateCount(): Int

    @Query("DELETE FROM candidates")
    suspend fun clearCandidates()

    @Query("DELETE FROM swipes")
    suspend fun clearSwipes()

    @Query("DELETE FROM match_relationships")
    suspend fun clearMatchRelationships()

    // Swipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSwipe(swipe: Swipe)

    @Query("SELECT * FROM swipes")
    fun getAllSwipes(): Flow<List<Swipe>>

    // Match Relationships
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchRelationship(match: MatchRelationship)

    @Query("DELETE FROM match_relationships WHERE candidateId = :candidateId")
    suspend fun deleteMatchRelationship(candidateId: Int)

    @Query("SELECT * FROM match_relationships")
    fun getAllMatchRelationships(): Flow<List<MatchRelationship>>

    // Matches (Derived from match relationships)
    @Query("SELECT * FROM candidates WHERE id IN (SELECT candidateId FROM match_relationships)")
    fun getMatchedCandidates(): Flow<List<Candidate>>

    @Query("SELECT * FROM candidates WHERE id = :id LIMIT 1")
    suspend fun getCandidateById(id: Int): Candidate?

    // Messages
    @Query("SELECT * FROM messages WHERE candidateId = :candidateId ORDER BY timestamp ASC")
    fun getMessagesForCandidate(candidateId: Int): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages WHERE candidateId = :candidateId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestMessage(candidateId: Int): Flow<Message?>
}
