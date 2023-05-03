package com.auro.application.util

import com.auro.application.core.common.AppConstant
import com.auro.application.core.network.URLConstant
import com.auro.application.home.data.FaqCategoryDataModel
import com.auro.application.home.data.FaqQuestionDataModel
import com.auro.application.home.data.model.*
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


interface RemoteApi
{
  @GET("states")
  public fun getStateData():Call<StateDataModel>

    @GET("grade_list")
    public fun getGrade():Call<GradeDataModel>

    @POST("get_parent_details")
    public fun getParentData(@Body params:HashMap<String,String>):Call<ParentProfileDataModel>

    @POST("add_new_school")
    public fun addNewSchool(@Body params:HashMap<String,String>):Call<AddNewSchoolResModel>

    @Headers("Secret-Id: 2198f0011288666d3694ccf4e7d16c29",
        "Secret-Key: f7115915ae4efc1bdab7ae9fc686348848f8cc2e7bf4a9")
    @POST("PartnerListGetBySubjectCode")
    public fun partnerList(@Body params:HashMap<String,String>):Call<PartnerModel>

    @Headers("Secret-Id: 2198f0011288666d3694ccf4e7d16c29",
        "Secret-Key: f7115915ae4efc1bdab7ae9fc686348848f8cc2e7bf4a9")
    @POST("PartnerClickRecord")
    public fun partnerClick(@Body params:HashMap<String,String>):Call<PartnerModel>

    @POST("show_rating_popup")
    public fun getShowRatingPopup(@Body params:HashMap<String,String>): Call<ShowRatingPopup>

//    @POST("add_rating")
@POST("PostUserRatingClicks")
    public fun getAddRating(@Body params:HashMap<String,String>): Call<AddRatingModel>

    @POST("passport")
    public fun getPassportData(@Body params:HashMap<String,String>):Call<PassportStudentDetailResModel>

    @POST("all_student_passport")
    public fun getStudentPassportData(@Body params:HashMap<String,String>):Call<TeacherStudentPassportResModel>

    @POST("get_quiz_months")
    public fun getStudentPassportMonth(@Body params:HashMap<String,String>):Call<TeacherStudentPassportDetailResModel>

    @POST("get_quiz_month_subjects")
    public fun getQuizMonthSubject(@Body params:HashMap<String,String>):Call<PassportQuizMonthModel>


    @POST("get_topic_list")
    public fun getQuizTopic(@Body params:HashMap<String,String>):Call<PassportQuizTopicModel>

    @Headers("Secret-Id: 2198f0011288666d3694ccf4e7d16c29",
        "Secret-Key: f7115915ae4efc1bdab7ae9fc686348848f8cc2e7bf4a9",
        "Lang-Code: English")
    @POST("GeneratedCertificace")
    public fun getGeneratedCertificace(@Body params:HashMap<String,String>):Call<PassportQuizMonthModel>
    @POST("get_user_details")
    public fun getStudentData(@Body params:HashMap<String,String>):Call<GetStudentUpdateProfile>
    @POST("get_teacher_profile")
    public fun getTeacherData(@Body params:HashMap<String,String>):Call<MyProfileResModel>

    @POST("check_kyc_status")
    public fun getCheckKYC(@Body params:HashMap<String,String>):Call<GetStudentUpdateProfile>

    @POST("student_full_details")
    public fun getStatusForInsta(@Body params:HashMap<String,String>):Call<DashboardResponselDataModel>

    @POST("get_kyc_details")
    public fun getKYCStatus(@Body params:HashMap<String,String>):Call<StudentKycStatusResModel>

    @POST("get_user_slab")
    public fun getSlabAPI(@Body params:HashMap<String,String>):Call<SlabsResModel>

    @POST("show_pending_refferals")
    public fun getPendingRefferal(@Body params:HashMap<String,String>):Call<ReferralPopUpDataModel>

    @POST("check_username")
    public fun checkexist(@Body params:HashMap<String,String>):Call<ParentProfileDataModel>

  @POST("districts")
  public fun getDistrict(@Body params:HashMap<String,String>):Call<DistrictDataModel>

    @POST("get_language_transalation")
    public fun getSchoolmedium(@Body params:HashMap<String,String>):Call<SchoolMediumLangDataModel>

    @POST("fetch_student_preference")
    public fun getStudentSubject(@Body params:HashMap<String,String>):Call<SubjectPreferenceDataModel>


    @POST("user_check_new")
    public fun getUserCheck(@Body params:HashMap<String,String>):Call<CheckUserResModel>

