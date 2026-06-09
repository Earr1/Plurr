package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Candidate
import com.example.data.Message
import com.example.data.SparkRepository
import com.example.data.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SparkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SparkRepository
    
    val userProfile: StateFlow<UserProfile?>
    val discoverableCandidates: StateFlow<List<Candidate>>
    val matchedCandidates: StateFlow<List<Candidate>>

    // Audio or Visual match celebrate overlay state
    private val _matchCelebrationCandidate = MutableStateFlow<Candidate?>(null)
    val matchCelebrationCandidate: StateFlow<Candidate?> = _matchCelebrationCandidate.asStateFlow()

    // Active screen navigation helper
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Swipe)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Active partner ID for Chat Room
    private val _activeChatCandidateId = MutableStateFlow<Int?>(null)
    val activeChatCandidateId: StateFlow<Int?> = _activeChatCandidateId.asStateFlow()

    private val _activeChatCandidate = MutableStateFlow<Candidate?>(null)
    val activeChatCandidate: StateFlow<Candidate?> = _activeChatCandidate.asStateFlow()

    // Observe active messaging dialogue list reactively
    val activeChatMessages: StateFlow<List<Message>> = _activeChatCandidateId
        .flatMapLatest { id ->
            if (id != null) repository.getMessages(id) else flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isPartnerTyping = MutableStateFlow(false)
    val isPartnerTyping: StateFlow<Boolean> = _isPartnerTyping.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = SparkRepository(database.appDao())

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        discoverableCandidates = repository.discoverableCandidates.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        matchedCandidates = repository.matchedCandidates.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Seed initial data and auto navigate if onboarding required
        viewModelScope.launch {
            repository.seedCandidatesIfEmpty()
        }
    }

    fun onboardUser(name: String, age: Int, gender: String, lookingFor: String, bio: String, interests: String, avatarId: Int) {
        viewModelScope.launch {
            val newUser = UserProfile(
                name = name,
                age = age,
                gender = gender,
                lookingFor = lookingFor,
                bio = bio,
                interests = interests,
                avatarId = avatarId,
                isOnboarded = true
            )
            repository.saveUserProfile(newUser)
            repository.clearAndReSeedCandidates()
            _currentScreen.value = Screen.Swipe
        }
    }

    fun updateUserProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            repository.clearAndReSeedCandidates()
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun openChatForCandidate(candidate: Candidate) {
        _activeChatCandidateId.value = candidate.id
        _activeChatCandidate.value = candidate
        _currentScreen.value = Screen.Chat
    }

    fun swipe(candidate: Candidate, isLiked: Boolean) {
        viewModelScope.launch {
            repository.swipeCandidate(candidate.id, isLiked)
            if (isLiked && candidate.readyToMatch) {
                // Show celebration layout overlay
                _matchCelebrationCandidate.value = candidate
                // Automatically insert an icebreaker message from the candidate!
                val icebreakers = listOf(
                    "Hey there! I'm so excited we matched! Your profile is awesome. 😊 Let's chat!",
                    "Hi! Great matching with you. Tell me, what's your absolute dream weekend plan? ✨",
                    "Hey! So glad we swiped right on each other. 😊 How is your day going?",
                    "It's a match! 🥂 Let's skip the small talk—recommend an obscure song or a secret café?"
                )
                repository.insertMessage(candidate.id, icebreakers.random(), isFromMe = false)
            }
        }
    }

    fun dismissMatchCelebration() {
        _matchCelebrationCandidate.value = null
    }

    fun sendMessage(text: String) {
        val candidateId = _activeChatCandidateId.value ?: return
        if (text.trim().isEmpty()) return

        viewModelScope.launch {
            // Save user instruction
            repository.insertMessage(candidateId, text, isFromMe = true)
            
            // Set simulated typing state for realism
            _isPartnerTyping.value = true
            delay(1800) // Aesthetic visual pause
            
            // Call Gemini API or offline generator
            val reply = repository.getAiReply(candidateId, text)
            _isPartnerTyping.value = false
            
            // Insert partner reply
            repository.insertMessage(candidateId, reply, isFromMe = false)
        }
    }

    fun getLatestMessage(candidateId: Int): Flow<Message?> {
        return repository.getLatestMessage(candidateId)
    }
}

sealed class Screen {
    object Onboarding : Screen()
    object Swipe : Screen()
    object Matches : Screen()
    object MyProfile : Screen()
    object Chat : Screen()
}
