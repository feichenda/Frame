package com.lenovo.frame.net;

import com.lenovo.frame.base.BaseModel;
import com.lenovo.frame.entity.User;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author feizai
 * @date 12/21/2020 021 10:02:37 PM
 */
public interface RequsetAPI {
    public final static String baseURL="http://172.21.58.56:8080/Parking_war/";
    public final static String baseImageURL="http://172.21.58.56:8080/";

    @GET("api/user/selectAllUser")
    Observable<BaseModel<User>> selectAllUser();

}
