package com.auro.application.core.common;

public enum Status {

    LOADING,
    SUCCESS,
    FAIL,
    AUTH_FAIL,
    FAIL_400,
    FAIL_DB,
   TASK_CLICK,
    ON_SHOW_DATA,
    ON_SHOW_ERROR,
    ON_SHOW_NO_INTERNET,
    ON_SHOW_SHIMMER,
    NEXT_QUIZ_CLICK,
    KYC_RESULT_PATH,
    MESSAGE_SELECT_CLICK,
    SEND_INVITE_CLICK,
    REQUEST_ACCEPT,
    REQUEST_DECLINE,
    KYC_BUTTON_CLICK,

    STATE,
    GENDER,
    SENDINVITEBUDDY,
    SUBJECT,
    OK,
    HIDE_KEYBOARD,
    NO_INTERNET,
    DEFAULT_MSG,

    //SignUp MODULE
    INVALID_OTP,
    INVALID_PIN,
    UPLOAD_PROFILE_IMAGE,
    UPLOAD_DOC_CALLBACK,
    UPLOAD_TEACHER_DOC_CALLBACK,
    AGREE_TERMS_CONDITION,
    INVALID_PASSWORD,
    MOPBILE_VERIFY,
    COUNTRY_PICKER,
    OTP_VERIFY,
    RESEND_OTP,
    SET_PIN,
    RESET_PIN,
    FORGOT_PIN_RESEND_OTP,
    FORGOT_PIN_OTP_VERIFY,
    GET_ZOHO_APPOINTMENT,
    FORGOT_CLICK,
    SEND_INVITE_API,
    DEMOGRAPHIC_API,
    ASSIGNMENT_STUDENT_DATA_API,
    UPDATE_TEACHER_PROFILE_API,
    GET_PROFILE_TEACHER_API,
    START_QUIZ_BUTON,

    VERIFY_PIN,
    NEXT_CLICK,
    BACK_CLICK,
    RESEND_CLICK,
    DASHBOARD_API,
    ACCEPT_INVITE_REQUEST,
    FRIENDS_REQUEST_LIST,
    AZURE_API,
    INVITE_FRIENDS_LIST,
    ACCEPT_INVITE_CLICK,


    //Profile
    COUNTRY_NATIONALITY,
    EDIT_PROFILE,
    VIEW_PROFILE_API,
    UPDATE_PROFILE_API,
    GENDER_ERROR,
    ERROR_MOBILE_ZERO,
    ERROR_IMAGE,
    ERROR_FNAME,
    ERROR_LNAME,
    ERROR_CITY,
    FLAG_CLICK,
    ERROR_MOBILE,
    EMPTY_EMAIL,
    DOB_ERROR,
    ERROR_AREA,
    ERROR_NATIONALITY,
    ERROR_COUNTRY,
    CANCEL_CLICK,
    UPDATE_CLICK,
    MODIFY_CLICK,
    DOB_CLICK,
    CLICK_COUNTRY,
    CLICK_NATIONALITY,
    CLICK_GENDER,
    CLICK_CHANGE_EMAIL,
    CLICK_CHANGE_PROFILE,
    CLICK_CHANGE_MOBILE_NUM,
    EROR_IMAGESIZE,

    //Faq
    CLICK_SEARCH,
    CLICK_CANCEL,
    CLICK_BACK,
    CLICK_OPENPROFILEBACK,
    CLICK_PARENTPROFILE,
    CLICK_CHILDPROFILE,
    FAQ_CATEGORY,
    CATEGORY_QUESTION,


    //WriteUs
    CLICK_CATEGORY,
    CLICK_SUBMIT,

    //home toolbar
    BASKET,
    NOTIFICATION,
    HUMBURGER,

    //WriteUs
    ERROR_NAME,
    ERROR_CATEGORY,
    ERROR_DESCRIPTION,
    GRADE_UPGRADE,


