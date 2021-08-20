package com.example.energy_trading.ui.camera;

import android.net.Uri;

import com.example.energy_trading.Fragment.PermissionCheckerFragment;
import com.example.energy_trading.Util.File.FileUtil;


/**
 * @author: wuchao
 * @date: 2018/1/4 23:17
 * @desciption: 照相机调用类
 */

public class Energy_trade_Camera {

    public static Uri createCropFile() {
        return Uri.parse(FileUtil.createFile("crop_image",
                FileUtil.getFileNameByTime("IMG", "jpg")).getPath());
    }

    public static void start(PermissionCheckerFragment delegate) {
        new CameraHandler(delegate).beginCameraDialog();
    }
}
