package sinelix.sportapp.data.news.model

import com.google.firebase.database.DataSnapshot

data class News(
    val description: String,
    val imageUrl: String? = null,
    val title: String,
    val webUrl: String
)

fun DataSnapshot.mapNews(): News {

    val imageUrl = this.child("image_url").value.toString()

    return News(
        description = this.child("description").value.toString(),
        title = this.child("title").value.toString(),
        webUrl = this.child("web_url").value.toString(),
        imageUrl = if(imageUrl == "null") null else imageUrl
    )
}