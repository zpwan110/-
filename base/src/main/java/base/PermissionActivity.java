package base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import base.util.MIUIUtils;
import jingshu.com.base.R;


/**
 * Created by HanTuo on 16/8/5.
 */
public class PermissionActivity extends AppCompatActivity {
    private static final String EXTRA_PERMISSIONS = "permission";
    private static final int PERMISSION_REQUEST_CODE = 11; // 系统权限管理页面的参数
    private static ArrayList<String> newLocationper = new ArrayList<>();
    private static String newTips="";

    public static PermissionActivity.OnPermissionCallback getOnPermissionCallback() {
        return onPermissionCallback;
    }

    public static void setOnPermissionCallback(PermissionActivity.OnPermissionCallback onPermissionCallback) {
        if(onPermissionCallback == null){
            return;
        }else {
            PermissionActivity.onPermissionCallback = onPermissionCallback;
        }
    }

    private static OnPermissionCallback onPermissionCallback;

    private String mTips;

    public interface OnPermissionCallback {
        void onPermissionAuthenticated();
        void onPermissionDenied();
    }
    private static Intent newIntent(Context context, ArrayList<String> mPermission, String mTips) {
        Intent intent = new Intent(context, PermissionActivity.class);
        String[] pers = mPermission.toArray(new String[mPermission.size()]);
        intent.putExtra("permission", pers);
        intent.putExtra("tips", mTips);
        return intent;
    }
    private static Intent newIntent(Context context, String[] mPermission, String mTips) {
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra("permission", mPermission);
        intent.putExtra("tips", mTips);
        return intent;
    }
    public static Intent newIntent(Context context,String mPermission,String mTips,OnPermissionCallback callback){
        setOnPermissionCallback(callback);
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra("permission", mPermission);
        intent.putExtra("tips", mTips);
        intent.putExtra("shouldCallback",true);
        return intent;
    }
    // 启动当前权限页面的公开接口
    public static void startActivityForResult(Activity activity,int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }
    static ArrayList<String> locationPers =
            new ArrayList<String>(Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE));
    public static void requsetPermissonsLocation(Activity activity,String tips,OnPermissionCallback callback) {
        setOnPermissionCallback(callback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermissions(activity,locationPers)) {
                getOnPermissionCallback().onPermissionAuthenticated();
                setOnPermissionCallback(null);
            } else {
                activity.startActivity(PermissionActivity.newIntent(activity, newLocationper,newTips));
            }
        } else {
            try {
                getOnPermissionCallback().onPermissionAuthenticated();
                setOnPermissionCallback(null);
            } catch (RuntimeException e) {
                activity.startActivity(PermissionActivity.newIntent(activity, locationPers,tips));
            }
        }
    }

    public static void requestPermission(Activity context, String[] permission, String tips, OnPermissionCallback callback) {
        setOnPermissionCallback(callback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkAllPermissions(context, permission)) {
                getOnPermissionCallback().onPermissionAuthenticated();
                setOnPermissionCallback(null);
            } else {
                context.startActivity(PermissionActivity.newIntent(context, permission, tips));
            }
        } else {
            try {
                getOnPermissionCallback().onPermissionAuthenticated();
                setOnPermissionCallback(null);
            } catch (RuntimeException e) {
                context.startActivity(PermissionActivity.newIntent(context, permission, tips));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if (hasAllPermissionsGranted(grantResults)) {
                onPermissionCallback.onPermissionAuthenticated();
                setOnPermissionCallback(null);
                finish();
            } else {
                showMissingPermissionDialog();
            }
        }else{
            finish();
        }
    }
    // 含有全部的权限
    private static boolean checkAllPermissions(Activity activity,String[] permissons) {
        for (String permiss : permissons) {
            int result = ContextCompat.checkSelfPermission(activity, permiss);
            if (PackageManager.PERMISSION_DENIED == result) {
                return false;
            }
        }
        return true;
    }
    // 含有全部的权限
    private static boolean checkLocationPermissions(Activity activity,ArrayList<String> permissons) {
        int percount=0;
        newLocationper.clear();
        for (int i = 0; i < permissons.size(); i++) {
            int result = ContextCompat.checkSelfPermission(activity, permissons.get(i));
            if (PackageManager.PERMISSION_DENIED == result) {
                percount++;
                newLocationper.add(permissons.get(i));
            }
        }
        if(newLocationper.contains(Manifest.permission.ACCESS_FINE_LOCATION)&&!newLocationper.contains(Manifest.permission.READ_PHONE_STATE)){
            newTips ="为了更好的评估您的信用情况，请允许获取地理位置";
        }
        if(newLocationper.contains(Manifest.permission.READ_PHONE_STATE)&&!newLocationper.contains(Manifest.permission.ACCESS_FINE_LOCATION)){
            newTips ="为了更好的评估您的信用情况，请允许获取手机信息";
        }
        if(newLocationper.contains(Manifest.permission.READ_PHONE_STATE)&&newLocationper.contains(Manifest.permission.ACCESS_FINE_LOCATION)){
            newTips ="为了更好的评估您的信用情况，请允许获取地理位置和手机信息";
        }
        return percount==0;
    }
    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionActivity.this);
        builder.setTitle(R.string.help);
        builder.setMessage(mTips);
        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                getOnPermissionCallback().onPermissionDenied();
                setOnPermissionCallback(null);
                if(MIUIUtils.isMIUI()){
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }else{//原生和其他系统
                    startActivity(getAppDetailSettingIntent());
                }
                finish();
            }
        });
        builder.setNeutralButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                getOnPermissionCallback().onPermissionDenied();
                setOnPermissionCallback(null);
                finish();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        builder.show();
    }
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] mPermissions = getIntent().getStringArrayExtra("permission");
        mTips = getIntent().getStringExtra("tips");
        ActivityCompat.requestPermissions(this, mPermissions, PERMISSION_REQUEST_CODE);
    }

}
