package com.yapp.data.model

import com.yapp.data.response.basic.ExerciseResponse
import com.yapp.data.response.basic.RegionResponse

data class CommonApiModel(
    val id: Long,
    val name: String
)

//fun CommonApiModel.toExercise(): ExerciseResponse.Data {
//    return ExerciseResponse.Data(id, name)
//}
//
//fun CommonApiModel.toCity(): RegionResponse.Data {
//    return RegionResponse.Data(id, name)
//}
//
//fun CommonApiModel.toUserTag(): UserTagModel {
//    return UserTagModel(id, name)
//}