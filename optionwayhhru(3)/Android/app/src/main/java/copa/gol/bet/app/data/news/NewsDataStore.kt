package copa.gol.bet.app.data.news

import com.google.firebase.database.FirebaseDatabase
import copa.gol.bet.app.data.news.model.News
import copa.gol.bet.app.data.news.model.mapNews
import copa.gol.bet.app.utils.FirebaseUtils

class NewsDataStore {

    private val db = FirebaseDatabase.getInstance(FirebaseUtils.first)

    fun getNewsList(
        onSuccess: (List<News>) -> Unit
    ){
        db.reference.child("footbol_info").get()
            .addOnSuccessListener {
                val newsList = ArrayList<News>()
                it.children.forEach {
                    newsList.add(it.mapNews())
                }
                onSuccess(newsList)
            }
    }

    fun getById(id: String, onSuccess: (News) -> Unit) {
        db.reference.child("footbol_info").child(id).get()
            .addOnSuccessListener { onSuccess(it.mapNews()) }
    }
}