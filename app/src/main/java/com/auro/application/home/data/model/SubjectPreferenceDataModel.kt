package com.auro.application.home.data.model

data class SubjectPreferenceDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var subjects:List<SubjectPrefModel>?=null)

class SubjectPrefModel(var id:Int?=null,var subjectname:String?=null,var subject:String?=null,
                         var subject_code:String?=null)