    @POST("invite_school_teachers_list")
    public fun getInviteTeacherList(@Body params:HashMap<String,String>):Call<TeacherInviteTeacherResModel>

    @Headers("Secret-Id: 2198f0011288666d3694ccf4e7d16c29",
        "Secret-Key: f7115915ae4efc1bdab7ae9fc686348848f8cc2e7bf4a9",
        "Lang-Code: English")
//    @POST("Getmycertificates")
    @POST("GetCertificate")
    public fun getCertificate(@Body params:HashMap<String,String>):Call<CertificateResModel>

    @Headers("Secret-Id: 2198f0011288666d3694ccf4e7d16c29",
        "Secret-Key: f7115915ae4efc1bdab7ae9fc686348848f8cc2e7bf4a9",
        "Lang-Code: English")
    @POST("reffer")
    public fun getreffertostudent(@Body params:HashMap<String,String>):Call<DynamiclinkResModel>


    @POST("GroupStudentDetailsByGroupIb")
    public fun getGroupStudentDetailsByGroupIb(@Body params:HashMap<String,String>):Call<StudentListDataModel>


    @POST("send_invite_to_school_teachers")
    public fun getSendInvite(@Body params:HashMap<String,String>):Call<TeacherInviteTeacherResModel>

    @POST("teacher_approved_status")
    public fun getApproveInvite(@Body params:HashMap<String,String>):Call<TeacherInviteTeacherResModel>


    @POST("delete_teacher_from_list")
    public fun getDeleteTeacher(@Body params:HashMap<String,String>):Call<TeacherInviteTeacherResModel>

    @POST("teacher_referred_list")
    public fun getTeacherBuddyList(@Body params:HashMap<String,String>):Call<AcceptTeacherBuddyResModel>

    @POST("teacher_buddy_list")
    public fun getMyBuddyList(@Body params:HashMap<String,String>):Call<MyBuddyResModel>

    @POST("teacher_buddy_list")
    public fun getTestMyBuddyList(@Body params:HashMap<String,String>):Call<AcceptTeacherBuddyResModel>

    @POST("teacher_buddy_list")
    public fun getMyBuddyListInvite(@Body params:HashMap<String,String>):Call<MyBuddyResModel>

    @POST("teacher_invitation_list")
    public fun getTeacherInvitationList(@Body params:HashMap<String,String>):Call<ReceiveTeacherBuddyResModel>


    @POST("set_user_pin")
    public fun setparentpin(@Body params:HashMap<String,String>):Call<CheckUserResModel>

    @POST("compare_documents")
    public fun setComparedoc(@Body params:HashMap<String,String>):Call<UpdateParentProfileResModel>

    @POST("get_child_details")
    public fun getAllChild(@Body params:HashMap<String,String>):Call<GetAllChildModel>

    @POST("user_check_new")
    public fun getAllLogindetail(@Body params:HashMap<String,String>):Call<CheckUserResModel>

    @POST("get_all_referrals")
    public fun getAllChildRefer(@Body params:HashMap<String,String>):Call<GetAllChildReferModel>

    @POST("referral_response")
    public fun postrefer(@Body params:HashMap<String,String>):Call<GetAllChildModel>

    @POST("student_pin_login")
    public fun getStudentLogin(@Body params:HashMap<String,String>):Call<CheckUserResModel>

    @POST("set_user_pin")
    public fun setparentuserpin(@Body params:HashMap<String,String>):Call<SetUsernamePinResModel>

//    @FormUrlEncoded
//    @POST("get_user_details")
//    public fun getUserProfile(@Field("user_id")String user_id));


    @POST("get_user_details")
    fun get_user_details(@Body body: RequestBody?): Call<ChildDetailResModel?>?

    @POST("register_user")
    public fun setUserRegistered(@Body params:HashMap<String,String>):Call<RegisterApiResModel>

  @POST("get_translated_master")
  public fun getGender(@Body params:HashMap<String,String>):Call<GenderDataModel>

    @POST("get_translated_master")
    public fun getAllData(@Body params:HashMap<String,String>):Call<GenderDataModel>

    @POST("FaqCategoryByLanguageId")
    public fun getAllFaqCategory(@Body params:HashMap<String,String>):Call<FaqCategoryDataModel>

    @POST("FaqQuestionAnswerByLanguageId")
    public fun getAllFaqQuestion(@Body params:HashMap<String,String>):Call<FaqQuestionDataModel>

  @POST("get_schools")
  public fun getSchool(@Body params:HashMap<String,String>):Call<SchoolDataModel>

