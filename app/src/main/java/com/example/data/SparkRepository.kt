package com.example.data

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class SparkRepository(private val appDao: AppDao) {

    val userProfile: Flow<UserProfile?> = appDao.getUserProfile()
    val discoverableCandidates: Flow<List<Candidate>> = appDao.getDiscoverableCandidates()
    val matchedCandidates: Flow<List<Candidate>> = appDao.getMatchedCandidates()

    suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        appDao.insertUserProfile(profile)
    }

    fun getMessages(candidateId: Int): Flow<List<Message>> {
        return appDao.getMessagesForCandidate(candidateId)
    }

    fun getLatestMessage(candidateId: Int): Flow<Message?> {
        return appDao.getLatestMessage(candidateId)
    }

    suspend fun swipeCandidate(candidateId: Int, isLiked: Boolean) = withContext(Dispatchers.IO) {
        val swipe = Swipe(candidateId = candidateId, isLiked = isLiked)
        appDao.insertSwipe(swipe)
    }

    suspend fun insertMessage(
        candidateId: Int,
        text: String,
        isFromMe: Boolean,
        msgType: String = "TEXT",
        mediaUri: String? = null,
        mediaDuration: Int = 0,
        latitude: Double? = null,
        longitude: Double? = null,
        locationName: String? = null
    ) = withContext(Dispatchers.IO) {
        val message = Message(
            candidateId = candidateId,
            text = text,
            isFromMe = isFromMe,
            msgType = msgType,
            mediaUri = mediaUri,
            mediaDuration = mediaDuration,
            latitude = latitude,
            longitude = longitude,
            locationName = locationName
        )
        appDao.insertMessage(message)
    }

    suspend fun seedCandidatesIfEmpty() = withContext(Dispatchers.IO) {
        val user = appDao.getUserProfileDirect()
        val preference = user?.lookingFor ?: "Everyone"
        val count = appDao.getCandidateCount()
        if (count == 0) {
            val list = generateDynamicCandidates(preference)
            appDao.insertCandidates(list)
        }
    }

    suspend fun clearAndReSeedCandidates() = withContext(Dispatchers.IO) {
        appDao.clearCandidates()
        appDao.clearSwipes()
        seedCandidatesIfEmpty()
    }

    suspend fun clearCandidatesAndReset() = withContext(Dispatchers.IO) {
        appDao.clearCandidates()
        appDao.clearSwipes()
    }

    suspend fun insertCandidate(candidate: Candidate) = withContext(Dispatchers.IO) {
        appDao.insertCandidate(candidate)
    }

    private fun generateDynamicCandidates(lookingFor: String): List<Candidate> {
        val femalePool = listOf(
            Triple("Aria Chen", "UI/UX Designer", "UX designer by day, pottery creator by night. Coffee runs on high speed, and indoor plants keep me busy! Looking for someone who enjoys aesthetic design and spontaneous road trips."),
            Triple("Chloe Jenkins", "Wildlife Biologist", "National parks are basically my living room. I hike high peaks, sketch wildlife, and love campfires under starry skies. Let's swap campfire ghost stories!"),
            Triple("Zoe Sterling", "Yoga & Mindfulness Coach", "Curating positive vibes and sunny horizons. Morning sunrise flows, organic green juices, and cozy matcha teas. Let's find some serene nature spots together."),
            Triple("Maya Patel", "Indie Singer-Songwriter", "Creating soulful acoustic melodies and soft poetry. If you can recommend an obscure indie cassette or play the drums, we'll get along instantly! Live shows are my absolute home."),
            Triple("Elena Rostova", "Freelance Illustrator", "Always drawing life's small details. Lover of hot black coffee, vintage stationery, film photography, and cozy rainy afternoons with a brilliant novel."),
            Triple("Sophia Martinez", "Artisanal Chef", "I speak fluent marinara sauce, custom pastry recipes, and spice gardens. Let's run a kitchen experiment, bake fresh focaccia, or check out local markets!"),
            Triple("Diana Prince", "Athletic Co-Trainer", "Sunrise runner, obstacle course competitor, and wellness advisor. Let's challenge each other to a mountain path sprint or try bouldering some Sunday morning!"),
            Triple("Clara Oswald", "Travel Photojournalist", "Wanderlust is a massive understatement. Visited over 20 countries capturing rare moments. Let's share some vintage travel logs or plan our next wild departure!"),
            Triple("Luna Love", "Astrology & Tarot Blogger", "Reading mystic planetary alignment charts and brewing custom herbal tea blends. Let's explore star signs, old bookstores, or vintage thrifting!"),
            Triple("Isabella Cruz", "Historic Preservationist", "Obsessed with vintage brick structures, mid-century design, and warm vinyl jazz records. Let's grab a smooth latte and talk architectural secrets."),
            Triple("Mia Wong", "Front-end Software Engineer", "Authoring responsive open-source web tools and checking out rare coffee stalls. Down to play a high-stakes board game or show off a custom gaming rig."),
            Triple("Natasha Romanoff", "Bilingual Literature Translator", "Lover of foreign narrative translations, combat sport training, and spicy noodles. Let's drop by a secret underground comic book spot or catch a film!")
        )

        val malePool = listOf(
            Triple("Marcus Vance", "Artisan Baker", "I express culinary love through sourdough bubbling starters and hand-stamped ravioli. When not tasting batch dough, I'm stargazing or hiking high cliffs."),
            Triple("Leo Kowalski", "Indie Game Developer", "Retro visualizer, code wrangler, and synthwave listener. Collector of antique physical arcade boards. Looking for a Player 2 who can beat me in Mario Kart!"),
            Triple("Ethan Brooks", "Historical Architect", "Passionate about classical brickwork, architectural sketches, and obscure bookstores. Let's explore some gothic museums on a soft, rainy Tuesday."),
            Triple("Lucas Miller", "Fitness Coach & Nutritionist", "Always active. High-energy trails, functional bodyweight training, and meal prep experiments with fresh greens. Looking for a dedicated workout buddy!"),
            Triple("Liam Fitzpatrick", "Record Store Manager", "Vinyl enthusiast, classic cassette tapes curator, and indie show traveler. Tell me your top three desert-island albums or join me front row!"),
            Triple("Noah Alston", "Eco-Barista & Poet", "Serving single-origin organic pour-overs and writing typewriter nature prose. Coffee by day, open mic sessions by night. Let's talk favorite verses."),
            Triple("Oliver Queen", "Boutique Coffee Roaster", "Sourcing organic beans globally and roasting them locally. Fan of green trail runs, archery, and hyper-clean minimalist designs."),
            Triple("Benjamin Frank", "Landscape Sculptor", "Transforming residential backyards into visual zen gardens and koi ponds. Working with rock arrangements, bonsai trees, and running streams."),
            Triple("Henry Cavill", "Tactical Sound Designer", "Synthesizing custom audio soundscapes for immersive strategy games. When disconnected, I'm spoiling my golden retriever or picking fresh records."),
            Triple("Julian Casablan", "Indie Bass Guitarist", "Touring around local dive shows and collecting distressed leather items. Let's talk garage indie scene or capture standard acoustic lines."),
            Triple("William Tell", "Wildlife Photojournalist", "Observing wildlife in deep cedar forests and photographing rare bird species. Lover of camping, canoes, and cozy fires under open skies."),
            Triple("James Carter", "Custom Bicycle Builder", "Hand-welding retro steel frames. Let's head out for an evening city cruise, check out vintage tracks, or grab a craft cold IPA!")
        )

        val interestsMap = mapOf(
            "UI/UX Designer" to "Design,Art,Coffee,Photography",
            "Wildlife Biologist" to "Nature,Animals,Hiking,Camping",
            "Yoga & Mindfulness Coach" to "Yoga,Wellness,Matcha,Reading",
            "Indie Singer-Songwriter" to "Music,Concerts,Poetry,Vinyl",
            "Freelance Illustrator" to "Art,Drawing,Books,Coffee",
            "Artisanal Chef" to "Cooking,Baking,Food,Travel",
            "Athletic Co-Trainer" to "Fitness,Running,Outdoors,Wellness",
            "Travel Photojournalist" to "Travel,Photography,Art,Nature",
            "Astrology & Tarot Blogger" to "Wellness,Books,Art,Nature",
            "Historic Preservationist" to "Architecture,History,Jazz,Design",
            "Front-end Software Engineer" to "Coding,Gaming,Tech,Matcha",
            "Bilingual Literature Translator" to "Books,History,Fitness,Travel",
            "Artisan Baker" to "Cooking,Baking,Hiking,Nature",
            "Indie Game Developer" to "Coding,Gaming,Tech,Synthwave",
            "Historical Architect" to "Architecture,History,Books,Jazz",
            "Fitness Coach & Nutritionist" to "Fitness,Running,Nutrition,Outdoors",
            "Record Store Manager" to "Music,Concerts,Vinyl,Coffee",
            "Eco-Barista & Poet" to "Coffee,Poetry,Music,Books",
            "Boutique Coffee Roaster" to "Coffee,Outdoors,Design,Art",
            "Landscape Sculptor" to "Nature,Design,Art,Hiking",
            "Tactical Sound Designer" to "Gaming,Music,Animals,Fitness",
            "Indie Bass Guitarist" to "Music,Concerts,Guitar,Vinyl",
            "Wildlife Photojournalist" to "Nature,Animals,Camping,Photography",
            "Custom Bicycle Builder" to "Outdoors,Fitness,Coffee,Crafts"
        )

        val selectedPool = when (lookingFor) {
            "Female" -> femalePool.map { Triple(it.first, it.second, Pair(it.third, "Female")) }
            "Male" -> malePool.map { Triple(it.first, it.second, Pair(it.third, "Male")) }
            else -> {
                (femalePool.map { Triple(it.first, it.second, Pair(it.third, "Female")) } +
                 malePool.map { Triple(it.first, it.second, Pair(it.third, "Male")) })
            }
        }.shuffled()

        // Take up to 10 unique candidate profiles
        val numToGenerate = minOf(12, selectedPool.size)
        val candidatesList = mutableListOf<Candidate>()

        for (i in 0 until numToGenerate) {
            val element = selectedPool[i]
            val name = element.first
            val job = element.second
            val bio = element.third.first
            val gender = element.third.second
            val interests = interestsMap[job] ?: "Coffee,Art,Music,Outdoors"
            val dist = String.format("%.1f miles away", (12..78).random() / 10.0)
            val age = (21..34).random()
            val score = (82..97).random()
            val avatarId = (0..11).random() // Choose from 12 avatars

            candidatesList.add(
                Candidate(
                    id = i + 1,
                    name = name,
                    age = age,
                    gender = gender,
                    job = job,
                    location = dist,
                    bio = bio,
                    interests = interests,
                    avatarId = avatarId,
                    compatScore = score
                )
            )
        }

        return candidatesList
    }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getAiReply(candidateId: Int, userMsg: String): String = withContext(Dispatchers.IO) {
        val candidate = appDao.getCandidateById(candidateId) ?: return@withContext "Hey! I'm here."
        val userProfile = appDao.getUserProfile()
            // Room yields Flow, let's extract current value safely
            // Using a simple blocking call since we are on Dispatchers.IO
            // but we can query standard direct suspend function if necessary.
            // Let's fallback gracefully.
        
        val apiKey = BuildConfig.GEMINI_API_KEY
        val hasGemini = apiKey.isNotEmpty() && apiKey != "MY_GEMINI_API_KEY"

        if (hasGemini) {
            try {
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val systemPrompt = """
                    You are ${candidate.name}, a ${candidate.age} year old ${candidate.gender} working as a ${candidate.job}.
                    Here is your personal biography: ${candidate.bio}
                    Your personal interests: ${candidate.interests}
                    
                    Respond to the user's latest dating message in character. Keep the tone conversational, engaging, flirting, warm, and natural (between 1 to 3 sentences). Refer to your interests when natural, ask them questions about themselves, and match their level of enthusiasm. Never write listicles or speak like an AI model.
                """.trimIndent()

                val payload = """
                    {
                      "contents": [
                        {
                          "parts": [
                            {"text": "$userMsg"}
                          ]
                        }
                      ],
                      "systemInstruction": {
                        "parts": [
                          {"text": ${escapeJsonText(systemPrompt)}}
                        ]
                      },
                      "generationConfig": {
                        "temperature": 0.8,
                        "maxOutputTokens": 150
                      }
                    }
                """.trimIndent()

                val request = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                    .post(payload.toRequestBody(mediaType))
                    .build()

                Log.d("SparkRepository", "Calling Gemini API to get reply from ${candidate.name}")
                val response = httpClient.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""
                Log.d("SparkRepository", "Gemini response: $responseBody")

                if (response.isSuccessful && responseBody.isNotEmpty()) {
                    val extracted = extractTextFromJson(responseBody)
                    if (extracted.isNotEmpty()) {
                        return@withContext extracted
                    }
                }
            } catch (e: Exception) {
                Log.e("SparkRepository", "Error fetching Gemini AI response", e)
            }
        }

        // Offline Simulator Fallback System
        // Generate delightful replies customized based on interests and the prompt
        return@withContext getOfflineReply(candidate, userMsg)
    }

    private fun escapeJsonText(text: String): String {
        return "\"" + text.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r") + "\""
    }

    private fun extractTextFromJson(json: String): String {
        try {
            val index = json.indexOf("\"text\":")
            if (index != -1) {
                val start = json.indexOf("\"", index + 7)
                if (start != -1) {
                    val end = json.indexOf("\"", start + 1)
                    if (end != -1) {
                        val rawText = json.substring(start + 1, end)
                        return rawText.replace("\\n", "\n")
                            .replace("\\\"", "\"")
                            .replace("\\\\", "\\")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SparkRepository", "JSON extract error", e)
        }
        return ""
    }

    private fun getOfflineReply(candidate: Candidate, msg: String): String {
        val lower = msg.lowercase()
        val interests = candidate.interests.split(",")

        return when {
            lower.contains("hello") || lower.contains("hi") || lower.contains("hey") -> {
                listOf(
                    "Hey there! Ready to get to know each other? Your profile looked super interesting! 😊",
                    "Hi! So glad we matched. How's your week going so far? ✨",
                    "Hey! I was hoping we'd match. What caught your eye about my profile? ☕"
                ).random()
            }
            lower.contains("sourdough") || lower.contains("bake") || lower.contains("cooking") || lower.contains("food") -> {
                if (interests.contains("Baking") || interests.contains("Cooking")) {
                    "Cooking and baking are my absolute therapy! I just made a golden sourdough yesterday. What's your top comfort food to eat?"
                } else {
                    "Yum! I love food adventures. Let's explore a secret street food spot some weekend!"
                }
            }
            lower.contains("design") || lower.contains("ui") || lower.contains("pottery") || lower.contains("art") -> {
                if (interests.contains("Design") || interests.contains("Art")) {
                    "Art feeds the soul! I'm actually sculpting a clay vase right now. Do you paint, draw, or collect physical design books?"
                } else {
                    "Aesthetic design changes everything! Tell me, what's your absolute favorite place in the city to find creative inspiration?"
                }
            }
            lower.contains("game") || lower.contains("mario") || lower.contains("kart") || lower.contains("coder") || lower.contains("coding") -> {
                if (interests.contains("Gaming") || interests.contains("Coding")) {
                    "Ah! A kindred coder. I've been refining my arcade graphics engine all week! Down to play a retro Street Fighter round sometime? 🕹️"
                } else {
                    "That sounds super fun! I enjoy cozy gaming nights with some hot tea on rainy evenings."
                }
            }
            lower.contains("hike") || lower.contains("mountain") || lower.contains("trail") || lower.contains("nature") -> {
                if (interests.contains("Hiking") || interests.contains("Nature")) {
                    "Oh, you enjoy nature too? I'm planning a sunrise trek this weekend. Have you been to any national parks lately?"
                } else {
                    "Hiking in the woods is so peaceful. It is the best way to disconnect and recharge."
                }
            }
            lower.contains("yoga") || lower.contains("meditation") || lower.contains("wellness") -> {
                if (interests.contains("Yoga") || interests.contains("Wellness")) {
                    "Mindfulness is my daily ritual. Nothing beats a flowing sun salutation! Do you practice yoga, or do you prefer a quiet run in the park?"
                } else {
                    "Finding inner balance is so beautiful. I love starting my mornings with a silent cup of green tea!"
                }
            }
            lower.contains("music") || lower.contains("singer") || lower.contains("song") || lower.contains("concert") -> {
                if (interests.contains("Music") || interests.contains("Vinyl")) {
                    "Yes! Music connects us on a totally different wavelength. I'm currently listening to an indie folk vinyl. What band do you have on repeat right now? 🎶"
                } else {
                    "I love having acoustic guitar music playing in the background of my home. It creates the best cozy vibe."
                }
            }
            else -> {
                listOf(
                    "That's so fascinating! Tell me more about that. What are you most passionate about? ✨",
                    "Aha, interesting! By the way, how do you usually like to spend a perfect sunny Sunday morning? ☕",
                    "I completely agree! It's so rare to find someone who gets details like that. Let's do a coffee date and talk more? ☕"
                ).random()
            }
        }
    }
}
