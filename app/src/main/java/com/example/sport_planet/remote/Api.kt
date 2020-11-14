package com.example.sport_planet.remote

import com.example.sport_planet.model.*
import com.example.sport_planet.model.enums.TimeFilterEnum
import com.example.sport_planet.model.request.BookMarkRequest
import com.example.sport_planet.model.request.PostBoardIdRequest
import com.example.sport_planet.model.request.PostBoardRequest
import com.example.sport_planet.model.request.ReportRequest
import com.example.sport_planet.model.response.BoardContentResponse
import com.example.sport_planet.model.response.BoardListResponse
import com.example.sport_planet.model.response.CommonResponse
import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.*

const val jwt =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZXN0ZXIxIiwiaWF0IjoxNjA0NjY3MzA5LCJleHAiOjE2MzYyMDMzMTEsImF1ZCI6IiIsInN1YiI6InRlc3RlcjFAZ21haWwuY29tIiwidXNlcklkIjoiMSJ9.Bmbhc-I1r-L-dW5vUzvB9jRsPPKtcqYXutAyWKqkPrc"

//const val jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ0ZXN0ZXIyIiwiaWF0IjoxNjA0NjY3MzA5LCJleHAiOjE2MzYyMDMzMTEsImF1ZCI6IiIsInN1YiI6InRlc3RlcjJAZ21haWwuY29tIiwidXNlcklkIjoiMiJ9.iMoSPe9k5Uj6w8qa4eaOIQqjIzI5Scuts3tBbQ4p79g"
interface Api {
    @GET("/v1/exercise")
    fun getExercise(): Single<ExerciseResponse>

    @GET("/v1/address/city")
    fun getRegion(): Single<RegionResponse>

    @POST("/v1/user/sign-in")
    fun postSignIn(@Body userInfo: LoginResponse): Single<ServerCallBackResponse>

    @POST("/v1/user/sign-up")
    fun postSignUp(@Body userSignUp: SignUpResponse): Single<ServerCallBackResponse>

    @Headers("Authorization: Bearer $jwt")
    @POST("/v1/chat/room")
    fun makeChattingRoom(
        @Body param: JsonObject
    ): Single<MakeChattingRoomResponse>

    @Headers("Authorization: Bearer $jwt")
    @GET("/v1/chat/room")
    fun getChattingRoomList(): Single<ChattingRoomListResponse>

    @Headers("Authorization: Bearer $jwt")
    @GET("/v1/chat/room/{chatRoomId}/message")
    fun getChattingMessageList(
        @Path("chatRoomId") chatRoomId: Long
    ): Single<ChattingMessageListResponse>

    @Headers("Authorization: Bearer $jwt")
    @PUT("/v1/chat/room/{chatRoomId}/message/{messageId}")
    fun makeChattingMessageRead(
        @Path("chatRoomId") boardId: Long,
        @Path("messageId") messageId: Long
    ): Single<MakeChattingMessageReadResponse>

    @Headers("Authorization: Bearer $jwt")
    @POST("/v1/board/{boardId}/apply")
    fun applyBoard(
        @Path("boardId") boardId: Long,
        @Body param: JsonObject
    ): Single<ApplyBoardResponse>

    @Headers("Authorization: Bearer $jwt")
    @POST("/v1/board/{boardId}/approve")
    fun approveBoard(
        @Path("boardId") boardId: Long,
        @Body param: JsonObject
    ): Single<ApplyBoardResponse>

    @Headers("Authorization: Bearer $jwt")
    @HTTP(method = "DELETE", path = "/v1/board/{boardId}/approve", hasBody = true)
    fun disapproveBoard(
        @Path("boardId") boardId: Long,
        @Body param: JsonObject
    ): Single<ApplyBoardResponse>

    @POST("/v1/board")
    fun postBoard(
        @Body body: PostBoardRequest
    ): Single<CommonResponse>

    @GET("/v1/board")
    fun getBoardList(
        @Query("page") page: Int = 0,
        @Query("sorting") sorting: String = TimeFilterEnum.TIME_LATEST.text,
        @Query("category") category: Long,
        @Query("city") city: Long
    ): Single<BoardListResponse>

    @GET("v1/board/{boardId}")
    fun getBoardContent(
        @Path("boardId") boardId: Long
    ): Single<BoardContentResponse>

    @DELETE("/v1/board/{boarId}")
    fun deleteBoard(
        @Path("boardId") boardId: Long
    ): Single<CommonResponse>

    @PUT("/v1/board/{boardId")
    fun editBoard(
        @Path("boardId") boardId: Long,
        @Body body: PostBoardRequest
    ): Single<BoardContentResponse>

    @POST("/v1/board/bookmark")
    fun createBookMark(
        @Body body: BookMarkRequest
    ): Single<CommonResponse>

    @DELETE("/v1/board/{boardId}/bookmark")
    fun deleteBookMark(
        @Path("boardId") boardId: Long
    ): Single<CommonResponse>

    @POST("/v1/board/hidden")
    fun hideBoard(@Body body: PostBoardIdRequest): Single<CommonResponse>

    @POST("/v1/board/report")
    fun reportBoard(@Body body: ReportRequest): Single<CommonResponse>
}