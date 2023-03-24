package ru.cfif31.eventshistory.data.firebase.realtimaDatabase.event.model

import com.google.firebase.database.DataSnapshot

data class Event(
    val date:String,
    val event:String
)

fun createEventLoading(): Event {
    return Event(
        date = "Загрузка",
        event = "Загрузка"
    )
}

fun DataSnapshot.mapEvent(): Event {
    return Event(
        date = this.child("Date").value.toString(),
        event = this.child("Event").value.toString()
    )
}