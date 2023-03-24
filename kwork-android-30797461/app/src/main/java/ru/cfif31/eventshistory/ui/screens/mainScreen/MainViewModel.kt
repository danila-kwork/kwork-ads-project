package ru.cfif31.eventshistory.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.cfif31.eventshistory.data.firebase.realtimaDatabase.event.dataStore.EventDataStore
import ru.cfif31.eventshistory.data.firebase.realtimaDatabase.event.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val eventDataStore: EventDataStore
):ViewModel() {

    fun getEvents(
        onSuccess:(List<Event>) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        try {
            eventDataStore.getEvents(
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }catch (e:Exception){
            onFailure(e.message ?: "error")
        }
    }

    fun getEventRandom(
        onSuccess:(Event) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        try {
            eventDataStore.getEventRandom(
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }catch (e:Exception){
            onFailure(e.message ?: "error")
        }
    }
}