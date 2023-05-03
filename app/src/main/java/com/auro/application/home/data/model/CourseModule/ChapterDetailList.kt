package com.auro.application.home.data.model.CourseModule

data class ChapterDetailList(var message:String?=null,var status:String?=null,var error:String?=null,var result:List<ChapterData>?=null)
class ChapterData(var PageID:String?=null,var PageTitle:String?=null,var PageText:String?=null,var Youtube:String?=null,var FolderPath:String?=null)