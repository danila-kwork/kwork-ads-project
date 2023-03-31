package copa.gol.bet.app.data.news.model

import com.google.firebase.database.DataSnapshot

data class News(
    val id: String,
    val description: String,
    val fullDescription: String,
    val imageUrl: String? = null,
    val title: String,
    val webUrl: String
)

fun DataSnapshot.mapNews(): News {

    val imageUrl = this.child("icon").value.toString()

    return News(
        id = this.child("id").value.toString(),
        description = this.child("desc").value.toString(),
        fullDescription = this.child("full_desc").value.toString(),
        title = this.child("name").value.toString(),
        webUrl = this.child("wikipedia_url").value.toString(),
        imageUrl = if(imageUrl == "null") null else imageUrl
    )
}