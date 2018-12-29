package com.crowd.curtain.common.model;

/**
 * Created by zhangpeng on 2018/3/1.
 */

public class CateEntity {
/**
 "id": "3",
 "sort": "7",
 "name": "儿童",
 "desc": "儿童",
 "parent_id": "1",
 "active_icon": "20180707/1214339654aa2b29aa8913294e6b36e4.png",
 "inactive_icon": "20180707/84725e8a5155b5185e5aa05d740be6ca.png",
 "created_at": "1519634541",
 "updated_at": "1533890080"
 */
    public String id;
    public String sort;
    public String name;
    public String desc;
    public String parent_id;
    public String active_icon;
    public String inactive_icon;
    public boolean isSelect;


    public CateEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getActive_icon() {
        return active_icon;
    }

    public void setActive_icon(String active_icon) {
        this.active_icon = active_icon;
    }

    public String getInactive_icon() {
        return inactive_icon;
    }

    public void setInactive_icon(String inactive_icon) {
        this.inactive_icon = inactive_icon;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
