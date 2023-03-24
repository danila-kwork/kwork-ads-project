package ru.madtwoproject1.data.achievement

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.madtwoproject1.data.achievement.model.Achievement
import ru.madtwoproject1.data.achievement.model.mapAchievement
import java.util.*
import kotlin.collections.ArrayList

class AchievementDataStore {

    private val db = Firebase.database

    fun getList(onSuccess: (List<Achievement>) -> Unit){

        val achievementList = ArrayList<Achievement>()

        db.reference.child("achievement").get()
            .addOnSuccessListener {
                it.children.forEach {
                    achievementList.add(it.mapAchievement())
                }

                onSuccess(achievementList)
            }
    }

    fun create(achievement: Achievement, onSuccess: () -> Unit) {

        val id = UUID.randomUUID().toString()

        db.reference.child("achievement").child(id).setValue(achievement.copy(id = id))
            .addOnSuccessListener { onSuccess() }
    }

    fun deleteById(id: String, onSuccess: () -> Unit){
        db.reference.child("achievement").child(id).setValue(null)
            .addOnSuccessListener { onSuccess() }
    }
}