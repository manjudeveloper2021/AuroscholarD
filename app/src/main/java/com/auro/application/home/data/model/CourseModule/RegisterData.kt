package com.auro.application.home.data.model.CourseModule

data class RegisterData(var message:String?=null,var status:String?=null,var response:List<RegisterDetail>?=null)
class RegisterDetail(var outStatus:String?=null,var outMessage:String?=null,var partnerSource:String?=null,var languageName:String?=null,var lmsUserID:Int?=null,var roleName:String?=null)