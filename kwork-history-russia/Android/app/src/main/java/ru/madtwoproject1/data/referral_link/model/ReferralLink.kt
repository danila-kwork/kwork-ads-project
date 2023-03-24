package ru.madtwoproject1.data.referral_link.model

import com.google.firebase.database.DataSnapshot

data class ReferralLink(
    val id: String,
    val userId: String
)

fun DataSnapshot.mapReferralLink(): ReferralLink {
    return ReferralLink(
        id = child("id").value.toString(),
        userId = child("userId").value.toString()
    )
}