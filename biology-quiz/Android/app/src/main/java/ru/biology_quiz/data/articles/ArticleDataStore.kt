package ru.biology_quiz.data.articles

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.biology_quiz.data.articles.model.Article
import ru.biology_quiz.data.articles.model.mapArticle

class ArticleDataStore {

   private val db = Firebase.database

    fun getAll(onSuccess: (List<Article>) -> Unit) {
        val articles = ArrayList<Article>()

        db.reference.child("articles").get()
            .addOnSuccessListener {
                it.children.forEach {
                    articles.add(it.mapArticle())
                }
                onSuccess(articles)
            }
    }
}