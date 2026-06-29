package com.dorronsoro.aparkau

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dorronsoro.aparkau.screen.home.HomeScreen
import com.dorronsoro.aparkau.screen.login.LoginScreen
import com.dorronsoro.aparkau.screen.sign_up.SignUpScreen

@Composable
fun AparkauNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AparkauRoutes.SIGN_UP_SCREEN
    ) {
        signUpGraph(navController)
        loginGraph(navController)

        composable(route = AparkauRoutes.HOME_SCREEN) {
            HomeScreen()
        }
    }
}

private fun NavGraphBuilder.signUpGraph(navController: NavHostController) {
    composable(route = AparkauRoutes.SIGN_UP_SCREEN) {
        SignUpScreen(
            openAndPopUp = { route, popUp ->
                navController.navigate(route) {
                    popUpTo(popUp) { inclusive = true }
                }
            }
        )
    }
}

private fun NavGraphBuilder.loginGraph(navController: NavHostController) {
    composable(route = AparkauRoutes.LOGIN_SCREEN) {
        LoginScreen(
            openAndPopUp = { route, popUp ->
                navController.navigate(route) {
                    popUpTo(popUp) { inclusive = true }
                }
            }
        )
    }
}

