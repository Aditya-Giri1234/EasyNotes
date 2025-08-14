package com.kin.easynotes.pure_unit_test

import com.kin.easynotes.presentation.navigation.ActionType
import com.kin.easynotes.presentation.navigation.NavRoutes
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.robolectric.ParameterizedRobolectricTestRunner.Parameters

@RunWith(Parameterized::class)
class RouteTesting(
    private val actualRoute : String,
    private val expectedRoute : String
){

    companion object{
        @JvmStatic
        @Parameterized.Parameters(name = "actualRoute : {0} , expectedRoute {1}")
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf("${NavRoutes.Edit::class.simpleName?.lowercase()}/10/true" , NavRoutes.Edit.createRoute(10,true)) ,
                arrayOf("settings/lock/${ActionType.PATTERN}" , NavRoutes.LockScreen.createRoute(ActionType.PATTERN))
            )
        }
    }
    @Test
    fun `is route valid`(){
        //Assert
        assert(expectedRoute == actualRoute)
    }
}