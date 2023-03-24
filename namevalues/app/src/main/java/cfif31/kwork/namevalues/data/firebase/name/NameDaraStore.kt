package cfif31.kwork.namevalues.data.firebase.name

import cfif31.kwork.namevalues.data.firebase.name.model.Name
import cfif31.kwork.namevalues.data.firebase.name.model.mapName
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlin.random.Random

class NameDaraStore @Inject constructor(
    private val db:FirebaseDatabase
){
    fun getNameRandom(
        onSuccess:(Name) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        val nameId = Random.nextInt(0,1108).toString()
        db.reference.child("names").child(nameId).get()
            .addOnSuccessListener { onSuccess(it.mapName()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }

    }
}