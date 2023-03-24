package cfif69.kworkproject.morsecode.navigation

sealed class Screen(val route:String) {
    object MainScreen: Screen("main_screen")
}