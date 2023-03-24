package cfif31.kwork.namevalues.data.firebase.name.model

import com.google.firebase.database.DataSnapshot

data class Name(
    val id:Int,
    val name:String,
    val value:String
)

fun createNameLoading(): Name {
    return Name(
        id = 0,
        name = "Загрузка",
        value = "Загрузка"
    )
}

fun DataSnapshot.mapName(): Name {
    val id = this.child("id").value.toString().toInt()
    val name = this.child("name").value.toString()
    val value = this.child("value").value.toString()

    return Name(
        id = id,
        name = name,
        value = "Какое имя имеет значения $value ?"
    )
}