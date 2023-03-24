package ru.cfif31.eventshistory.data.firebase.realtimaDatabase.event.dataStore

import ru.cfif31.eventshistory.data.firebase.realtimaDatabase.event.model.Event
import ru.cfif31.eventshistory.data.firebase.realtimaDatabase.event.model.mapEvent
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlin.random.Random

class EventDataStore @Inject constructor(
    private val db:FirebaseDatabase
) {

    fun getEvents(
        onSuccess:(List<Event>) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        db.reference.child("events").get()
            .addOnSuccessListener {
                onSuccess(it.children.map { it.mapEvent() })
            }
            .addOnFailureListener { onFailure(it.message ?: "error") }
    }

    fun getEventRandom(
        onSuccess:(Event) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        val eventId = Random.nextInt(0,1798).toString()
        db.reference.child("events").child(eventId).get()
            .addOnSuccessListener { onSuccess(it.mapEvent()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }
    }
}