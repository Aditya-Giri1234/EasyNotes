package com.kin.easynotes.markdown_test

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.AndroidJUnitRunner
import com.kin.easynotes.HiltComponentActivity
import com.kin.easynotes.core.constant.TestTagId
import com.kin.easynotes.data.repository.ImportExportRepository
import com.kin.easynotes.domain.usecase.ImportExportUseCase
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.domain.usecase.SettingsUseCase
import com.kin.easynotes.presentation.MainActivity
import com.kin.easynotes.presentation.components.GalleryObserver
import com.kin.easynotes.presentation.navigation.AppNavHost
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.theme.LeafNotesTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
class LineProcessingTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var settingsViewModel: SettingsViewModel

    @Inject
    lateinit var galleryObserver: GalleryObserver
    @Inject
    lateinit var backUp : ImportExportRepository
    @Inject
    lateinit var settingsUseCase: SettingsUseCase
    @Inject
    lateinit var noteUseCase: NoteUseCase
    @Inject
    lateinit var importExportUseCase: ImportExportUseCase


    @Before
    fun setUp() {
//        Dispatchers.setMain(testDispatcher) // Override Dispatchers.Main for testing coroutines
        hiltRule.inject() // Hilt injects dependencies
//        composeTestRule.mainClock.autoAdvance = true
        settingsViewModel = SettingsViewModel(
            galleryObserver,
            backUp,
            settingsUseCase,
            noteUseCase,
            importExportUseCase
        )
        composeTestRule.activity.setContent {
            val navController = rememberNavController()
            LeafNotesTheme(settingsViewModel){
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.semantics {
                        testTagsAsResourceId = true
                    }
                ) {
                    AppNavHost(
                        settingsModel = settingsViewModel,
                        navController,
                        -1,
                        NavRoutes.Home.route
                    )
                }
            }

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
//        Dispatchers.resetMain() // Reset the dispatcher after tests
    }

    @Test
    fun test_insert_image_syntax(){
        composeTestRule.onNodeWithTag(TestTagId.FLOATING_ACTION_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTagId.FLOATING_ACTION_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTagId.EDIT_NOTE_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTagId.EDIT_TEXT_MODE).performClick()
        composeTestRule.onNodeWithTag(TestTagId.EDIT_TEXT_SPACE).performClick()
        composeTestRule.onNodeWithTag(TestTagId.EDIT_TEXT_SPACE).performTextInput("!(https://plus.unsplash.com/premium_photo-1757260019141-458516170c6c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHwxfHx8ZW58MHx8fHx8)")
        composeTestRule.onNodeWithTag(TestTagId.PREVIEW_TEXT_MODE).performClick()
        val desiredNode = composeTestRule.onNodeWithContentDescription("https://plus.unsplash.com/premium_photo-1757260019141-458516170c6c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHwxfHx8ZW58MHx8fHx8".hashCode().toString())
//        composeTestRule.waitUntil(10000){
//            desiredNode.isDisplayed()
//        }
        desiredNode.assertIsDisplayed()
        composeTestRule.onNodeWithTag("https://plus.unsplash.com/premium_photo-1757260019141-458516170c6c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxmZWF0dXJlZC1waG90b3MtZmVlZHwxfHx8ZW58MHx8fHx8".hashCode().toString() + "Success").assertIsDisplayed()
    }

}