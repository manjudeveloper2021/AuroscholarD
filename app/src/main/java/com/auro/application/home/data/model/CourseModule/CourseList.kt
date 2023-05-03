package com.auro.application.home.data.model.CourseModule

data class CourseList(var message:String?=null,var status:String?=null,var error:String?=null,var result:List<CourseData>?=null)
class CourseData(var CCCourseID:String?=null,var CourseTitle:String?=null,var ModuleCount:Int?=null,var Hours:Int?=null,var Minutes:Int?=null,var Progress:Int?=null,var Status:Int?=null)