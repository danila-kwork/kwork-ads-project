package ru.fourthproject.guessthenumber.data.firebase.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ru.fourthproject.guessthenumber.data.firebase.user.model.User

class UserDataStore {

    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.database

    fun get(
        onSuccess:(User) -> Unit
    ){
        val userId = auth.currentUser?.uid ?: return

        database.reference.child("users")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onSuccess(snapshot.getValue<User>()!!)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}