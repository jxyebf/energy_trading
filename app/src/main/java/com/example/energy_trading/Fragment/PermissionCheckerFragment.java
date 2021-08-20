package com.example.energy_trading.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.energy_trading.Util.callback.CallbackManager;
import com.example.energy_trading.Util.callback.CallbackType;
import com.example.energy_trading.Util.callback.IGlobalCallback;
import com.example.energy_trading.ui.camera.CameraImageBean;
import com.example.energy_trading.ui.camera.Energy_trade_Camera;
import com.example.energy_trading.ui.camera.RequestCodes;
import com.yalantis.ucrop.UCrop;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/*
权限Fragment   处理和权限有关的设定，例如：拍照、图片剪裁、二维码扫描等全限
 */

@RuntimePermissions
public abstract class PermissionCheckerFragment extends BaseFragment {
    //不是直接调用方法
    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE})
    void startCamera() {
        Energy_trade_Camera.start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.TAKE_PHOTO:
                    final Uri resultUri = CameraImageBean.getInstance().getPath();
                    UCrop.of(resultUri, resultUri)
                            .withMaxResultSize(400, 400)
                            .start(getContext(), this);
                    break;
                case RequestCodes.PICK_PHOTO:
                    if (data != null) {
                        final Uri pickPath = data.getData();
                        //从相册选择后需要有个路径存放裁剪过的图片
                        final String pickCropPath = Energy_trade_Camera.createCropFile().getPath();
                        UCrop.of(pickPath, Uri.parse(pickCropPath))
                                .withMaxResultSize(400, 400)
                                .start(getContext(), this);
                    }
                    break;
                case RequestCodes.CROP_PHOTO:
                    final Uri cropUri = UCrop.getOutput(data);
                    //拿到裁剪后的数据进行处理
                    final IGlobalCallback<Uri> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_CROP);
                    if (callback != null) {
                        callback.executeCallback(cropUri);
                    }
                    break;
                case RequestCodes.CROP_ERROR:
                    Toast.makeText(getContext(), "剪裁出错", Toast.LENGTH_SHORT).show();
                    break;
                case RequestCodes.SCAN:
                    break;
            }
        }
    }
}
