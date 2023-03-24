package ru.madtwoproject1.data.referral_link

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.madtwoproject1.data.referral_link.model.ReferralLink
import ru.madtwoproject1.data.referral_link.model.mapReferralLink
import ru.madtwoproject1.data.user.UserDataStore
import java.util.UUID

class ReferralLinkDataStore {

    private val db = Firebase.database
    private val auth = Firebase.auth
    private val userDataStore = UserDataStore()

    fun get(id: String, onSuccess: (ReferralLink) -> Unit, onFailed: () -> Unit) {
        try {
            db.reference.child("referral_link").child(id).get()
                .addOnSuccessListener {
                    val referralLink = it.mapReferralLink()

                    if(referralLink.id == "null" || referralLink.userId == "null"){
                        onFailed()
                        return@addOnSuccessListener
                    }

                    userDataStore.updateActiveReferralLink(referralLink){
                        onSuccess(referralLink)
                    }
                }
                .addOnFailureListener { onFailed() }
        }catch (e:Exception){
            onFailed()
        }
    }

    fun create(onSuccess: (String) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val id = UUID.randomUUID().toString()

        val model = ReferralLink(
            id = id,
            userId = userId
        )

        userDataStore.getReferralLink { link ->
            if(link == "null"){
                db.reference.child("referral_link").child(id).setValue(model)
                    .addOnSuccessListener {
                        userDataStore.updateReferralLink(id){
                            onSuccess(id)
                        }
                    }
            }else {
                onSuccess(link)
            }
        }
    }
}