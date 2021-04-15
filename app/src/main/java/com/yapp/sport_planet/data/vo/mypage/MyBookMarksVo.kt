package com.yapp.sport_planet.data.vo.mypage

data class MyBookMarksVo(
    val boardId : Long,
    val hostId : Long,
    val hostName : String,
    val title : String,
    val groupStatus : String,
    val exercise: String,
    val city : String,
    val isBookMark : Boolean,
    val recruitNumber : Int,
    val recruitedNumber : Int,
    val time : String
)