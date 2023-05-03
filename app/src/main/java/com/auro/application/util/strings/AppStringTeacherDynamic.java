package com.auro.application.util.strings;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.AcceptBuddyLayoutBinding;
import com.auro.application.databinding.ActivityAddteacherbuddyBinding;
import com.auro.application.databinding.ActivityAppLanguageBinding;
import com.auro.application.databinding.FragmentClassRoomGroupBinding;
import com.auro.application.databinding.FragmentCreateGroupBinding;
import com.auro.application.databinding.FragmentInformationDashboardBinding;
import com.auro.application.databinding.FragmentInviteteacherbuddyBinding;
import com.auro.application.databinding.FragmentKycInfoScreenBinding;
import com.auro.application.databinding.FragmentTeacherNewprofileBinding;
import com.auro.application.databinding.FragmentTeacherpassportBinding;
import com.auro.application.databinding.FragmentUpCommingBookBinding;
import com.auro.application.databinding.MoreTeacherLayoutBinding;
import com.auro.application.databinding.NewstudentdetailPassportTeacherLayoutBinding;
import com.auro.application.databinding.ReceiveBuddyLayoutBinding;
import com.auro.application.databinding.SentBuddyLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.util.AppLogger;

public enum AppStringTeacherDynamic {

    INSTANCE;

    static String TAG = "AppStringDynamic";

    /*TeacherKycInfoFragment  Strings*/
    public static void setTeacherKycInfoFragmentStrings(FragmentKycInfoScreenBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPKycInformation.setText(details.getKyc_information());
                binding.RPreupload.setText(details.getReUploadNdocuments());
                binding.RpKycVerification.setText(details.getUploadVerification());
                binding.onceitsDone.setText(details.getOnceItSDone());
                binding.RPUploadDocument.setText(details.getCompleteKyc());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }




