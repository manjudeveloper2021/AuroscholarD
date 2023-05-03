package com.auro.application.home.data.model.CourseModule

data class ModuleChapterList(var message:String?=null,var status:String?=null,var error:String?=null,var result:List<ModuleChapterData>?=null)
class ModuleChapterData(var ChapterID:String?=null,var ChapterTitle:String?=null,var Hours:Int?=null,var Minutes:Int?=null,var Status:Int?=null,var IsLocked:Int?=null,var Type:Int?=null)