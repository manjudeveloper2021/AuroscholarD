package com.auro.application.home.data.model.CourseModule

data class GenerateLinkList(var message:String?=null,var status:String?=null,var error:String?=null,var result:List<GenerateLinkData>?=null)
class GenerateLinkData(var TaskID:String?=null,var Link:String?=null,var LinkGenerationDate:String?=null,var LinkExpiry:String?=null,var userID:String?=null)