package com.crowd.curtain.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangpeng on 2018/3/7.
 */

public class UserEntity implements Parcelable{
//    "username": "test", # 用户名
//        "name": "app开发人员测试账号", # 姓名
//        "phone": "13313313383", # 联系电话
//        "sex": "男" # 性别

    public String username;
    public String name;
    public String phone;
    public String sex;
    public String address;

    public UserEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // 写数据进行保存
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(username);
        out.writeString(name);
        out.writeString(phone);
        out.writeString(sex);
        out.writeString(address);
    }

    // 用来创建自定义的Parcelable的对象
    public static final Parcelable.Creator<UserEntity> CREATOR
            = new Parcelable.Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    // 读数据进行恢复
    private UserEntity(Parcel in) {
        username = in.readString();
        name = in.readString();
        phone = in.readString();
        sex = in.readString();
        address = in.readString();
    }
}
