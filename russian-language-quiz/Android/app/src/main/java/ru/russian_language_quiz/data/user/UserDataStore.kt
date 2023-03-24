package ru.russian_language_quiz.data.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.russian_language_quiz.data.referral_link.model.ReferralLink
import ru.russian_language_quiz.data.referral_link.model.mapReferralLink
import ru.russian_language_quiz.data.user.model.AchievementId
import ru.russian_language_quiz.data.user.model.User
import ru.russian_language_quiz.data.user.model.mapUser

class UserDataStore {

    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.database
    private val userId = auth.currentUser!!.uid

    fun get(
        onSuccess:(User) -> Unit
    ){
        database.reference.child("users")
            .child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.mapUser()
                    onSuccess(user)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    fun updateCountInterstitialAds(count: Int) {
        database.reference.child("users")
            .child(userId)
            .child("countInterstitialAds")
            .setValue(count)
    }

    fun updateCountInterstitialAdsClick(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countInterstitialAdsClick")
            .setValue(count)
    }

    fun updateCountRewardedAds(count: Int) {
        database.reference.child("users")
            .child(userId)
            .child("countRewardedAds")
            .setValue(count)
    }

    fun updateCountRewardedAdsClick(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countRewardedAdsClick")
            .setValue(count)
    }

    fun updateCountBannerAdsClick(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countBannerAdsClick")
            .setValue(count)
    }

    fun updateCountBannerAds(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countBannerAds")
            .setValue(count)
    }

    fun updateCountQuestion(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countQuestion")
            .setValue(count)
    }

    fun updateCountInterestingFact(count: Int){
        database.reference.child("users")
            .child(userId)
            .child("countInterestingFact")
            .setValue(count)
    }

    fun updateGiftMoney(count: Double){
        database.reference.child("users")
            .child(userId)
            .child("giftMoney")
            .setValue(count)
    }

    fun getReferralLink(onSuccess: (String) -> Unit){
        database.reference.child("users")
            .child(userId)
            .child("referralLink")
            .get()
            .addOnSuccessListener { onSuccess(it.value.toString()) }
    }

    fun updateReferralLink(link: String, onSuccess: () -> Unit){
        database.reference.child("users")
            .child(userId)
            .child("referralLink")
            .setValue(link)
            .addOnSuccessListener { onSuccess() }
    }

    fun addAchievementId(
        id: String,
        price: Double,
        onSuccess: () -> Unit
    ) {
        database.reference.child("users")
            .child(userId)
            .child("achievementId")
            .child(id)
            .setValue(AchievementId(id = id))
            .addOnSuccessListener {
                database.reference.child("users")
                    .child(userId)
                    .child("achievementPrice")
                    .setValue(price)
                    .addOnSuccessListener { onSuccess() }
            }
    }

    fun updateActiveReferralLink(
        referralLink: ReferralLink,
        onSuccess: () -> Unit
    ) {
        database.reference.child("users")
            .child(userId)
            .child("activeReferralLink")
            .setValue(referralLink)
            .addOnSuccessListener { onSuccess() }
    }

    fun getActiveReferralLink(
        onSuccess: (ReferralLink) -> Unit
    ) {
        database.reference.child("users")
            .child(userId)
            .child("activeReferralLink")
            .get()
            .addOnSuccessListener { onSuccess(it.mapReferralLink()) }
    }
}