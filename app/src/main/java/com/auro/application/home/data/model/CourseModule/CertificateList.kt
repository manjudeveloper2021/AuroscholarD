package com.auro.application.home.data.model.CourseModule

data class CertificateList(var message:String?=null,var status:String?=null,var error:String?=null,var result:List<CertificateData>?=null)
class CertificateData(var ID:String?=null,var Name:String?=null,var CertificatePath:String?=null)