package com.auro.application.core.common;

import android.content.Context;
import android.net.ConnectivityManager;

import com.auro.application.util.AppLogger;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetworkUtil {

    public static Single<Boolean> hasInternetConnection() {

        return Single.fromCallable(() -> {
            Socket socket = new Socket();
            try {
                int timeoutMs = 3000;
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, timeoutMs);
                return true;
            } catch (Exception e) {
                socket.close();
                return false;
            } finally {
                socket.close();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }



    public static boolean hasInternet(Context context) {
        boolean flag = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                flag = true;
            }
        } catch (Exception e) {
            AppLogger.e("NetworkUtil.class", e.toString());
        }

        return flag;
    }



}
