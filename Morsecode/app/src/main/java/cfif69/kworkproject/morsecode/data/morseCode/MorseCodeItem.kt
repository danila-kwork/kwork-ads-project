package cfif69.kworkproject.morsecode.data.morseCode

import kotlin.random.Random

data class MorseCodeItem(
    val symbol:String = "",
    val morse:String = ""
)

fun getMorseCodeRandom():MorseCodeItem {
    val morseCode = getMorseCode()
    val index = Random.nextInt(0,morseCode.size)

    return morseCode[index]
}

fun getMorseCode():List<MorseCodeItem> {
    return listOf(
        MorseCodeItem(symbol = "А", morse = "точка тире"),
        MorseCodeItem(symbol = "Б", morse = "тире точка точка точка"),
        MorseCodeItem(symbol = "В", morse = "точка тире тире"),
        MorseCodeItem(symbol = "Г", morse = "тире тире точка"),
        MorseCodeItem(symbol = "Д", morse = "тире точка точка"),
        MorseCodeItem(symbol = "Е", morse = "точка"),
        MorseCodeItem(symbol = "Ж", morse = "точка точка точка тире"),
        MorseCodeItem(symbol = "З", morse = "тире тире точка точка"),
        MorseCodeItem(symbol = "И", morse = "точка точка"),
        MorseCodeItem(symbol = "Й", morse = "точка тире тире тире"),
        MorseCodeItem(symbol = "К", morse = "тире точка тире"),
        MorseCodeItem(symbol = "Л", morse = "точка тире точкаточка"),
        MorseCodeItem(symbol = "М", morse = "тире тире"),
        MorseCodeItem(symbol = "Н", morse = "тире точка"),
        MorseCodeItem(symbol = "О", morse = "тире тире тире"),
        MorseCodeItem(symbol = "П", morse = "точка тире тире точка"),
        MorseCodeItem(symbol = "Р", morse = "точка тире точка"),
        MorseCodeItem(symbol = "С", morse = "точка точка точка"),
        MorseCodeItem(symbol = "Т", morse = "тире"),
        MorseCodeItem(symbol = "У", morse = "точка точка тире"),
        MorseCodeItem(symbol = "Ф", morse = "точка точка тире точка"),
        MorseCodeItem(symbol = "Х", morse = "точка точка точка точка"),
        MorseCodeItem(symbol = "Ц", morse = "тире точка тире точка"),
        MorseCodeItem(symbol = "Ч", morse = "тире тире тире точка"),
        MorseCodeItem(symbol = "Ш", morse = "тире тире тире тире"),
        MorseCodeItem(symbol = "Щ", morse = "тире тире точка тире"),
        MorseCodeItem(symbol = "Ъ", morse = "точка тире тире точка тире точка"),
        MorseCodeItem(symbol = "Ы", morse = "тире точка тире тире"),
        MorseCodeItem(symbol = "Ь", morse = "тире точка точка тире"),
        MorseCodeItem(symbol = "Э", morse = "точка точка тире точка точка"),
        MorseCodeItem(symbol = "Ю", morse = "точка точка тире тире"),
        MorseCodeItem(symbol = "Я", morse = "точка тире точка тире")
    )
}