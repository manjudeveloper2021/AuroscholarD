package com.auro.application.home.data.model.CourseModule

data class ModuleList(var message:String?=null,var status:String?=null,var error:String?=null,var result:List<ModuleData>?=null)
class ModuleData(var CCCourseID:String?=null,var ModuleID:String?=null,var ModuleTitle:String?=null,var Description:String?=null,var ChapterCount:Int?=null,var Hours:Int?=null,var Minutes:Int?=null,var Progress:Int?=null,var Task:Int?=null,var Status:Int?=null)