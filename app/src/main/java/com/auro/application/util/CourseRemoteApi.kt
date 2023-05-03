package com.auro.application.util

import com.auro.application.core.common.AppConstant
import com.auro.application.core.network.URLConstant
import com.auro.application.home.data.FaqCategoryDataModel
import com.auro.application.home.data.FaqQuestionDataModel
import com.auro.application.home.data.model.*
import com.auro.application.home.data.model.CourseModule.*
import com.auro.application.home.data.model.passportmodels.PassportQuizMonthModel
import com.auro.application.home.data.model.passportmodels.PassportQuizTopicModel
import com.auro.application.home.data.model.response.*
import com.auro.application.home.data.model.signupmodel.response.RegisterApiResModel
import com.auro.application.home.data.model.signupmodel.response.SetUsernamePinResModel
import com.auro.application.teacher.data.model.StudentListDataModel
import com.auro.application.teacher.data.model.response.*
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers


interface CourseRemoteApi
{
  @POST("fetchCourseDetailsByPartnerID")
  public fun getActiveCourseList(@Body params:HashMap<String,String>):Call<CourseList>

  @POST("fetchModuleDetailsByCourseID")
  public fun getModuleList(@Body params:HashMap<String,String>):Call<ModuleList>

  @POST("fetchTaskDetailsByCourseID")
  public fun getTaskList(@Body params:HashMap<String,String>):Call<ModuleTaskList>

  @POST("fetchChapterDetailsByModuleID")
  public fun getModuleChapterList(@Body params:HashMap<String,String>):Call<ModuleChapterList>

  @POST("fetchTaskQuestion")
  public fun getGenerateLink(@Body params:HashMap<String,String>):Call<GenerateLinkList>

  @POST("fetchPageDetailsByChapterID")
  public fun getChapterList(@Body params:HashMap<String,String>):Call<ChapterDetailList>

  @POST("viewCertificateByCourseID")
  public fun getCertificateList(@Body params:HashMap<String,String>):Call<CertificateList>
  @POST("https://lmsapi.projectinclusion.in/api/UserAuthentication/PartnerLogin")
  public fun getRegister(@Body params:HashMap<String,String>):Call<RegisterData>

  @POST("submitCourseDetails")
  public fun getSubmit(@Body params:HashMap<String,String>):Call<ChapterDetailList>

  @POST("certificateGenerate")
  public fun getCertificate(@Body params:HashMap<String,String>):Call<ChapterDetailList>

  companion object
  {
    operator fun invoke():CourseRemoteApi
    {
      return Retrofit.Builder()
        .baseUrl(URLConstant.COURSEBASE_URL)

        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CourseRemoteApi::class.java)
    }
  }
}