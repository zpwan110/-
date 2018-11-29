package com.crowd.curtain.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import base.BaseActivity;
import base.BroadcastConstants;
import base.PermissionActivity;
import base.http.Callback;
import base.http.ErrorModel;
import base.util.BroadCastReceiverUtil;
import base.util.LogUtil;
import base.util.ToastUtil;
import com.crowd.curtain.R;
import com.crowd.curtain.api.ApplicationApi;
import com.crowd.curtain.ui.activity.LoginActivity;
import com.crowd.curtain.ui.activity.MainActivity;
import com.crowd.curtain.utils.AppSharePerference;

import static base.util.BroadCastReceiverUtil.registerBroadcastReceiver;
import static base.util.BroadCastReceiverUtil.unRegisterBroadcastReceiver;

/**
 *
 * @author zpwan110
 * @date 2017/3/20
 */

public class AppBaseActivity extends BaseActivity implements BroadCastReceiverUtil.OnReceiveBroadcast{
    protected Context mContext;
    private String TAG = getClass().getName();
    private FragmentManager fragmentManager;
    private BroadcastReceiver mBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mBroadcast = registerBroadcastReceiver(mContext,new String[]{BroadcastConstants.BROADCAST_REFRESH_TOKEN_TIME_OUT}, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterBroadcastReceiver(mContext,mBroadcast);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            return;
        }
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        if (BroadcastConstants.BROADCAST_REFRESH_TOKEN_TIME_OUT.equals(action)) {
            // 重新登录
            toActivity(LoginActivity.newIntent());

        }
    }
    public void takePicForCamera(){
        PermissionActivity.requestPermission((Activity) activity, new String[]{Manifest.permission.CAMERA}, "需要授权使用摄像头", new PermissionActivity.OnPermissionCallback() {
            @Override
            public void onPermissionAuthenticated() {
                File file = new File(getExternalCacheDir(), "img.jpg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(mContext,"curtain.photograph.com.fileprovider", file));
                }else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }
                startActivityForResult(intent, MainActivity.CAMERA_REQUEST_CODE);
            }
            @Override
            public void onPermissionDenied() {
                ToastUtil.showToast("授权失败");
            }
        });
    }

    public void takePicForAlbum(){
        PermissionActivity.requestPermission((Activity) activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, "需要授权读写权限", new PermissionActivity.OnPermissionCallback() {
            @Override
            public void onPermissionAuthenticated() {
                Matisse.from(AppBaseActivity.this)
                        .choose(MimeType.ofImage())
                        .theme(R.style.Matisse_Dracula)
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(
                                getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(MainActivity.IMAGE_REQUEST_CODE);
            }
            @Override
            public void onPermissionDenied() {
                ToastUtil.showToast("授权失败");
            }
        });
    }
    /**
     * 手动生成设备号
     * @return
     */
    @SuppressLint("MissingPermission")
    public void initDeviceCode() {
        String deviceId="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
            String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String m_szLongID = getUniqueId()
                    + m_szAndroidID + m_szWLANMAC + getBluetoothId();
            MessageDigest m = null;
            try {
                m = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
            // get md5 bytes
            byte p_md5Data[] = m.digest();
            String m_szUniqueID = new String();
            for (int i = 0; i < p_md5Data.length; i++) {
                int b = (0xFF & p_md5Data[i]);
                if (b <= 0xF) {
                    m_szUniqueID += "0";
                }
                m_szUniqueID += Integer.toHexString(b);
            }
            deviceId = m_szUniqueID.toUpperCase();
        }else{
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
        }
        AppSharePerference.saveDeviceId(deviceId);
        if(!TextUtils.isEmpty(deviceId)){
            ApplicationApi.postAppData(AppSharePerference.getDeviceId(), new Callback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject jsonObject) {

                }

                @Override
                public void onFail(ErrorModel httpError) {

                }
            });
        }
    }
    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            LogUtil.d("本软件的版本名：" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
    private String getBluetoothId() {
        BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(m_BluetoothAdapter==null){
            return "";
        }
        String m_szBTMAC = m_BluetoothAdapter.getAddress();
        return m_szBTMAC;
    }

    private String getUniqueId() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
        return m_szDevIDShort;
    }
}
