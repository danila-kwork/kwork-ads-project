package cfif31.kwork.namevalues.navigation

sealed class Screen(val route:String) {
    object MainScreen: Screen("main_screen")
}