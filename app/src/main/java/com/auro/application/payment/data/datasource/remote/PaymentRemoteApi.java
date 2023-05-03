package com.auro.application.payment.data.datasource.remote;

import com.auro.application.core.common.AppConstant;
import com.auro.application.core.network.URLConstant;
import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PaymentRemoteApi {


    @POST(URLConstant.DASHBOARD_SDK_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> getDashboardData(@FieldMap Map<String, String> params);

    @POST(URLConstant.PAYTM_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> paytmWithdrawalApi(@FieldMap Map<String, Object> params);

    @POST(URLConstant.PAYTM_UPI_TRANSFER)
    @FormUrlEncoded
    Single<Response<JsonObject>> paytmUpiTransferApi(@FieldMap Map<String, Object> params);

    @POST(URLConstant.NEW_PAYMENT_API)
    @FormUrlEncoded
    Single<Response<JsonObject>> paytmAccountTransferApi(@FieldMap Map<String, Object> params);



}
