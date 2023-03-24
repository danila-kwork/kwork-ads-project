package ru.mooncalendar.data.auth.model

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.database.DataSnapshot
import ru.mooncalendar.common.extension.toDate
import ru.mooncalendar.common.extension.toLocalDate
import ru.mooncalendar.data.subscriptionStatement.model.SubscriptionStatement
import ru.mooncalendar.data.subscriptionStatement.model.SubscriptionStatementStatus
import ru.mooncalendar.data.subscriptionStatement.model.SubscriptionTime
import ru.mooncalendar.data.subscriptionStatement.model.SubscriptionType
import java.time.LocalDate
import java.util.*

data class Advice(
    val parameter: String = "",
    val state: AdviceState
)

enum class AdviceState(val text: String, val color: Color) {
    Adverse("неблагоприятный", Color.Red),
    Neutral("нейтральный", Color.Yellow),
    Favorable("благоприятный", Color.Green),
}

enum class UserRole {
    BASE_USER,
    ADMIN
}

data class User(
    val id: String,
    val email: String,
    val password: String,
    val premium: Boolean = false,
    val premiumDate: String? = null,
    val birthday: String,
    val userRole: UserRole = UserRole.BASE_USER
){
    @SuppressLint("NewApi")
    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @SuppressLint("NewApi")
    fun isSubscription(
        subscriptionStatement: SubscriptionStatement?,
        statusCheck: Boolean = true
    ): Boolean {

        if(premiumDate == null || premiumDate == "null")
            return false

        if(statusCheck){
            if(subscriptionStatement?.status == SubscriptionStatementStatus.WAITING)
                return false
        }

        val date = LocalDate.now()
        val subscriptionDate = simpleDateFormat.parse(premiumDate).toLocalDate()

        val finalDate = when(subscriptionStatement?.type?.time){
            SubscriptionTime.ONE_MONTH -> subscriptionDate.plusMonths(1)
            SubscriptionTime.ONE_YEAR -> subscriptionDate.plusYears(1)
            SubscriptionTime.UNLIMITED -> return premium
            else -> return false
        }

        return premium && date <= finalDate
    }

    @SuppressLint("NewApi")
    fun debitingFundsDate(subscriptionType: SubscriptionType): Date? {
        val subscriptionDate = simpleDateFormat.parse(premiumDate).toLocalDate()

        return when(subscriptionType.time){
            SubscriptionTime.ONE_MONTH -> subscriptionDate.plusMonths(1).toDate()
            SubscriptionTime.ONE_YEAR -> subscriptionDate.plusYears(1).toDate()
            SubscriptionTime.UNLIMITED -> null
        }
    }

    @SuppressLint("NewApi")
    fun getFinancialCode(): String {
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            .parse(this.birthday).toLocalDate()

        val dayNumber = getFinancialCodeDay(date)
        val monthNumber = getFinancialCodeMonth(date)
        val yearNumber = getFinancialCodeYear(date)
        val sum = getFinancialCodeSum(dayNumber, monthNumber, yearNumber)

        return "$dayNumber$monthNumber$yearNumber$sum"
    }

    fun getFinancialCodeSum(
        dayNumber: Int,
        monthNumber: Int,
        yearNumber: Int,
        num: Int? = null
    ): Int {
        var code = num ?: (dayNumber + monthNumber + yearNumber)
        var sum = 0

        while(code > 0){
            sum += code % 10
            code /=10
        }

        return if(sum >= 10){
            getFinancialCodeSum(dayNumber, monthNumber, yearNumber, sum)
        }else {
            sum
        }
    }

    @SuppressLint("NewApi")
    fun getFinancialCodeDay(
        date: LocalDate,
        num: Int? = null
    ) : Int {
        var dayOfMonthNumber = num ?: date.dayOfMonth
        var dayOfMonthSum = 0

        while(dayOfMonthNumber > 0){
            dayOfMonthSum += dayOfMonthNumber % 10
            dayOfMonthNumber /=10
        }

        return if(dayOfMonthSum >= 10)
            getFinancialCodeDay(date, dayOfMonthSum)
        else
            dayOfMonthSum
    }

    @SuppressLint("NewApi")
    fun getFinancialCodeMonth(
        date: LocalDate,
        num: Int? = null
    ) : Int {
        var monthNumber = num ?: date.monthValue
        var monthSum = 0

        while(monthNumber > 0){
            monthSum += monthNumber % 10
            monthNumber /=10
        }

        return if(monthSum >= 10)
            getFinancialCodeMonth(date, monthSum)
        else
            monthSum
    }


    @SuppressLint("NewApi")
    fun getFinancialCodeYear(
        date: LocalDate,
        num: Int? = null
    ) : Int {
        var yearNumber = num ?: date.year
        var yearSum = 0

        while(yearNumber > 0){
            yearSum += yearNumber % 10
            yearNumber /=10
        }

        return if(yearSum >= 10)
            getFinancialCodeYear(date, yearSum)
        else
            yearSum
    }

    @SuppressLint("NewApi")
    fun getMyDay(
        date: LocalDate,
        num: Int? = null
    ): Pair<Int, AnnotatedString> {

        val currentYear = date.year
        val currentMonth = date.month.value
        val currentDay = date.dayOfMonth

        val myMonth = getMyMonth(
            currentYear,
            currentMonth
        )

        var number = num ?: (myMonth.first + currentDay)
        var sum = 0

        while(number > 0){
            sum += number % 10
            number /=10
        }

        return when(sum){
            1 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 1. ")
                }
                append("Используйте энергию дня правильно: не уходите в эгоизм. Важно оставаться в покое и выстраивать стратегию задуманного плана.")
            }
            2 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 2.")
                }
                append(" Будьте хорошим дипломатом. Может появиться желание разорвать отношения, но их необходимо налаживать. В ресурсном состоянии через энергию понимания удастся добиться идеальных договоренностей.")
            }
            3 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 3.")
                }
                append(" Есть вероятность азарта, но за этим могут последовать потери. Будет желание получения легкой выгоды. Поэтому важно все делать включив анализ")
            }
            4 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 4.")
                }
                append(" Важно быть на позитиве, чтобы были только положительные мистические события, иначе могут быть потери и неожиданные неприятности")
            }
            5 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 5.")
                }
                append(" Все возможности приходят через коммуникацию. Налаживайте связи, знакомьтесь и общайтесь!")
            }
            6 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 6.")
                }
                append(" Сегодня могут сбыться все ваши мечты! Также возможны обострения хронических заболеваний и повышенный эмоциональный фон, а страдания из-за отсутствия комфорта в любых аспектах.")
            }
            7 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 7.")
                }
                append(" Держите себя в дисциплине: служение, молитва, занятие йогой, обучение и задача от Творца!")
            }
            8 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 8. ")
                }
                append("Труд принесет финансовый результат. Могут быть сомнения, недоверие, желание все тотально проконтролировать.")
            }
            9 -> sum to buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append("Сегодня ваш личный день 9. ")
                }
                append("Не ведитесь на эмоции, оставайтесь в покое, тогда можно обнаружить новые возможности")
            }
            else -> getMyDay(date, sum)
        }
    }

    fun getMyMonth(currentYear: Int, currentMonth: Int): Pair<Int, AnnotatedString> {
        val myYear = getMyYear(currentYear)

        var number = myYear.first + currentMonth
        var sum = 0

        while(number > 0){
            sum += number % 10
            number /=10
        }

        return sum to when(sum) {
            1 -> {
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 1 проходит под влиянием Солнца!")
                        }
                    }

                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Благоприятное воздействие:")
                    }
                    append(" - ЭГО ищет счастье, тогда необходимо открыть свое дело, создание чего-то нового, начало новой жизни.")

                    append("\n")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Деструктивное воздействие:")
                    }
                    append("- ЭГО страдает: жжение сердечной чакры, внутри возможно ощущение духоты. Человек не будет знать куда себя деть")
                }
            }
            2 -> {
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 2 проходит под влиянием Луны!")
                        }
                    }

                    append("\n")
                    append("● Можно налаживать отношения.")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Благоприятное воздействие:")
                    }
                    append("- включается дипломатия. Приходит желание  создать новые отношения или укрепить старые.")

                    append("\n")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Деструктивное воздействие:")
                    }
                    append("нельзя принимать серьезные решения в отношениях. Так как может произойти их разрыв.")
                    append("\n")
                    append("● Возможны депрессии и неуверенность.")
                }
            }
            3 -> {
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 3 проходит под влиянием Юпитера!")
                        }
                    }

                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Благоприятное воздействие:")
                    }
                    append(" станет месяцем анализа и успеха.")

                    append("\n")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Деструктивное воздействие:")
                    }
                    append(" включится азарт и разрушения.")
                }
            }
            4 -> {
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 4 проходит под влиянием Раху!")
                        }
                    }

                    append("\n")
                    append("● Месяц мистики.")
                    append("\n")
                    append("● Возможны неизвестные мистические события.")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Благоприятное воздействие:")
                    }
                    append(" — это положительные события и мистика")

                    append("\n")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Деструктивное воздействие:")
                    }
                    append(" - это отрицательные события, отрицательная Мистика")
                }
            }
            5 -> {
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 5 проходит под влиянием Меркурия!")
                        }
                    }

                    append("\n")
                    append("● Месяц коммуникаций, когда все тайное становится явным. Скелеты выпадают из шкафа. Человек будет говорить о. вещах, что «правильно и неправильно».")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Благоприятное воздействие:")
                    }
                    append(" здоровая коммуникация и новые связи.")

                    append("\n")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Деструктивное воздействие:")
                    }
                    append(" разрушается логика.")
                }
            }
            6 -> {
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 6 проходит под влиянием Венеры!")
                        }
                    }

                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Благоприятное воздействие:")
                    }
                    append(" развитие, успех и могут сбываться мечты.")

                    append("\n")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Деструктивное воздействие:")
                    }
                    append(" обострение хронических заболеваний. Стоит завершить все незаконченные дела.")
                }
            }
            7 -> {
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 7 проходит под влиянием Кету!")
                        }
                    }

                    append("\n")
                    append("● Нужна дисциплина, медитации и духовность")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Благоприятное воздействие:")
                    }
                    append(" трансформация сознания, где необходимо полностью изменить свое сознание.")

                    append("\n")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Деструктивное воздействие:")
                    }
                    append(" кризис и страдания. Кризис воспринимать как избавление от чего-то ненужного. Важно трансформироваться, уйти от страданий.")
                }
            }
            8 -> {
                buildAnnotatedString {
                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 8 проходит под влиянием Сатурна!")
                        }
                    }

                    append("\n")
                    append("● Месяц реализации кармы. Человек должен учиться и работать. Уходите от тотального контроля ситуации!")
                }
            }
            9 -> {
                buildAnnotatedString {

                    withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                            append("Ваш личный месяц 9 проходит под влиянием Марса!")
                        }
                    }

                    append("\n")
                    append("● Нужно принимать все, что происходит.")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Благоприятное воздействие:")
                    }
                    append(" принимать ситуацию.")

                    append("\n")
                    append("\n")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("● Деструктивное воздействие:")
                    }
                    append(" будьте готовы ко всему. В этом месяце нужно проявлять волю к победе. Сабр.")
                }
            }
            else -> buildAnnotatedString {}
        }
    }

    @SuppressLint("NewApi")
    fun getMyYearShortText(currentYear: Int): AnnotatedString {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = simpleDateFormat.parse(this.birthday).toLocalDate()

        var year = currentYear
        var yearSum = 0

        while(year > 0){
            yearSum += year % 10
            year /=10
        }

        var month = date.month.value
        var monthSum = 0

        while(month > 0){
            monthSum += month % 10
            month /=10
        }

        var day = date.dayOfMonth
        var daySum = 0

        while(day > 0){
            daySum += day % 10
            day /=10
        }

        var sum = yearSum + monthSum + daySum
        var number = 0

        while(sum > 0){
            number += sum % 10
            sum /=10
        }

        return when(number) {
            1 -> buildAnnotatedString {
                append("ГОД 1 : СОЛНЦЕ")
            }
            2 -> buildAnnotatedString {
                append("ГОД 2: ЛУНЫ")
            }
            3 -> buildAnnotatedString {
                append("ГОД 3: ЮПИТЕР")
            }
            4 -> buildAnnotatedString {
                append("ГОД 4: РАХУ")
            }
            5 -> buildAnnotatedString {
                append("ГОД 5: МЕРКУРИЙ")
            }
            6 -> buildAnnotatedString {
                append("ГОД 6: ВЕНЕРА")
            }
            7 -> buildAnnotatedString {
                append("ГОД 7: КЕТУ")
            }
            8 -> buildAnnotatedString {
                append("ГОД 8: САТУРН")
            }
            9 -> buildAnnotatedString {
                append("ГОД 9 – МАРС")
            }
            else -> buildAnnotatedString {  }
        }
    }

    @SuppressLint("NewApi")
    fun getMyYear(currentYear: Int): Pair<Int, AnnotatedString> {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = simpleDateFormat.parse(this.birthday).toLocalDate()

        var year = currentYear
        var yearSum = 0

        while(year > 0){
            yearSum += year % 10
            year /=10
        }

        var month = date.month.value
        var monthSum = 0

        while(month > 0){
            monthSum += month % 10
            month /=10
        }

        var day = date.dayOfMonth
        var daySum = 0

        while(day > 0){
            daySum += day % 10
            day /=10
        }

        var sum = yearSum + monthSum + daySum
        var number = 0

        while(sum > 0){
            number += sum % 10
            sum /=10
        }

        return number to when(number) {
            1 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 1 : СОЛНЦЕ\n")
                        append("«НАЧАЛО НОВОЙ ЖИЗНИ»")
                    }
                }

                append("\n")
                append(" Когда приходит год Солнца, открывается чистый лист, заполнение которого вы вольны начать в любом направлении. Данный год также можно назвать периодом посева семян, которые в последующие годы начнут приносить плоды. Поэтому человеку следует открыть новое дело (начинается новая жизнь). Так энергия в данном цикле хочет и может свернуть горы.")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие: ")
                }
                append("Принятие решений. Стратегия. Плодотворное начало (когда человек в ресурсном состоянии, он может открыть собственное прибыльное дело).")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }

                append("\n")

                append(" Человеку следует смотреть на мир по-новому:")

                append("\n")
                append("\n")
                append("- Открыть новое дело.\n")
                append("- Принимать решения.\n")
                append("- Нарабатывать лидерские качества.\n")
                append("- Развивать стратегическое мышление.\n")
                append("\n")
                append(" В прошлом году в вашей жизни завершился какой-либо этап, что-то подошло к концу, логическому завершению. Даже если не было таких глобальных революций, изменениям могли подвергнуться ваш подход к жизни или взгляд на мир.")
            }
            2 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 2: ЛУНЫ")
                        append("\n")
                        append("«СВЯЗИ И ВЗАИМООТНОШЕНИЯ»")
                    }
                }

                append("\n")
                append(" Вы можете чувствовать замедление и отсутствие прогресса, эффекта своих действий, но это не так. В течение этого года вы будете медленно, но, верно продвигаться к своей цели. Можно построить крепкие отношения с людьми и второй половинкой. Главное не уходить в депрессию.")

                append("\n")
                append("\n")
                append(" Разорванные в этот год отношения по большей вероятности не наладятся в последующем, поэтому следует все обдумать.")
                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие:")
                }
                append(" Реалистичность. Чувственность. Психология. Нежность. Дипломатичность.")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }
                append(" Сомнение. Двойственность. Депрессивность. Будут разрывы отношений и депрессии (страдания).")

                append("\n")
                append("\n")

                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("Что нужно делать:")
                    }
                }

                append("\n")
                append("- Учиться понимать людей̆")
                append("\n")
                append("- Прийти к пониманию данности человека")
            }
            3 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 3: ЮПИТЕР")
                        append("\n")
                        append("«УСПЕХ И УСКОРЕНИЕ»")
                    }
                }

                append("\n")
                append("Ваши социальные проекты, профессиональные дела начнут заметно улучшаться. У вас появится много новых перспектив, успехи тоже не заставят себя долго ждать, если вы не уйдете в свои принципы и погрузитесь в азарт.")
                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие: ")
                }


                append("Стратегия, анализ, планирование")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }
                append("Принципиальность. Лень. Азарт. Категоричность. Это азарт и разрушения (Действия азартные).")

                append("\n")
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("Что нужно делать:")
                    }
                }

                append("\n")
                append("- Вести ежедневник.")
                append("\n")
                append("- Анализировать ситуации из жизни.")
                append("\n")
                append("- Планировать свой день и вечером проводить его анализ.")
                append("\n")
                append("- Контролировать деньги.")
            }
            4 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 4: РАХУ")
                        append("\n")
                        append("«ЦЕЛИ И ПЛАНИРОВАНИЯ»")
                    }
                }

                append("\n")
                append("Этот цикл в целом вращается вокруг работы, организации своего времени, планирования деятельности, разработки целей, построения своего будущего.")
                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие: ")
                }
                append("Знания. Механизм постановки целей. Желание отдать Механизм реализации целей. Позитивные мистические события, которые не поддаются здравому смыслу. Пример: внезапное получение гранта на обучение или премия на работе, и т.д.")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }
                append(" Неудовлетворенность. Разрушение. Отчужденность. Апатия. Мошенничество. Отрицательные мистические события. Пример: выпишут штраф, аннулируют результаты труда, и т.д.")

                append("\n")
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("Что нужно делать:")
                    }
                }

                append("\n")
                append("- Получать знания и определять цели.")
                append("\n")
                append("- Тратить деньги на курсы и обучения, получать сертификаты.")
                append("\n")
                append("- Выписать 100-400 своих целей.")
                append("\n")
                append("- Освоить инструменты целеполагания.")
            }
            5 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 5: МЕРКУРИЙ")
                        append("\n")
                        append("«ГОД ИНТЕЛЛЕКТА И ЛОГИКИ»")
                    }
                }

                append("\n")
                append("В этот период в вас по большей вероятности проснется бунтарь, который захочет вырваться из оков, двинуться в кругосветное путешествие, сменить номер телефона или дистанцироваться от всех знакомых.")
                append("\n")
                append("\n")
                append(" Год, когда всё тайное становится явным (скелеты выпадают из шкафа).")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие: ")
                }
                append("это проявление коммуникации. Деловая коммуникация. Коммуникации. Логика. Адекатное восприятие. Интеллект.")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }
                append("Непостоянство. Беспечность. Безумие. Экстремизм. Обидчивость. Полное разрушение логики (Человек будет говорить правильно/неправильно). Жизнь покажется нелогичной̆.")

                append("\n")
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("Что нужно делать:")
                    }
                }

                append("\n")
                append("- Ежедневно коммуницировать с 1-5 человеком выше себя по уровню успешности, компетенции и доходах в поставленной цели.")
                append("\n")
                append("- Позвонить и попросить прощения у них, поблагодарить их за то, что он преподал урок и был учителем для нас.")
                append("\n")
                append("- Простить долги и обязательно позвонить этим людям и сказать им, что вы простили им их долги и что вы всегда рады им в гостях на чай.")
                append("\n")
                append("- Масштабироваться (например проводите обучение: надо значит провести в другом месте обучение, или если готовите торты - значит начните печь еще печенье, открыли свою кофейню - значит настало время открыть еще одну и т.д")
            }
            6 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 6: ВЕНЕРА")
                        append("\n")
                        append("«ГОД ЛИЧНОЙ ЖИЗНИ И ЖЕЛАНИЙ»")
                    }
                }

                append("\n")
                append(" Если ранее на первом месте у вас были карьера, социальная жизнь либо другие цели, то в шестом цикле все изменится, и на первый план у вас выйдет личная жизнь.")

                append("\n")
                append("\n")
                append(" Вы обнаружите в себе потребность в стабилизации, укреплении связей, строительстве метафорического дома, который станет вашим убежищем и движущей силой. Также у вас может возникнуть потребность в безопасной гавани и человеке, который будет с вами делить успехи и неудачи.")

                append("\n")
                append("\n")
                append(" В этом году могут сбыться все мечты и желания. ")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие: ")
                }
                append("год любви и успеха. Мудрость. Счастье. Завершение начатых дел. Внутренний̆ комфорт")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }
                append("хронических заболеваний Сверхкомфорт. Противозаконность. Неразборчивость. Месть")

                append("\n")
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("Что нужно делать:")
                    }
                }

                append("\n")
                append("- Прочитать книгу Г. Чепмена “Пять языков любви”.")
                append("\n")
                append("- Благодарить Творца с утра и перед сном с конкретным перечислением за что.")
                append("\n")
                append("- Вода, баня, сауна, бассейн, верховая езда.")
                append("\n")
                append("- Созерцать природу, смотреть на прекрасное, вдохновение.")
                append("\n")
                append("- Медитация на любовь.")
                append("\n")
                append("- Дневник Успеха (каждый̆ день по пяти пунктов, за что можешь себя похвалить).")
            }
            7 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 7: КЕТУ.")
                        append("\n")
                        append("«ГОД ДУХОВНОГО РАЗВИТИЯ И СТРАДАНИЙ»")
                    }
                }
                append("\n")
                append(" В этом жизненном цикле пришло время заглянуть внутрь себя и искать способы стать еще лучше, более умными и развитыми. Прежде всего, вы делаете ставку на духовное развитие, станете искать ответы экзистенциального характера.")
                append("\n")
                append("\n")
                append(" Задача - уйти от страданий и прийти в трансформацию.")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие: ")
                }
                append("Гениальность. Божественный бензин. Кундалини. Внутренний реактор (Двигатель). Темпераментность (Секс. энергия).")

                append("\n")
                append(" Будет трансформация сознания, расширение сознания, выход на новый уровень.")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }
                append(" Хаос. Рассеянность. Непонимание ситуации. Сверхуверенность. Кризис.")

                append("\n")
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("Что нужно делать:")
                    }
                }

                append("\n")
                append("- Заниматься йогой")
                append("\n")
                append("- Стоять на гвоздях")
                append("\n")
                append("- Плавание")
                append("\n")
                append("- Медитации")
                append("\n")
                append("- Секс")
                append("\n")
                append("- Ходить в баню.")
            }
            8 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 8: САТУРН")
                        append("\n")
                        append("«ГОД ИНВЕСТИЦИЙ»")
                    }
                }

                append("\n")
                append("  Пора собирать урожай и работать на поле своего труда. Время работы над качеством своей деятельности. В этот год необходимо много учиться и много работать, т.е. инвестировать в обучение. Вы пожинаете плоды, но и работаете с ними")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие: ")
                }
                append("Опыт. Повторение. Труд. Карма. Мудрость прошлых жизней̆. Контроль. Если человек учится и работает — это всё будет для него опытом на долгие годы. ")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }
                append("Сомнение. Недоверие. Тотальный контроль. Человек попадает в зону ограничений.")

                append(" В этом году нельзя прожигать время в кайф. Брак, заключённый̆ в этот год, может быть расторгнут через 10–15 лет. Нельзя брать кредиты и расширять бизнес.\n")

                append("\n")
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("Что нужно делать:")
                    }
                }

                append("\n")
                append("- Трудиться 20 часов в день.")
                append("\n")
                append("- Нарабатывать навыки.")
                append("\n")
                append("- Стать профессионалом.")
            }
            9 -> buildAnnotatedString {
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("ГОД 9 – МАРС.")
                        append("\n")
                        append("«ГОД ЗАВЕРШЕНИЯ»")
                    }
                }

                append("\n")
                append(" Время попрощаться с людьми, которые перестали быть с вами на одной волне, завершить отношения, у которых давно истек «срок годности», расторгнуть договоренности, которые не дают вам больше ни удовлетворения, ни возможностей дальнейшего развития")

                append("\n")
                append("\n")
                append(" Вы очистите сейчас свое жизненное пространство. В конце 9 циклов жизни вы будете чувствовать, что через вашу жизнь прошло торнадо и после него осталась пустота. Но не стоит беспокоиться, ведь это только место, которое скоро заполнится чем-то новым, лучшим!")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Благоприятное воздействие: ")
                }
                append("Механическое действие. Идеи. Возможности. Действие. Эмоции. Служение. Понимание, что разрушения неизбежны (родившись, человек должен знать, что умрёт).")

                append("\n")
                append("\n")
                withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                    append(" Деструктивное: ")
                }
                append("Ярость. Паранойя. Наивность. Разрушение.")

                append(" Если человек боится, его год смерти неизбежен. В этот год делать максимум жертвоприношений.")
                append(" ")

                append("\n")
                withStyle(style = ParagraphStyle(textAlign = TextAlign.Center)){
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)){
                        append("Что нужно делать:")
                    }
                }

                append("\n")
                append("- Действовать.")
                append("\n")
                append("- Следить за здоровьем тела")
                append("\n")
                append("- Заниматься соревновательным спортом - придёт ощущение своих возможностей и уверенность в себе. Пройти курсы по принятию решения и действия, например, пройти курс “Спарта”.")
            }
            else -> buildAnnotatedString {}
        }
    }
}

