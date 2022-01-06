package com.lenovo.frame.net;

import com.lenovo.frame.net.base.BaseModel;
import com.lenovo.frame.entity.User;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

/**
 * @author feizai
 * @date 12/21/2020 021 10:02:37 PM
 */
public interface RequestAPI {
    public final static String BASE_URL="http://172.21.58.56:8080/Parking_war/";
    public final static String BASE_IMAGE_URL="http://172.21.58.56:8080/";

    @GET("api/user/selectAllUser")
    Observable<BaseModel<User>> selectAllUser();

}