    //Dashbord Module
    SEARCH,
    //Dine adapter click status
    ITEM_CLICK,
    COURSE_CLICK,
    VIEW_CLICK,
    VIDEO_CLICK,
    CHAPTER_CLICK,
    MODULE_CLICK,
    MONTH_CLICK,
    SLOT_CLICK,
    NUMBER_OF_PEOPLE_CLICK,
    BOOK_TABLE_AVAILBILITY_API,
    BOOK_TABLE_RESERVE_API,
    MODIFY_BOOK_TABLE,
    CANCEL_TABLE_API,
    OUTLET_LIST,
    PRESS_RELEASE,
    COURSE_LIST,
    CALL_BUTTON_CLICK,
    DIRECTION_BUTTON_CLICK,
    SEARCH_TEXT,
    SEARCH_CLOSE,
    SEARCH_OPEN,
    IMAGE_URL,
    IMAGE_360_URL,
    SEND_OTP,
    VERIFY_OTP,
    VERSIONAPI,
    CHECKVALIDUSER,
    CHANGE_GRADE,
    SEND_FRIENDS_REQUEST,
    PAYTM_UPI_WITHDRAWAL,
    PAYTM_ACCOUNT_WITHDRAWAL,
    PAYTM_WITHDRAWAL,
    FIND_FRIEND_DATA,
    TEACHER_KYC_API,
    GET_TEACHER_DASHBOARD_API,
    GET_TEACHER_PROGRESS_API,
    DOCUMENT_CLICK,
    DOWNLOAD_CLICK,
    WEBINAR_CLICK,
    NAV_CHANGE_GRADE_CLICK,
    BOOK_TUTOR_SESSION_CLICK,
    FACE_DETECTION_CALLBACK,
    STATE_LIST_ARRAY,
    DISTRICT_LIST_DATA,
    SUBJECT_CLICK,
    CLASS_CLICK,
    SEND_MESSAGE_CLICK,
    FRIEND_LEADER_BOARD_CLICK,
    DYNAMIC_LINK_API,
    SEND_REFERRAL_API,
    CERTIFICATE_API,
    ITEM_LONG_CLICK,
    LISTNER_SUCCESS,
    LISTNER_FAIL,
    PAYMENT_TRANSFER_API,
    UPLOAD_EXAM_FACE_API,
    NAME_DONE_CLICK,
    UPDATE_STUDENT,
    UPDATE_PARENT,
    SCREEN_TOUCH,
    PASSPORT_API,
    FACE_DETECT,
    CAMERA_BITMAP_CALLBACK,
    DIALER_CALL_BACK_LISTNER,
    CLEAR_EDIT_TEXT,
    PRIVACY_POLICY_TEXT,
    TERMS_CONDITION_TEXT,
    SUBJECT_CLICKED,
    REFFER_API_SUCCESS,
    REFFER_API_ERROR,
    PARTNERS_API,
    PARTNERS_LOGIN_API,
    PARTNERS_CLICK,
    ASK_DIALOG_CLICK,
    ACCEPT_PARENT_BUTTON ,
    PARTNER_EXPLORE,
    AVAILABLE_WEBINAR_SLOTS,
    BOOKED_WEBINAR_SLOTS,
    ADD_GROUP,
    ADD_STUDENT_IN_GROUP,
    DELETE_STUDENT_FROM_GROUP,
    ONCLICKFORTIMESLOT,
    TEACHER_DASBOARD_SUMMARY,
    TEACHER_CLASSROOM,
    TEACHER_STUDENTPASSPORT,
    GROUP_CLICK_CALLBACK,
    TEACHER_BOOKED_SLOT_API,
    TEACHER_AVAILABLE_SLOTS_API,
    STATE_WISE,
    DISTRICT,
    SCHOOL,
    TUTIONTYPE,
    TUTION,
    SCHHOLMEDIUM,
    SCHHOLTYPE,
    BOARD,
    GRADE,
    GRADEID,
    SCHOOL_ID,
    ADD_STUDENT,
    REMOVE_STUDENT,
    TEACHER_CREATE_GROUP_API,
    TEACHER_ADD_STUDENT_IN_GROUP_API,
    BOOK_SLOT_API,
    BOOK_SLOT_CLICK,
    REGISTER_CALLBACK,
    SET_PASSWORD,
    LOGIN_API,
    CHANGE_NUMBER,
    SEND_OLD_OTP_API,
    VERIFY_OTP_OLD,
    FETCH_STUDENT_PREFERENCES_API,
    SUBJECT_PREFRENCE_LIST_API,
    UPDATE_PREFERENCE_API,
    REMOVE_ITEM,
    EXIT_DIALOG_CLICK,
    OPTION_SELECT_API,
    NO_INTERNET_BROADCAST,
    FINISH_DIALOG_CLICK,
    FETCH_QUIZ_DATA_API,
    SUBMITQUIZ,
    SAVE_QUIZ_DATA_API,
    FINISH_QUIZ_API,
    OPTION_IMAGE_CLICK,
    LANGUAGE_LIST,
    GET_USER_PROFILE_DATA,
    REGISTER_API,
    SET_USERNAME_PIN_API,
    LOGIN_PIN_API,
    SET_USER_PIN,
    BOOK_WEBINAR_SLOT,
    CANCEL_WEBINAR_SLOT,
    TEACHER_KYC_STATUS_API,
    WALLET_STATUS_API,
    DYNAMIC_LANGUAGE,
    REFFERAL_API,
    STUDENT_KYC_STATUS_API,
    OTP_OVER_CALL,
    GET_INSTRUCTIONS_API,
    ACCEPT_INSTRUCTION_CALLBACK,
    GET_SLABS_API,
    NOTICE_INSTRUCTION,
    GET_MESSAGE_POP_UP,
    PENDING_KYC_DOCS,
    LOGIN_DISCLAMER_DIALOG,
    NOTICE_DIALOG,
    GRADE_CHANGE_DIALOG
}
