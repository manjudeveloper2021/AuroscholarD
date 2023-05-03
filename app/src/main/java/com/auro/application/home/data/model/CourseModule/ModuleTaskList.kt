package com.auro.application.home.data.model.CourseModule

data class ModuleTaskList(var message:String?=null,var status:String?=null,var error:String?=null,var result:List<ModuleTaskData>?=null)
class ModuleTaskData(var ID:String?=null,var Name:String?=null,var IsLocked:Int?=null,var Status:Int?=null,var Description:String?=null)