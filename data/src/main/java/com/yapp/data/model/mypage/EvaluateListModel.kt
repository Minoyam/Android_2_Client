package com.yapp.data.model.mypage

data class EvaluateListModel(
    val userId : Long,
    val nickName : String,
    val isHost : Boolean,
    val isLike : Boolean,
    val isDislike : Boolean
)