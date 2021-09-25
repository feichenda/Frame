package com.lenovo.frame.util;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feizai
 * @date 12/22/2020 022 5:10:08 PM
 */
public class SelectPhotoUtil {

    public static void shoot(Activity activity,OnResultCallbackListener<LocalMedia> onResultCallbackListener){
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .compress(true)
                .minimumCompressSize(2048)
                .forResult(onResultCallbackListener);
    }

    public static void select(Activity activity,int maxNum,OnResultCallbackListener<LocalMedia> onResultCallbackListener){
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofAll())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.MULTIPLE)//设置为多选模式
                .maxSelectNum(maxNum<=0?1:maxNum)//设置最大选择图片数
                .isWeChatStyle(true)//开启微信样式
                .isCamera(false)
                .isAutomaticTitleRecyclerTop(true)
                .forResult(onResultCallbackListener);
    }

}
