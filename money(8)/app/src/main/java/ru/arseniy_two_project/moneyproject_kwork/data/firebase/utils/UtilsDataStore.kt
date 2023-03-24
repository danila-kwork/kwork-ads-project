package ru.arseniy_two_project.moneyproject_kwork.data.firebase.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.arseniy_two_project.moneyproject_kwork.data.firebase.utils.model.Utils
import ru.arseniy_two_project.moneyproject_kwork.data.firebase.utils.model.mapUtils

class UtilsDataStore {

    private val database = Firebase.database

    fun get(onSuccess: (Utils) -> Unit, onError:(message:String) -> Unit = {}) {

        database.reference.child("utils").get()
            .addOnSuccessListener {
                onSuccess(it.mapUtils())
            }
            .addOnFailureListener {
                onError(it.message ?: "Ошибка")
            }
    }

    fun create(utils: Utils, onSuccess: () -> Unit) {
        database.reference.child("utils").setValue(utils)
            .addOnSuccessListener { onSuccess() }
    }
}