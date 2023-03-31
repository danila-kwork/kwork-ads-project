package sinelix.sportapp.data.news

import com.google.firebase.database.FirebaseDatabase
import sinelix.sportapp.data.news.model.News
import sinelix.sportapp.data.news.model.mapNews
import sinelix.sportapp.utils.FirebaseUtils

class NewsDataStore {

    private val db = FirebaseDatabase.getInstance(FirebaseUtils.first)

    fun getNewsList(
        onSuccess: (List<News>) -> Unit
    ){
        db.reference.child("news").get()
            .addOnSuccessListener {
                val newsList = ArrayList<News>()
                it.children.forEach {
                    newsList.add(it.mapNews())
                }
                onSuccess(newsList)
            }
    }
}