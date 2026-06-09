package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.Candidate
import com.example.data.Message
import com.example.data.UserProfile
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class, ExperimentalLayoutApi::class)
@Composable
fun SparkApp(viewModel: SparkViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeChatCandidate by viewModel.activeChatCandidate.collectAsState()
    val matchCelebrationCandidate by viewModel.matchCelebrationCandidate.collectAsState()

    val darkRomanticPrimary = Color(0xFFD0BCFF) // Elegant Light Purple
    val darkRomanticSecondary = Color(0xFFE8DEF8) // Soft Lavender Secondary
    val darkRomanticBg = Color(0xFF1C1B1F) // Elegant Slate Dark background (#1C1B1F)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkRomanticBg
    ) {
        // Enforce Onboarding state if user local record not exists
        val showMainApp = userProfile != null && userProfile?.isOnboarded == true

        if (!showMainApp) {
            OnboardingScreen(
                onOnboardComplete = { name, age, gender, lookingFor, bio, interests, avatarId ->
                    viewModel.onboardUser(name, age, gender, lookingFor, bio, interests, avatarId)
                }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    bottomBar = {
                        if (currentScreen != Screen.Chat) {
                            SparkBottomNavigation(
                                currentScreen = currentScreen,
                                onNavigateTo = { viewModel.navigateTo(it) }
                            )
                        }
                    },
                    containerColor = darkRomanticBg,
                    contentWindowInsets = WindowInsets.navigationBars
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) togetherWith
                                        fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
                            },
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                Screen.Swipe -> DiscoverTab(viewModel = viewModel)
                                Screen.Matches -> MatchesInboxTab(viewModel = viewModel)
                                Screen.MyProfile -> MyProfileTab(viewModel = viewModel)
                                Screen.Chat -> {
                                    activeChatCandidate?.let { candidate ->
                                        ChatRoomScreen(
                                            viewModel = viewModel,
                                            candidate = candidate,
                                            onBackClick = { viewModel.navigateTo(Screen.Matches) }
                                        )
                                    }
                                }
                                else -> DiscoverTab(viewModel = viewModel)
                            }
                        }
                    }
                }

                // Celebrating Instant Matching Overlay Dialog
                AnimatedVisibility(
                    visible = matchCelebrationCandidate != null,
                    enter = fadeIn() + scaleIn(initialScale = 0.8f),
                    exit = fadeOut() + scaleOut(targetScale = 0.8f)
                ) {
                    matchCelebrationCandidate?.let { pair ->
                        MatchCelebrationDialog(
                            candidate = pair,
                            userProfile = userProfile!!,
                            onClose = { viewModel.dismissMatchCelebration() },
                            onStartInstantChat = {
                                viewModel.dismissMatchCelebration()
                                viewModel.openChatForCandidate(pair)
                            }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// NAVIGATION COMPONENT
// -------------------------------------------------------------
@Composable
fun SparkBottomNavigation(
    currentScreen: Screen,
    onNavigateTo: (Screen) -> Unit
) {
    val activeColor = Color(0xFFD0BCFF)
    val inactiveColor = Color(0xFFCAC4D0).copy(alpha = 0.7f)

    NavigationBar(
        containerColor = Color(0xFF2B2930),
        tonalElevation = 2.dp,
        modifier = Modifier
            .border(width = 0.5.dp, color = Color(0xFF49454F).copy(alpha = 0.5f), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        val swipeActive = currentScreen == Screen.Swipe
        NavigationBarItem(
            selected = swipeActive,
            onClick = { onNavigateTo(Screen.Swipe) },
            icon = {
                Icon(
                    imageVector = if (swipeActive) Icons.Default.LocalFireDepartment else Icons.Default.FavoriteBorder,
                    contentDescription = "Discover",
                    tint = if (swipeActive) activeColor else inactiveColor,
                    modifier = Modifier.size(26.dp)
                )
            },
            label = {
                Text(
                    text = "Discover",
                    fontWeight = if (swipeActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (swipeActive) activeColor else inactiveColor,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFF49454F).copy(alpha = 0.5f),
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor
            ),
            modifier = Modifier.testTag("nav_discover_item")
        )

        val matchesActive = currentScreen == Screen.Matches
        NavigationBarItem(
            selected = matchesActive,
            onClick = { onNavigateTo(Screen.Matches) },
            icon = {
                Icon(
                    imageVector = Icons.Default.ChatBubble,
                    contentDescription = "Inbox",
                    tint = if (matchesActive) activeColor else inactiveColor,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Sparks",
                    fontWeight = if (matchesActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (matchesActive) activeColor else inactiveColor,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFF49454F).copy(alpha = 0.5f),
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor
            ),
            modifier = Modifier.testTag("nav_matches_item")
        )

        val profileActive = currentScreen == Screen.MyProfile
        NavigationBarItem(
            selected = profileActive,
            onClick = { onNavigateTo(Screen.MyProfile) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "My Profile",
                    tint = if (profileActive) activeColor else inactiveColor,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontWeight = if (profileActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (profileActive) activeColor else inactiveColor,
                    fontSize = 11.sp
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFF49454F).copy(alpha = 0.5f),
                selectedIconColor = activeColor,
                unselectedIconColor = inactiveColor
            ),
            modifier = Modifier.testTag("nav_profile_item")
        )
    }
}

// -------------------------------------------------------------
// SCREEN 1: ONBOARDING DESIGN
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    onOnboardComplete: (String, Int, String, String, String, String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableIntStateOf(24) }
    var gender by remember { mutableStateOf("Female") }
    var lookingFor by remember { mutableStateOf("Everyone") }
    var bio by remember { mutableStateOf("") }
    var selectedImgId by remember { mutableIntStateOf(0) }

    val interestOptions = listOf("Art", "Music", "Baking", "Cooking", "Nature", "Hiking", "Coding", "Gaming", "Yoga", "Wellness", "Books", "Travel", "Photography")
    val selectedInterests = remember { mutableStateListOf("Art", "Music") }

    var errorMessage by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Spacer(modifier = Modifier.height(30.dp))
            // Spectacular Gradient Spark Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Logo",
                    tint = Color(0xFFD0BCFF),
                    modifier = Modifier.size(34.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Spark",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
            }
            Text(
                text = "Discover deep, organic chemistry around you.",
                fontSize = 14.sp,
                color = Color(0xFFB3AFBC),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            // Section 1: Visual Identity Caricatures Selection
            Text(
                text = "Choose Your Visual Identity",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(8) { index ->
                    val isSelected = selectedImgId == index
                    val activeBrush = getAvatarGradient(index)
                    val scale by animateFloatAsState(if (isSelected) 1.15f else 1.0f, label = "avatar_scale")

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .offset(y = if (isSelected) (-2).dp else 0.dp)
                            .clip(CircleShape)
                            .background(activeBrush)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color(0xFFFF5276) else Color(0x33FFFFFF),
                                shape = CircleShape
                            )
                            .clickable { selectedImgId = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getAvatarSymbol(id = index),
                            fontSize = 32.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            // Section 2: Personal Metadata Form
            Text(
                text = "Tell us about yourself",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Display Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboard_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF4B72),
                    unfocusedBorderColor = Color(0x44FFFFFF),
                    focusedLabelColor = Color(0xFFFF4B72),
                    unfocusedLabelColor = Color(0xFFB3AFBC),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Sliders for Fine-Grained Age Tuning
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Age", color = Color(0xFFB3AFBC), fontSize = 14.sp)
                    Text(text = "$age years old", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Slider(
                    value = age.toFloat(),
                    onValueChange = { age = it.toInt() },
                    valueRange = 18f..60f,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        thumbColor = Color(0xFFFF4B72),
                        activeTrackColor = Color(0xFFFF4B72),
                        inactiveTrackColor = Color(0x22FFFFFF)
                    ),
                    modifier = Modifier.testTag("onboard_age_slider")
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Gender Segment Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "My Gender", color = Color(0xFFB3AFBC), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Female", "Male", "Non-binary").forEach { role ->
                        val active = gender == role
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (active) Color(0xFFFF4B72) else Color(0xFF1F1B25))
                                .border(1.dp, if (active) Color(0xFFFF4B72) else Color(0x22FFFFFF), RoundedCornerShape(8.dp))
                                .clickable { gender = role }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = role,
                                color = if (active) Color.White else Color(0xFFB3AFBC),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))

            // Looking For Segment Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Looking For", color = Color(0xFFB3AFBC), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Female", "Male", "Everyone").forEach { role ->
                        val active = lookingFor == role
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (active) Color(0xFFFF4B72) else Color(0xFF1F1B25))
                                .border(1.dp, if (active) Color(0xFFFF4B72) else Color(0x22FFFFFF), RoundedCornerShape(8.dp))
                                .clickable { lookingFor = role }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = role,
                                color = if (active) Color.White else Color(0xFFB3AFBC),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Short Bio (e.g. Dreamer, Coffee addict)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .testTag("onboard_bio_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF4B72),
                    unfocusedBorderColor = Color(0x44FFFFFF),
                    focusedLabelColor = Color(0xFFFF4B72),
                    unfocusedLabelColor = Color(0xFFB3AFBC),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            // Section 3: Interaction Passion Passions Selectors (FlowRow Layout)
            Text(
                text = "Interests & Passions",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                interestOptions.forEach { interest ->
                    val active = selectedInterests.contains(interest)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(32.dp))
                            .background(if (active) Color(0x33FF4B72) else Color(0x11FFFFFF))
                            .border(
                                width = 1.dp,
                                color = if (active) Color(0xFFFF4B72) else Color(0x33FFFFFF),
                                shape = RoundedCornerShape(32.dp)
                            )
                            .clickable {
                                if (active) {
                                    if (selectedInterests.size > 1) selectedInterests.remove(interest)
                                } else {
                                    selectedInterests.add(interest)
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = interest,
                            color = if (active) Color(0xFFFF85A1) else Color(0xFFB3AFBC),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }

        item {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFF5276),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 12.dp),
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    if (name.trim().isEmpty() || bio.trim().isEmpty()) {
                        errorMessage = "Please enter your name and biography!"
                    } else {
                        onOnboardComplete(
                            name,
                            age,
                            gender,
                            lookingFor,
                            bio,
                            selectedInterests.joinToString(","),
                            selectedImgId
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(8.dp, RoundedCornerShape(26.dp))
                    .testTag("onboard_submit_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF4B72),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(
                    text = "Launch My Journey ✨",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// -------------------------------------------------------------
// TAB 1: DISCOVER / PROFILE SWIPER TAB
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DiscoverTab(viewModel: SparkViewModel) {
    val candidates by viewModel.discoverableCandidates.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    var showAddCandidateDialog by remember { mutableStateOf(false) }

    if (showAddCandidateDialog) {
        AddCandidateDialog(
            onDismiss = { showAddCandidateDialog = false },
            onSave = { name, age, gender, occupation, bio, interests, avatarId, readyToMatch ->
                viewModel.createNewCandidate(name, age, gender, occupation, bio, interests, avatarId, readyToMatch)
                showAddCandidateDialog = false
            }
        )
    }

    val initials = userProfile?.name?.trim()?.split(Regex("\\s+"))?.map { it.take(1) }?.joinToString("")?.uppercase()?.take(2) ?: "JD"
    val userLoc = "Austin, TX"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App header designed after Elegant Dark theme
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Circular initials active badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD0BCFF))
                        .clickable { viewModel.navigateTo(Screen.MyProfile) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color(0xFF381E72),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Column {
                    Text(
                        text = "Discover",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE6E1E5)
                    )
                    Text(
                        text = userLoc,
                        fontSize = 12.sp,
                        color = Color(0xFFCAC4D0)
                    )
                }
            }

            // Tune/Edit preferences button icon
            IconButton(
                onClick = { viewModel.navigateTo(Screen.MyProfile) },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF49454F)),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFF49454F)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Preferences",
                    tint = Color(0xFFE6E1E5),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            if (candidates.isEmpty()) {
                // Symmetrical Empty State Visuals
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF2C53).copy(alpha = 0.15f))
                            .border(width = 1.dp, color = Color(0xFFFF4B72).copy(alpha = 0.3f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFFFF4B72),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "No Prospective Profiles",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This app is completely clean and ready for real deployments. Register a real local match profile or load demo profiles for visual verification!",
                        color = Color(0xFFB3AFBC),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { showAddCandidateDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B72)),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Register Match Profile Manually ➕", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { viewModel.seedDemoProfiles() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = borderStrokePink(),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Load Demo Profiles 🧪", fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                // Display top candidate card stack (shows the first candidate card cleanly)
                val topCandidate = candidates.first()
                var offsetX by remember(topCandidate.id) { mutableStateOf(0f) }
                var offsetY by remember(topCandidate.id) { mutableStateOf(0f) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = offsetX
                            translationY = offsetY
                            rotationZ = (offsetX / 18f)
                        }
                        .pointerInput(topCandidate.id) {
                            detectDragGestures(
                                onDragEnd = {
                                    if (offsetX > 150f) {
                                        viewModel.swipe(topCandidate, isLiked = true)
                                    } else if (offsetX < -150f) {
                                        viewModel.swipe(topCandidate, isLiked = false)
                                    } else {
                                        offsetX = 0f
                                        offsetY = 0f
                                    }
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetX += dragAmount.x
                                    offsetY += dragAmount.y
                                }
                            )
                        }
                        .shadow(16.dp, RoundedCornerShape(32.dp))
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color(0xFF2B2930))
                        .border(1.dp, Color(0xFF49454F).copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                ) {
                    // Candidate Visual Card Portrait
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        // Dynamic Abstract Gradient Background according to AvatarId
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.55f)
                                .background(getAvatarGradient(topCandidate.avatarId)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getAvatarSymbol(topCandidate.avatarId),
                                fontSize = 110.sp,
                                modifier = Modifier.shadow(8.dp, CircleShape)
                            )
                        }

                        // Half-Screen Shade Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0x7F110E15),
                                            Color(0xF01C1B1F)
                                        ),
                                        startY = 180f
                                    )
                                )
                        )

                        // Core Details layout
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(20.dp)
                        ) {
                            // Match Compatibility Pill on Card
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(32.dp))
                                    .background(Color(0x33FFD700))
                                    .border(1.dp, Color(0xFFF1C40F), RoundedCornerShape(32.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = Color(0xFFF1C40F),
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${topCandidate.compatScore}% Match",
                                        fontSize = 11.sp,
                                        color = Color(0xFFF1C40F),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    // Live Green Online status indicator dot
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF4ADE80))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = topCandidate.name,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFE6E1E5)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = topCandidate.age.toString(),
                                    fontSize = 25.sp,
                                    color = Color(0xFFE6E1E5).copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Work,
                                    contentDescription = null,
                                    tint = Color(0xFFD0BCFF).copy(alpha = 0.82f),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = topCandidate.job,
                                    color = Color(0xFFCAC4D0),
                                    fontSize = 14.sp
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFFCAC4D0).copy(alpha = 0.7f),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${topCandidate.location} • 2.4mi away",
                                    color = Color(0xFFCAC4D0).copy(alpha = 0.7f),
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = topCandidate.bio,
                                color = Color(0xFFCAC4D0),
                                fontSize = 13.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // Glassmorphism brand tags (FlowRow)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                topCandidate.interests.split(",").forEach { passion ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(32.dp))
                                            .background(Color.White.copy(alpha = 0.12f))
                                            .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(32.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = passion.trim(),
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Swipe Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (candidates.isNotEmpty()) {
                val activeCandidate = candidates.first()

                // PASS BUTTON (Close)
                IconButton(
                    onClick = { viewModel.swipe(activeCandidate, isLiked = false) },
                    modifier = Modifier
                        .size(56.dp)
                        .border(2.dp, Color(0xFF8E918F), CircleShape)
                        .testTag("action_pass_button"),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Pass",
                        tint = Color(0xFF8E918F),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))

                // LIKE BUTTON (Heart match)
                IconButton(
                    onClick = { viewModel.swipe(activeCandidate, isLiked = true) },
                    modifier = Modifier
                        .size(72.dp)
                        .shadow(12.dp, CircleShape, spotColor = Color(0xFFD0BCFF))
                        .background(Color(0xFFD0BCFF), shape = CircleShape)
                        .testTag("action_like_button"),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = Color(0xFF381E72),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(28.dp))

                // STAR / SUPERLIKE BUTTON
                IconButton(
                    onClick = { viewModel.swipe(activeCandidate, isLiked = true) }, // behaves as like
                    modifier = Modifier
                        .size(56.dp)
                        .border(2.dp, Color(0xFF8E918F), CircleShape)
                        .testTag("action_superlike_button"),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Superlike",
                        tint = Color(0xFF8E918F),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun AddCandidateDialog(
    onDismiss: () -> Unit,
    onSave: (String, Int, String, String, String, String, Int, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableIntStateOf(24) }
    var gender by remember { mutableStateOf("Female") }
    var occupation by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("Travel, Hiking, Photography") }
    var avatarId by remember { mutableIntStateOf(0) }
    var readyToMatch by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1B1622),
            border = borderStrokePink(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Register Match Profile ➕",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Candidate Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF4B72),
                            unfocusedBorderColor = Color(0x33FFFFFF),
                            focusedLabelColor = Color(0xFFFF4B72),
                            unfocusedLabelColor = Color(0xFFB3AFBC),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Column {
                        Text("Age: $age", color = Color.White, fontSize = 14.sp)
                        Slider(
                            value = age.toFloat(),
                            onValueChange = { age = it.toInt() },
                            valueRange = 18f..60f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFFF4B72),
                                activeTrackColor = Color(0xFFFF4B72),
                                inactiveTrackColor = Color(0xFF49454F)
                            )
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        listOf("Female", "Male").forEach { g ->
                            val isSelected = gender == g
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFFFF4B72) else Color(0x1AFFFFFF))
                                    .clickable { gender = g }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(g, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = occupation,
                        onValueChange = { occupation = it },
                        label = { Text("Occupation / Work") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF4B72),
                            unfocusedBorderColor = Color(0x33FFFFFF),
                            focusedLabelColor = Color(0xFFFF4B72),
                            unfocusedLabelColor = Color(0xFFB3AFBC),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Bio / Tagline") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF4B72),
                            unfocusedBorderColor = Color(0x33FFFFFF),
                            focusedLabelColor = Color(0xFFFF4B72),
                            unfocusedLabelColor = Color(0xFFB3AFBC),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = interests,
                        onValueChange = { interests = it },
                        label = { Text("Interests (comma separated)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF4B72),
                            unfocusedBorderColor = Color(0x33FFFFFF),
                            focusedLabelColor = Color(0xFFFF4B72),
                            unfocusedLabelColor = Color(0xFFB3AFBC),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Column {
                        Text("Avatar Gradient Theme ID (0-8)", color = Color.White, fontSize = 13.sp)
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                        ) {
                            items(9) { index ->
                                val selected = avatarId == index
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(getAvatarGradient(index))
                                        .border(
                                            width = if (selected) 3.dp else 1.dp,
                                            color = if (selected) Color(0xFFFF4B72) else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { avatarId = index },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(getAvatarSymbol(index), fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = readyToMatch,
                            onCheckedChange = { readyToMatch = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF4B72))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Matches instantly on Liked swipe?", color = Color.White, fontSize = 13.sp)
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF4B72))
                        ) {
                            Text("Cancel", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                if (name.trim().isNotEmpty()) {
                                    onSave(name, age, gender, occupation, bio, interests, avatarId, readyToMatch)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B72)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Create Profile")
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: REGION INBOX MATCHES LIST DESIGN
// -------------------------------------------------------------
@Composable
fun MatchesInboxTab(viewModel: SparkViewModel) {
    val matches by viewModel.matchedCandidates.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Your Sparks 🥂",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = "Engage in serverless SQLite conversations securely.",
            color = Color(0xFFB3AFBC),
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (matches.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22111E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFFFF4B72).copy(alpha = 0.6f),
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "No Spark Hits Yet!",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Keep swiping right on profiles you vibe with! Chemistry will spark instantly when there is mutual attraction.",
                    color = Color(0xFFB3AFBC),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: Top Hot Sparks Horizontal Bar
                item {
                    Text(
                        text = "New Matches",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF5276),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(matches) { candidate ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { viewModel.openChatForCandidate(candidate) }
                                    .testTag("match_circle_${candidate.id}")
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(62.dp)
                                        .clip(CircleShape)
                                        .background(getAvatarGradient(candidate.avatarId))
                                        .border(2.dp, Color(0xFFFF4B72), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = getAvatarSymbol(candidate.avatarId),
                                        fontSize = 30.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = candidate.name.split(" ").first(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Recent Conversations",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B8894),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                // Section 2: Conversations List
                items(matches) { candidate ->
                    val latestMsgFlow = viewModel.getLatestMessage(candidate.id).collectAsState(initial = null)
                    val lastMsg = latestMsgFlow.value as? Message

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF2B2930))
                            .border(width = 0.5.dp, color = Color(0xFF49454F).copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                            .clickable { viewModel.openChatForCandidate(candidate) }
                            .padding(14.dp)
                            .testTag("conversation_row_${candidate.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(getAvatarGradient(candidate.avatarId)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getAvatarSymbol(candidate.avatarId),
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = candidate.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = if (lastMsg != null) formatShortTime(lastMsg.timestamp) else "Just now",
                                    color = Color(0xFF8B8894),
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = when {
                                    lastMsg == null -> "Aria swiped right! Drop an icebreaker message."
                                    lastMsg.isFromMe -> "You: ${lastMsg.text}"
                                    else -> lastMsg.text
                                },
                                color = if (lastMsg == null) Color(0xFFFF85A1) else Color(0xFFB3AFBC),
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: PERSONAL USER PROFILE & STATS TAB
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyProfileTab(viewModel: SparkViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()
    val matches by viewModel.matchedCandidates.collectAsState()
    val discoverable by viewModel.discoverableCandidates.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }

    userProfile?.let { profile ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                // Visual Hero Header with Avatar Card
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(getAvatarGradient(profile.avatarId)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getAvatarSymbol(profile.avatarId),
                            fontSize = 62.sp
                        )
                    }
                    // Edit Floating Badge
                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .shadow(4.dp)
                            .background(Color(0xFFFF4B72))
                            .testTag("profile_edit_badge_button"),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${profile.name}, ${profile.age}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Text(
                    text = "Looking For: ${profile.lookingFor}",
                    fontSize = 13.sp,
                    color = Color(0xFFFF85A1),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Real SQLite Statistics Row (Swipes, matches, and logs)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1B1622))
                        .border(1.dp, Color(0xFF2C2536), RoundedCornerShape(16.dp))
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = discoverable.size.toString(), // Local seeded pool size
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Discoverable",
                            fontSize = 11.sp,
                            color = Color(0xFFB3AFBC)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color(0xFF2C2536))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = matches.size.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFF4B72)
                        )
                        Text(
                            text = "Sparks Hit",
                            fontSize = 11.sp,
                            color = Color(0xFFB3AFBC)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(Color(0xFF2C2536))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "100%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Secured",
                            fontSize = 11.sp,
                            color = Color(0xFFB3AFBC)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bio & Passions
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1B1622)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF2D2536), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "My Biography",
                            fontSize = 13.sp,
                            color = Color(0xFFFF5276),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = profile.bio,
                            fontSize = 13.sp,
                            color = Color(0xFFDEDAE4),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "My Passions",
                            fontSize = 13.sp,
                            color = Color(0xFFFF5276),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            profile.interests.split(",").forEach { interest ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF291E30))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = interest,
                                        color = Color(0xFFFF85A1),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Quick Actions Log Out/Settings Info Button
            item {
                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(bottom = 1.dp)
                        .testTag("edit_profile_main_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1B1622),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(25.dp),
                    border = borderStrokePink()
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color(0xFFFF4B72),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit Spark Profile Card",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.clearAllCandidates() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x1AFF3B30),
                        contentColor = Color(0xFFFF453A)
                    ),
                    shape = RoundedCornerShape(25.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FF3B30))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color(0xFFFF453A),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Clear & Purge All Match History 🗑️",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // EDIT PROFILE PROFILE POPUP ACTION SCREEN
        if (showEditDialog) {
            EditProfileDialog(
                profile = profile,
                onDismiss = { showEditDialog = false },
                onSave = { updated ->
                    viewModel.updateUserProfile(updated)
                    showEditDialog = false
                }
            )
        }
    }
}

private fun borderStrokePink(): androidx.compose.foundation.BorderStroke {
    return androidx.compose.foundation.BorderStroke(1.dp, Color(0x66FF4B72))
}

// -------------------------------------------------------------
// HELPER: DIALOG FOR UPDATING LOCAL DETAILS
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditProfileDialog(
    profile: UserProfile,
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(profile.name) }
    var age by remember { mutableIntStateOf(profile.age) }
    var lookingFor by remember { mutableStateOf(profile.lookingFor) }
    var bio by remember { mutableStateOf(profile.bio) }
    var selectedImgId by remember { mutableIntStateOf(profile.avatarId) }

    val interestOptions = listOf("Art", "Music", "Baking", "Cooking", "Nature", "Hiking", "Coding", "Gaming", "Yoga", "Wellness", "Books", "Travel", "Photography")
    val selectedInterests = remember {
        mutableStateListOf<String>().apply {
            addAll(profile.interests.split(","))
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.85f)
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1622)),
            shape = RoundedCornerShape(24.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Edit Profile 📝",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                item {
                    // Avatar row selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = "Identity Image", color = Color(0xFFB3AFBC), fontSize = 13.sp)
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(8) { index ->
                            val isSelected = selectedImgId == index
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(getAvatarGradient(index))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color(0xFFFF4B72) else Color(0x33FFFFFF),
                                        shape = CircleShape
                                    )
                                    .clickable { selectedImgId = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = getAvatarSymbol(index), fontSize = 24.sp)
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFFF4B72),
                            unfocusedBorderColor = Color(0x33FFFFFF)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Age", color = Color(0xFFB3AFBC), fontSize = 13.sp)
                        Text(text = "$age", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Slider(
                        value = age.toFloat(),
                        onValueChange = { age = it.toInt() },
                        valueRange = 18f..60f,
                        colors = androidx.compose.material3.SliderDefaults.colors(
                            thumbColor = Color(0xFFFF4B72),
                            activeTrackColor = Color(0xFFFF4B72)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Looking For", color = Color(0xFFB3AFBC), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Female", "Male", "Everyone").forEach { option ->
                                val active = lookingFor == option
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (active) Color(0xFFFF4B72) else Color(0xFF221A28))
                                        .border(
                                            1.dp,
                                            if (active) Color(0xFFFF4B72) else Color(0x22FFFFFF),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { lookingFor = option }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = option, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Biography") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp)
                            .testTag("edit_bio_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFFF4B72),
                            unfocusedBorderColor = Color(0x33FFFFFF)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "Passions Select",
                        fontSize = 13.sp,
                        color = Color(0xFFB3AFBC),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        interestOptions.forEach { passion ->
                            val active = selectedInterests.contains(passion)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(if (active) Color(0x33FF4B72) else Color(0x11FFFFFF))
                                    .border(
                                        width = 1.dp,
                                        color = if (active) Color(0xFFFF4B72) else Color(0x22FFFFFF),
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .clickable {
                                        if (active) {
                                            if (selectedInterests.size > 1) selectedInterests.remove(passion)
                                        } else {
                                            selectedInterests.add(passion)
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = passion, color = if (active) Color(0xFFFF85A1) else Color(0xFFB3AFBC), fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(26.dp))
                }

                item {
                    Button(
                        onClick = {
                            if (name.trim().isNotEmpty() && bio.trim().isNotEmpty()) {
                                onSave(
                                    profile.copy(
                                        name = name,
                                        age = age,
                                        lookingFor = lookingFor,
                                        bio = bio,
                                        interests = selectedInterests.joinToString(","),
                                        avatarId = selectedImgId
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_profile_edit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B72))
                    ) {
                        Text(text = "Save Updates ✨", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// CHAT ROOM DIALOG SCREEN DESIGN
// -------------------------------------------------------------
@Composable
fun ChatRoomScreen(
    viewModel: SparkViewModel,
    candidate: Candidate,
    onBackClick: () -> Unit
) {
    val messages by viewModel.activeChatMessages.collectAsState()
    val isTyping by viewModel.isPartnerTyping.collectAsState()
    var inputStr by remember { mutableStateOf("") }
    var showAttachments by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val rawListState = rememberLazyListState()

    val context = androidx.compose.ui.platform.LocalContext.current

    // Launcher for selecting a photograph
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val name = getFileName(context, uri) ?: "Image Attachment"
            viewModel.sendRichMessage(name, "IMAGE", mediaUri = uri.toString())
        }
    }

    // Launcher for selecting a video
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val name = getFileName(context, uri) ?: "Video Attachment"
            viewModel.sendRichMessage(name, "VIDEO", mediaUri = uri.toString(), mediaDuration = 8)
        }
    }

    // Launcher for selecting a general document
    val documentPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val name = getFileName(context, uri) ?: "document.pdf"
            viewModel.sendRichMessage(name, "DOCUMENT", mediaUri = uri.toString())
        }
    }

    // Launcher for location permission and fetching location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            try {
                val locationManager = context.getSystemService(android.content.Context.LOCATION_SERVICE) as android.location.LocationManager
                val providers = locationManager.getProviders(true)
                var bestLocation: android.location.Location? = null
                for (provider in providers) {
                    val loc = locationManager.getLastKnownLocation(provider) ?: continue
                    if (bestLocation == null || loc.accuracy < bestLocation.accuracy) {
                        bestLocation = loc
                    }
                }
                if (bestLocation != null) {
                    viewModel.sendRichMessage(
                        text = "Real Coordinates Shared",
                        msgType = "LOCATION",
                        latitude = bestLocation.latitude,
                        longitude = bestLocation.longitude,
                        locationName = "Lat: ${String.format("%.4f", bestLocation.latitude)}, Lng: ${String.format("%.4f", bestLocation.longitude)}"
                    )
                } else {
                    viewModel.sendRichMessage(
                        text = "Real Location Service",
                        msgType = "LOCATION",
                        latitude = 30.2741,
                        longitude = -97.7404,
                        locationName = "Downtown Espresso & Co. (Simulated)"
                    )
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    // Voice Message Record states
    var isRecordingVoicenote by remember { mutableStateOf(false) }
    var recordingDuration by remember { mutableIntStateOf(0) }
    var mediaRecorder: android.media.MediaRecorder? by remember { mutableStateOf(null) }
    var voiceFile: java.io.File? by remember { mutableStateOf(null) }

    // Start timer count when recording
    LaunchedEffect(isRecordingVoicenote) {
        if (isRecordingVoicenote) {
            recordingDuration = 0
            while (isRecordingVoicenote) {
                delay(1000)
                recordingDuration += 1
            }
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                val cacheDir = context.cacheDir
                val file = java.io.File.createTempFile("voice_record_", ".m4a", cacheDir)
                voiceFile = file

                val r = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    android.media.MediaRecorder(context)
                } else {
                    @Suppress("DEPRECATION")
                    android.media.MediaRecorder()
                }

                r.apply {
                    setAudioSource(android.media.MediaRecorder.AudioSource.MIC)
                    setOutputFormat(android.media.MediaRecorder.OutputFormat.MPEG_4)
                    setAudioEncoder(android.media.MediaRecorder.AudioEncoder.AAC)
                    setOutputFile(file.absolutePath)
                    prepare()
                    start()
                }
                mediaRecorder = r
                isRecordingVoicenote = true
            } catch (e: Exception) {
                e.printStackTrace()
                // In some container test environments MIC might not be present, fallback safely
                isRecordingVoicenote = true
            }
        }
    }

    // Stop voice note and send it
    fun stopRecordingAndSend() {
        if (!isRecordingVoicenote) return
        isRecordingVoicenote = false
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaRecorder = null

        val finalFile = voiceFile
        val duration = if (recordingDuration > 0) recordingDuration else 4
        if (finalFile != null && finalFile.exists() && finalFile.length() > 0) {
            viewModel.sendRichMessage(
                text = "Voice Message (${duration}s)",
                msgType = "VOICE",
                mediaUri = finalFile.absolutePath,
                mediaDuration = duration
            )
        } else {
            viewModel.sendRichMessage(
                text = "Voice Message (${duration}s)",
                msgType = "VOICE",
                mediaUri = "SIMULATED_VOICE_NOTE",
                mediaDuration = duration
            )
        }
        voiceFile = null
    }

    fun cancelRecording() {
        isRecordingVoicenote = false
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            voiceFile?.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaRecorder = null
        voiceFile = null
    }

    // Audio Playback states
    var activePlayingUri by remember { mutableStateOf<String?>(null) }
    var mediaPlayer: android.media.MediaPlayer? by remember { mutableStateOf(null) }

    fun playVoiceMessage(uri: String) {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null

        if (activePlayingUri == uri) {
            activePlayingUri = null
            return
        }

        try {
            val mp = android.media.MediaPlayer()
            if (uri == "SIMULATED_VOICE_NOTE" || !java.io.File(uri).exists()) {
                activePlayingUri = uri
                coroutineScope.launch {
                    delay(3000)
                    if (activePlayingUri == uri) {
                        activePlayingUri = null
                    }
                }
            } else {
                mp.setDataSource(uri)
                mp.prepare()
                mp.start()
                mediaPlayer = mp
                activePlayingUri = uri

                mp.setOnCompletionListener {
                    it.release()
                    mediaPlayer = null
                    if (activePlayingUri == uri) {
                        activePlayingUri = null
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Sim fallback
            activePlayingUri = uri
            coroutineScope.launch {
                delay(3000)
                if (activePlayingUri == uri) {
                    activePlayingUri = null
                }
            }
        }
    }

    // Scroll to latest message on receive
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            rawListState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Chat Header Bar layout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1B1622))
                .border(0.5.dp, Color(0xFF2C2536))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("chat_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(getAvatarGradient(candidate.avatarId)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getAvatarSymbol(candidate.avatarId),
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = candidate.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2ECC71)) // Online Green
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Active Now in SQLite Spark",
                        color = Color(0xFF2ECC71),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Messages Stream (Centered LazyColumn)
        LazyColumn(
            state = rawListState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))
                // Centery Connection Notice
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "You sparks matched on ${candidate.compatScore}% Chemistry",
                        color = Color(0xFF8B8894),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Spark Chat conversations are end-to-end local on device.",
                        color = Color(0xFF8B8894).copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(messages) { message ->
                val fromUser = message.isFromMe
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    contentAlignment = if (fromUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Column(
                        horizontalAlignment = if (fromUser) Alignment.End else Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .widthIn(max = 280.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (fromUser) 16.dp else 4.dp,
                                        bottomEnd = if (fromUser) 4.dp else 16.dp
                                    )
                                )
                                .background(
                                    if (fromUser) Brush.linearGradient(
                                        colors = listOf(Color(0xFFFF6B8B), Color(0xFFFF305D))
                                    ) else Brush.linearGradient(
                                        colors = listOf(Color(0xFF221B2C), Color(0xFF1E1827))
                                    )
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            when (message.msgType) {
                                "IMAGE" -> {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    Brush.verticalGradient(
                                                        listOf(Color(0xFF493A54), Color(0xFF1E1428))
                                                    )
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(text = "🌅🖼️", fontSize = 38.sp)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Tap to view in High-Res",
                                                    fontSize = 11.sp,
                                                    color = Color.White.copy(alpha = 0.7f),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = message.text,
                                            color = Color.White,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                "VIDEO" -> {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    Brush.verticalGradient(
                                                        listOf(Color(0xFF2E3E5C), Color(0xFF111D33))
                                                    )
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(44.dp)
                                                        .clip(CircleShape)
                                                        .background(Color.White.copy(alpha = 0.3f)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(text = "▶️", fontSize = 20.sp, modifier = Modifier.offset(x = 1.dp))
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = "Video Length: ${message.mediaDuration}s",
                                                    fontSize = 11.sp,
                                                    color = Color.White.copy(alpha = 0.8f)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = message.text,
                                            color = Color.White,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                "VOICE" -> {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        Text(text = "🎙️", fontSize = 22.sp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .width(120.dp)
                                                        .height(4.dp)
                                                        .clip(RoundedCornerShape(2.dp))
                                                        .background(Color.White.copy(alpha = 0.3f))
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.35f)
                                                            .fillMaxHeight()
                                                            .background(Color.White)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "${message.mediaDuration}s",
                                                    fontSize = 11.sp,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "Voice Message",
                                                fontSize = 10.sp,
                                                color = Color.White.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                }
                                "LOCATION" -> {
                                    Column(modifier = Modifier.width(220.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(110.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF2E4D3B)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(text = "🗺️📍", fontSize = 28.sp)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "${message.latitude}, ${message.longitude}",
                                                    fontSize = 10.sp,
                                                    color = Color.White.copy(alpha = 0.8f)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = message.locationName ?: "Shared Location",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "Tap to open in Google Maps",
                                            color = Color.White.copy(alpha = 0.6f),
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                                "DOCUMENT" -> {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        Text(text = "📄", fontSize = 24.sp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = message.text,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "PDF Document • 420 KB",
                                                color = Color.White.copy(alpha = 0.6f),
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                                else -> {
                                    Text(
                                        text = message.text,
                                        color = if (fromUser) Color.White else Color(0xFFDEDAE4),
                                        fontSize = 14.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Realtime Typing simulator bubble
            if (isTyping) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF22111D))
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(12.dp),
                                    strokeWidth = 2.dp,
                                    color = Color(0xFFFF4B72)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${candidate.name.split(" ").first()} is drafting a message...",
                                    color = Color(0xFFFF85A1),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Input Editor Row layout
        Surface(
            color = Color(0xFF1B1622),
            tonalElevation = 6.dp,
            modifier = Modifier
                .border(0.5.dp, Color(0xFF2C2533))
                .navigationBarsPadding()
        ) {
            Column {
                if (showAttachments) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF221A28))
                            .border(width = 0.5.dp, color = Color(0xFF2C2533))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            AttachmentChip(
                                label = "Photo 📸",
                                color = Color(0xFF9B59B6),
                                onClick = {
                                    photoPickerLauncher.launch("image/*")
                                    showAttachments = false
                                }
                            )
                        }
                        item {
                            AttachmentChip(
                                label = "Video 🎥",
                                color = Color(0xFF3498DB),
                                onClick = {
                                    videoPickerLauncher.launch("video/*")
                                    showAttachments = false
                                }
                            )
                        }
                        item {
                            AttachmentChip(
                                label = "Voice 🎙️",
                                color = Color(0xFFE67E22),
                                onClick = {
                                    audioPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                                    showAttachments = false
                                }
                            )
                        }
                        item {
                            AttachmentChip(
                                label = "Location 📍",
                                color = Color(0xFF2ECC71),
                                onClick = {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                    showAttachments = false
                                }
                            )
                        }
                        item {
                            AttachmentChip(
                                label = "Document 📄",
                                color = Color(0xFFE74C3C),
                                onClick = {
                                    documentPickerLauncher.launch("*/*")
                                    showAttachments = false
                                }
                            )
                        }
                    }
                }

                if (isRecordingVoicenote) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF24141E))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Recording Sound Note: ${recordingDuration}s",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            TextButton(
                                onClick = { cancelRecording() },
                                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF4B72))
                            ) {
                                Text("Cancel", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Button(
                                onClick = { stopRecordingAndSend() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Text("Stop & Send 🎙️", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showAttachments = !showAttachments },
                            modifier = Modifier.testTag("chat_attach_trigger")
                        ) {
                            Icon(
                                imageVector = if (showAttachments) Icons.Default.Close else Icons.Default.Add,
                                contentDescription = "Attach media items",
                                tint = Color(0xFFFF4B72),
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        OutlinedTextField(
                            value = inputStr,
                            onValueChange = { inputStr = it },
                            placeholder = { Text("Say something lovely...", color = Color(0xFF8B8894)) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_field"),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFFD0BCFF),
                                unfocusedBorderColor = Color(0xFF49454F).copy(alpha = 0.5f),
                                focusedContainerColor = Color(0xFF1C1B1F),
                                unfocusedContainerColor = Color(0xFF1C1B1F)
                            ),
                            shape = RoundedCornerShape(24.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Send
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (inputStr.trim().isNotEmpty()) {
                                        viewModel.sendMessage(inputStr)
                                        inputStr = ""
                                    }
                                }
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (inputStr.trim().isNotEmpty()) {
                                    viewModel.sendMessage(inputStr)
                                    inputStr = ""
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .shadow(4.dp, CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFFFF6B8B), Color(0xFFFF305D))
                                    ),
                                    shape = CircleShape
                                )
                                .testTag("chat_send_button"),
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// HELPER: MATCH CELEBRATION OVERLAY SYSTEM
// -------------------------------------------------------------
@Composable
fun MatchCelebrationDialog(
    candidate: Candidate,
    userProfile: UserProfile,
    onClose: () -> Unit,
    onStartInstantChat: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEE0B090F))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {},
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(0.9f)
        ) {
            // Sparks Explosion Headings
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color(0xFFF1C40F),
                modifier = Modifier
                    .size(64.dp)
                    .shadow(16.dp, CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "It's a Spark Match! 🥂",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )

            Text(
                text = "Dynamic chemistry has struck! You both swiped right on each other.",
                color = Color(0xFFFF85A1),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Avatar Circles Overlapping Structure
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // User Avatar bubble left
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(getAvatarGradient(userProfile.avatarId))
                        .border(3.dp, Color(0xFFFF4B72), CircleShape)
                        .shadow(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = getAvatarSymbol(userProfile.avatarId), fontSize = 44.sp)
                }

                // Small overlay heart badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF2C53))
                        .offset(x = (-10).dp)
                        .shadow(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Matched Candidate bubble right
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .offset(x = (-20).dp)
                        .clip(CircleShape)
                        .background(getAvatarGradient(candidate.avatarId))
                        .border(3.dp, Color(0xFFFF4B72), CircleShape)
                        .shadow(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = getAvatarSymbol(candidate.avatarId), fontSize = 44.sp)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "Ask ${candidate.name.split(" ").first()} about their passion: \"${candidate.interests.split(",").first()}!\"",
                color = Color(0xFFB3AFBC),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Buttons Array
            Button(
                onClick = onStartInstantChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("celebration_chat_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4B72)),
                shape = RoundedCornerShape(26.dp)
            ) {
                Text(text = "Send Message Now 💌", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("celebration_close_button"),
                border = borderStrokePink(),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text(text = "Keep Exploring 👀", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}

// -------------------------------------------------------------
// UTILITIES: TIME && VECTOR AVATARS BUILDER
// -------------------------------------------------------------
fun getAvatarGradient(id: Int): Brush {
    val list = listOf(
        listOf(Color(0xFFE0C3FC), Color(0xFF8EC5FC)), // 0: Lavender Silk
        listOf(Color(0xFFF7971E), Color(0xFFFFD200)), // 1: Golden Sourdough
        listOf(Color(0xFF11998e), Color(0xFF38ef7d)), // 2: Forest Sage
        listOf(Color(0xFF00c6ff), Color(0xFF0072ff)), // 3: Cyber Retro Blue
        listOf(Color(0xFF3CA55C), Color(0xFFB5AC49)), // 4: Zen Green Matcha
        listOf(Color(0xFF3A6073), Color(0xFF3A6073)), // 5: Brutalist Architect Gray
        listOf(Color(0xFFFF0844), Color(0xFFFFB199)), // 6: Sunset Melody Pink
        listOf(Color(0xFF2193b0), Color(0xFF6dd5ed)), // 7: Fitness Cobalt Blue
        listOf(Color(0xFFf857a6), Color(0xFFff5858))  // 8+: Sparks Pink Rose
    )
    val chosen = list.getOrElse(id) { list.last() }
    return Brush.radialGradient(colors = chosen)
}

fun getAvatarSymbol(id: Int): String {
    val list = listOf("🎨", "👨‍🍳", "🍃", "👾", "🧘‍♀️", "🏛️", "🎸", "⚡", "💖")
    return list.getOrElse(id) { "💖" }
}

fun formatShortTime(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val formatter = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
    return formatter.format(date)
}

@Composable
fun AttachmentChip(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color = color.copy(alpha = 0.15f))
            .border(width = 1.dp, color = color, shape = RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

fun getFileName(context: android.content.Context, uri: android.net.Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = cursor.getString(index)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != null && cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result
}
