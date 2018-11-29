package com.crowd.curtain.common.model;

/**
 * Created by zhangpeng on 2018/3/1.
 */

public class CateEntity {
/**
 "primaryKey": 4,
 "name": "书房",
 "activeIcon": "http://imagecl.gunaimu.com/20180301/f76830f72e70c6e9d88bc6817faa36bc.png",
 "inactiveIcon": "http://imagecl.gunaimu.com/20180301/dcf7b199d86dccfe5dc7753309003185.png"
 */
    public String productCateId;
    public String name;
    public String activeIcon;
    public String inactiveIcon;
    public boolean selected =false;
    public boolean isAll =false;

    public CateEntity() {
    }

    public CateEntity(String productCateId,String name,boolean isAll) {
        this.productCateId = productCateId;
        this.name = name;
        this.isAll = isAll;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isSelected() {
        return selected;
    }
}
