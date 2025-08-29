package com.kin.easynotes.resources

import android.os.Looper
import android.os.Looper.getMainLooper
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
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
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
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
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject

/**
 *
 * [See More about this basic testing on ](https://developer.android.com/training/dependency-injection/hilt-testing)
 * */
@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
//https://github.com/robolectric/robolectric/issues/5356 For this issue we need below annotation
@LooperMode(LooperMode.Mode.PAUSED)
//https://medium.com/@drflakelorenzgerman/tdd-part-iii-hilt-and-robolectric-android-dc941e3538f4 For Below
@Config(
    application = HiltTestApplication::class,
    instrumentedPackages = [
        // required to access final members on androidx.loader.content.ModernAsyncTask
        "androidx.loader.content"
    ])

class LineProcessingTest2{

    @get:Rule(order = 0)
    val hiltRule  = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

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


    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    @Throws(Exception::class)
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Override Dispatchers.Main for testing coroutines
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
        settingsViewModel = SettingsViewModel(
            galleryObserver,
            backUp,
            settingsUseCase,
            noteUseCase,
            importExportUseCase
        )
        //https://stackoverflow.com/a/79210545/17464278
        //why i used activity here , follow above issue
        // Here we simply override MainActivity default set content function
        composeTestRule.activity.setContent {
            AppNavHost(
                settingsModel = settingsViewModel,
                rememberNavController(),
                -1,
                NavRoutes.Home.route
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain() // Reset the dispatcher after tests
    }

    @Test
    fun `test code block flow in edit screen`(){
        //https://robolectric.org/blog/2019/06/04/paused-looper/
        // We need below idle because see above
        shadowOf(getMainLooper()).idle()
        composeTestRule.onNodeWithTag(TestTagId.FLOATING_ACTION_BUTTON).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTagId.FLOATING_ACTION_BUTTON).performClick()
        composeTestRule.onNodeWithTag(TestTagId.EDIT_NOTE_SCREEN).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TestTagId.EDIT_TEXT_MODE).performClick()
        composeTestRule.onNodeWithTag(TestTagId.EDIT_TEXT_SPACE).performClick()
        composeTestRule.onNodeWithTag(TestTagId.EDIT_TEXT_SPACE).performTextInput("asd@gmail.com")
        composeTestRule.onNodeWithTag(TestTagId.EDIT_TEXT_SPACE).assertTextEquals("asd@gmail.com")
    }

}