package cfif69.kworkproject.morsecode.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import cfif69.kworkproject.morsecode.data.morseCode.MorseCodeItem
import cfif69.kworkproject.morsecode.data.morseCode.getMorseCodeRandom

class MainViewModel:ViewModel() {
    fun getMorseCode(): MorseCodeItem = getMorseCodeRandom()
}