    @POST("set_username_pin")
    public fun setUsernamePin(@Body params:HashMap<String,String>):Call<SetUsernamePinResModel>

  @Multipart
  @POST("update_user_details")
  public fun updateuserdetail(@Part("build_version")build_Version:RequestBody,
                              @Part("school_name") schoolName:RequestBody ,
                              @Part("email") emailId:RequestBody ,
                              @Part("gender") gender:RequestBody ,
                              @Part("partner_source") partner_source:RequestBody ,
                              @Part("device_token") deviceToken:RequestBody ,
                              @Part("state_id") stateCode:RequestBody ,
                              @Part("district_id") distcitcode:RequestBody ,
                              @Part("username") firstName:RequestBody ,
                              @Part("user_id") userId:RequestBody ,
                              @Part("student_name") studentname:RequestBody ,
                              @Part("user_prefered_language_id") preflanguageid:RequestBody ,
                              @Part image:MultipartBody.Part
                               ):Call<StudentResponselDataModel>

    @Multipart
    @POST("update_user_details")
    public fun updateaddnewchilddetail(@Part("build_version")build_Version:RequestBody,
                                       @Part("partner_source") partner_source:RequestBody ,
                                @Part("school_name") schoolName:RequestBody ,
                                @Part("email") emailId:RequestBody ,
                                       @Part("board_type") boardType:RequestBody ,
                                @Part("gender") gender:RequestBody ,
                                       @Part("regitration_source") registersource:RequestBody ,
                                       @Part("share_type") sharetype:RequestBody ,
                                       @Part("device_token") deviceToken:RequestBody ,
                                       @Part("email_verified") emailverify:RequestBody ,
                                       @Part("is_new") isnew:RequestBody ,
                                       @Part("student_grade") studentgrade:RequestBody ,
                                       @Part("share_identity") shareidentity:RequestBody ,
                                       @Part("user_partner_id") userpartnerid:RequestBody ,
                                       @Part("ip_address") ipaddress:RequestBody ,
                                       @Part("mobile_version") mobileversion:RequestBody ,
                                       @Part("mobile_model") mobilemode:RequestBody ,
                                       @Part("private_tution_type") privatetution:RequestBody ,
                                       @Part("is_private_tution") isprivatetution:RequestBody ,
                                       @Part("latitude") latitude:RequestBody ,
                                       @Part("longitude") longitude:RequestBody ,
                                       @Part("language") language:RequestBody ,
                                       @Part("mobile_manufacturer") mobilemanufacture:RequestBody ,
                                       @Part("state_id") stateCode:RequestBody ,
                                @Part("district_id") distcitcode:RequestBody ,
                                       @Part("username") firstName:RequestBody ,
                                       @Part("school_type") schooltype:RequestBody ,
                                       @Part("user_id") userId:RequestBody ,
                                       @Part("language_version") languageversion:RequestBody ,
                                       @Part("api_version") apiversion:RequestBody ,
                                       @Part("user_prefered_language_id") userpreferlanguageid:RequestBody ,
                                       @Part("student_name") studentname:RequestBody,
                                       @Part("medium_of_instruction") mediuminst:RequestBody,
                                       @Part image:MultipartBody.Part
    ):Call<StudentResponselDataModel>



    @POST("student_full_details")
    public fun getStatusForVersion(@Body params:HashMap<String,String>):Call<DashboardResModel>
    @GET("getApiVersion")
    public fun getApiVersion():Call<DashboardResModel>


    @Multipart
    @POST("update_user_details")
    public fun updateexistchilddetail(@Part("build_version")build_Version:RequestBody,
                                       @Part("partner_source") partner_source:RequestBody ,
                                       @Part("school_name") schoolName:RequestBody ,
                                       @Part("email") emailId:RequestBody ,
                                       @Part("board_type") boardType:RequestBody ,
                                       @Part("gender") gender:RequestBody ,
                                       @Part("regitration_source") registersource:RequestBody ,
                                       @Part("share_type") sharetype:RequestBody ,
                                       @Part("device_token") deviceToken:RequestBody ,
                                       @Part("email_verified") emailverify:RequestBody ,
                                       @Part("share_identity") shareidentity:RequestBody ,
                                       @Part("user_partner_id") userpartnerid:RequestBody ,
                                       @Part("ip_address") ipaddress:RequestBody ,
                                       @Part("mobile_version") mobileversion:RequestBody ,
                                       @Part("mobile_model") mobilemode:RequestBody ,
                                       @Part("private_tution_type") privatetution:RequestBody ,
                                       @Part("is_private_tution") isprivatetution:RequestBody ,
                                       @Part("latitude") latitude:RequestBody ,
                                       @Part("longitude") longitude:RequestBody ,
                                       @Part("language") language:RequestBody ,
                                       @Part("mobile_manufacturer") mobilemanufacture:RequestBody ,
                                       @Part("state_id") stateCode:RequestBody ,
                                       @Part("district_id") distcitcode:RequestBody ,
                                       @Part("username") firstName:RequestBody ,
                                       @Part("school_type") schooltype:RequestBody ,
                                       @Part("user_id") userId:RequestBody ,
                                       @Part("language_version") languageversion:RequestBody ,
                                      @Part("medium_of_instruction") mediuminst:RequestBody ,
                                       @Part("api_version") apiversion:RequestBody ,
                                       @Part("user_prefered_language_id") userpreferlanguageid:RequestBody ,
                                       @Part("student_name") studentname:RequestBody,
                                       @Part image:MultipartBody.Part
    ):Call<StudentResponselDataModel>

