package en.cfif33.answer.navigation

sealed class Screen(val route:String) {
    object MainScreen: Screen("main_screen")
}