fun DataSnapshot.mapUser(): User {
    return User(
        id = this.child("id").value.toString(),
        email = this.child("email").value.toString(),
        password = this.child("password").value.toString(),
        premium = this.child("premium").value.toString().toBoolean(),
        premiumDate = this.child("premiumDate").value.toString(),
        birthday = this.child("birthday").value.toString(),
        userRole = enumValueOf(this.child("userRole").value.toString()),
    )
}

@SuppressLint("NewApi")
fun getAdvice(
    date: LocalDate
): List<Advice> {

    var day = date.dayOfMonth

    if(day % 10 == 0){
        return listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Adverse
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Adverse
            ),
            Advice(
                "Начало проекта",
                AdviceState.Adverse
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Adverse
            )
        )
    }

    var sum = 0

    while(day > 0){
        sum += day % 10
        day /=10
    }

    return when(sum){
        1 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Neutral
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Neutral
            ),
            Advice(
                "Начало проекта",
                AdviceState.Favorable
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Neutral
            )
        )
        2 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Neutral
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Neutral
            ),
            Advice(
                "Начало проекта",
                AdviceState.Neutral
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Neutral
            ),
        )
        3 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Favorable
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Favorable
            ),
            Advice(
                "Начало проекта",
                AdviceState.Favorable
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Favorable
            ),
        )
        4 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Neutral
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Neutral
            ),
            Advice(
                "Начало проекта",
                AdviceState.Neutral
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Neutral
            ),
        )
        5 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Neutral
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Neutral
            ),
            Advice(
                "Начало проекта",
                AdviceState.Neutral
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Neutral
            ),
        )
        6 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Favorable
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Favorable
            ),
            Advice(
                "Начало проекта",
                AdviceState.Favorable
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Favorable
            ),
        )
        7 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Neutral
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Neutral
            ),
            Advice(
                "Начало проекта",
                AdviceState.Neutral
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Neutral
            ),
        )
        8 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Favorable
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Favorable
            ),
            Advice(
                "Начало проекта",
                AdviceState.Favorable
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Favorable
            ),
        )
        9 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Neutral
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Neutral
            ),
            Advice(
                "Начало проекта",
                AdviceState.Neutral
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Neutral
            ),
        )
        10, 20, 30 -> listOf(
            Advice(
                "Деловые сделки",
                AdviceState.Adverse
            ),
            Advice(
                "Регистрация брака",
                AdviceState.Adverse
            ),
            Advice(
                "Начало проекта",
                AdviceState.Adverse
            ),
            Advice(
                "Медицинские операции",
                AdviceState.Adverse
            ),
        )
        else -> emptyList()
    }
}