    /*TeacherUserProfileStrings*/
    public static void setTeacherUserProfileStrings(FragmentTeacherNewprofileBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.rpTeacherprofile.setText(details.getTeacherProfile());
                binding.tiFullName.setHint(details.getTeacherFullName());
                binding.tlPhoneNumber.setHint(details.getTeacherPhoneNumber());
                binding.tlEmail.setHint(details.getEmailId());
                binding.tlSchool.setHint(details.getSchoolName());
                binding.tlGender.setHint(details.getGender());
                binding.tlState.setHint(details.getState());
                binding.tlDistict.setHint(details.getDistrict());
                binding.button.setText(details.getSaveChanges());
            }
        } catch (Exception e) {
            AppLogger.e(TAG,     e.getMessage());
        }
    }

    public static void setTeacherAddBuddyStrings(ActivityAddteacherbuddyBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {

            }
        } catch (Exception e) {
            AppLogger.e(TAG,     e.getMessage());
        }
    }


    /*UpCommingBook Strings*/
    public static void setUpComingBookFragmentStrings(FragmentUpCommingBookBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.bookedTab.setText(details.getChooseSlot());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public static void setInviteTeacherBuddyFragmentStrings(FragmentInviteteacherbuddyBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.txtbuddyinvite.setText(details.getTeacher_buddy() != null   ? details.getTeacher_buddy() : "Teacher Buddies");
                binding.txtbuddyaddinvite.setText(details.getNo_buddy_add() != null   ? details.getNo_buddy_add() : "No Buddies Added Yet");
                binding.txtbuddysloganinvite.setText(details.getTxt_buddy_add() != null   ? details.getTxt_buddy_add() : "Now you can add those buddies who\n are already users of Auroscholar App.");
                binding.btnaddbuddyinvite.setText(details.getAdd_buddy() != null   ? details.getAdd_buddy() : "Add Buddies");
                binding.txtbuddylink.setText(details.getShare_buddy_link() != null   ? details.getShare_buddy_link() : "Add buddies via link sharing");
                binding.btnshare.setText(details.getShare_buddy() != null   ? details.getShare_buddy() : "Share");
                binding.txtbuddy.setText(details.getAdd_buddy() != null   ? details.getAdd_buddy() : "Add Buddies");

                binding.txtbuddyadd.setText(details.getSearch_auro_teacher() != null   ? details.getSearch_auro_teacher() : "Search Auro Teacher");

                binding.txtrefresh.setText(details.getSearch_auro_teacher() != null   ? details.getRefresh() : "Refresh");

                binding.txtbuddyslogan.setText(details.getAuro_teacher() != null   ? details.getAuro_teacher() : "Auro Teacher");

                binding.btnaddbuddy.setText(details.getSend_invite() != null   ? details.getSend_invite() : "Send Invitation");
                binding.txtselectall.setText(details.getSelect_all() != null   ? details.getSelect_all() : "Select All");






            }

        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public static void setReceiveeacherBuddyFragmentStrings(ReceiveBuddyLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {

                binding.txtrefresh.setText(details.getRefresh() != null   ? details.getRefresh() : "Refresh");








            }

        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }
    public static void setSenteacherBuddyFragmentStrings(SentBuddyLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {

                binding.txtrefresh.setText(details.getRefresh() != null   ? details.getRefresh() : "Refresh");

            }

        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public static void setAcceptTeacherBuddyFragmentStrings(AcceptBuddyLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {

                binding.btnaddbuddyinvite.setText(details.getAdd_new_buddies() != null   ? details.getTeacher_buddy() : "Add New Buddies");
                binding.txtrefresh.setText(details.getRefresh() != null   ? details.getRefresh() : "Refresh");

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*MyClassRoomGroupFragment*/
    public static void setMyClassRoomGroupFragmentStrings(FragmentClassRoomGroupBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPmyClassRoom.setText(details.getAuroMyClassroom());
                binding.rpStudentList.setText(details.getAuroStudentList());
                binding.RpRecent.setText(details.getRecent());
                binding.RpGroup.setText(details.getGroup());
                binding.rpChooseStudent.setText(details.getChooseStudent());

            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public static void setMyStudentPassportFragmentStrings(FragmentTeacherpassportBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPmyClassRoom.setText(details.getStudentPassport() != null   ? details.getStudentPassport() : "Student Passport");

                binding.etgrade.setText(details.getGradeStudent() != null   ? details.getGradeStudent() : "Grade");
                binding.studentListMessage.setText(details.getNo_data_found() != null   ? details.getNo_data_found() : "No Data Found");




            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public static void setTeacherStudentPassportDetailFragmentStrings(NewstudentdetailPassportTeacherLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {


                binding.txtkycstatus.setText(details.getKyc_status() != null   ? details.getKyc_status() : "KYC Status : ");
                binding.etmonth.setText(details.getChoose_month() != null   ? details.getChoose_month() : "Month");
                binding.etsubject.setText(details.getAuroSubject() != null   ? details.getAuroSubject() : "Subject");
                binding.tvScholarship.setText(details.getTotal_scholar_disburse() != null   ? details.getTotal_scholar_disburse() : "Total Scholarship Disbursal");
                binding.tvquiz.setText(details.getTotal_quiz_attempt() != null   ? details.getTotal_quiz_attempt() : "Total Quiz Attempted");



            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    public static void setTeacherMoreFragmentStrings(MoreTeacherLayoutBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {


                binding.txtkycstatus.setText(details.getKyc_status() != null   ? details.getKyc_status() : "KYC Status : ");
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }


    /*MyInformation*/
    public static void setMyInformationStrings(FragmentInformationDashboardBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPinformation.setText(details.getInformation());
                binding.RPDetailInformation.setText(details.getAuroGrabThisOpportunity());
                binding.RpDahboardtitle.setText(details.getDashboard());
                binding.RpTotalStudent.setText(details.getTotalStudentsInClass());
                binding.Rpscholar.setText(details.getWinningQuiz());
                binding.testReport.setText(details.getTestReports());
                binding.RptotalMMarks.setText(details.getTotalTestTaken());
                binding.RptotalMMarksobtain.setText(details.getTestTakenByUniqueStudents());


            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }

    /*MyInformation*/
    public static void setCreateGroupStrings(FragmentCreateGroupBinding binding) {
        try {
            LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
            Details details = model.getDetails();
            if (model != null) {
                binding.RPCreateGroup.setText(details.getCreateGroup());
                //binding.RPDetailInformation.setText("Grab This opportunity To Become Auro Teacher");
                binding.rpChooseStudent.setText(details.getAdded());
                binding.addedListMessage.setText(details.getNoStudent());
                binding.rpStudentList.setText(details.getStudentsList());
            }
        } catch (Exception e) {
            AppLogger.e(TAG, e.getMessage());
        }
    }



}