    @Headers("Secret-Id: 2198f0011288666d3694ccf4e7d16c29",
        "Secret-Key: f7115915ae4efc1bdab7ae9fc686348848f8cc2e7bf4a9",
        "Lang-Code: English")
    @Multipart
    @POST(URLConstant.TEACHER_PROFILE_UPDATE_API)
    fun updateTeacherProfileApi(
        @Part(AppConstant.DashBoardParams.USER_ID) userId:RequestBody ,
                                      @Part(AppConstant.TeacherProfileParams.TEACHER_NAME) teacherName:RequestBody ,
                                      @Part(AppConstant.TeacherProfileParams.USER_NAME) userName:RequestBody ,
                                      @Part(AppConstant.MOBILE_NUMBER) mobileNumber:RequestBody ,
                                      @Part(AppConstant.TEACHER_EMAIL_ID) emaiId:RequestBody ,
                                      @Part(AppConstant.GENDER) gender:RequestBody ,
                                      @Part(AppConstant.COUNTRY_ID) contryId:RequestBody ,
                                      @Part(AppConstant.TeacherProfileParams.STATE_ID) stateId:RequestBody ,
                                      @Part(AppConstant.TeacherProfileParams.DISTRICT_ID) districtId:RequestBody ,
                                      @Part(AppConstant.SCHOOL_ID) schoolid:RequestBody ,
                                      @Part(AppConstant.DashBoardParams.PREFERED_LANGUAGE) prefrence:RequestBody ,
                                      @Part(AppConstant.DashBoardParams.DEVICE_TOKEN) deviceToken:RequestBody ,
                                      @Part(AppConstant.DashBoardParams.DATE_OF_BIRTH) dateofBirth:RequestBody,
                                      @Part image:MultipartBody.Part
    ):Call<StudentResponselDataModel>




    @Multipart
    @POST("update_student_photo")
    public fun update_student_photo(
                                      @Part("user_id") userId:RequestBody ,
                                      @Part("user_prefered_language_id") userpreferlanguageid:RequestBody ,
                                      @Part image:MultipartBody.Part
    ):Call<StudentResponselDataModel>

    @Multipart
    @POST("update_teacher_photo")
    public fun update_teacher_photo(
        @Part("user_id") userId:RequestBody ,
        @Part("user_prefered_language_id") userpreferlanguageid:RequestBody ,
        @Part image:MultipartBody.Part
    ):Call<MyProfileResModel>



    @Multipart
    @POST("update_parent")
    public fun updateparentdetail(@Part("user_id")user_id:RequestBody,
                                @Part("full_name") full_name:RequestBody ,
                                @Part("state_id") state_id:RequestBody ,
                                @Part("district_id") district_id:RequestBody ,
                                @Part("gender") gender:RequestBody ,
                                  @Part("user_prefered_language_id") langid:RequestBody ,
                                @Part("email_id") email_id:RequestBody,
                                @Part image:MultipartBody.Part
    ):Call<StudentResponselDataModel>


    @Multipart
    @POST("update_parent_photo")
    public fun update_parent_photo(@Part("user_id")user_id:RequestBody,
                                   @Part("user_prefered_language_id") langid:RequestBody ,
                                  @Part image:MultipartBody.Part
    ):Call<StudentResponselDataModel>


    @POST("compare_documents")
    public fun getCompareDoc(@Body params:HashMap<String,String>):Call<StudentResponselDataModel>



  companion object
  {
    operator fun invoke():RemoteApi
    {
      return Retrofit.Builder()
        .baseUrl(URLConstant.BASE_URL)

        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RemoteApi::class.java)
    }
  }
}