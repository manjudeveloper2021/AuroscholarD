package com.auro.application.teacher.data.model

data class StudentListDataModel(var status:String?=null,var error:String?=null,var total_student_list:List<StudentData>?=null)

class StudentData(var student_id:String?=null,var student_name:String?=null,var profile_pic:String?=null,var total_score:String?=null,var group_id:String?=null)