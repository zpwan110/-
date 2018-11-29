package com.crowd.curtain.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangpeng on 2018/3/12.
 */

public class CaseEntity {
//"number":"12138","desc":"不嗯我GPS","image":["http://cl.gunaimu.com/thumb/20180319/e07c52b8a501fee9ca53509a1ba2e2f9.jpg""time":1521429034,"status":"待审核","reason":null
    public String buyersShowId;
    public String number;
    public String desc;
    public List<CaseImage> image;
    public long time;
    public String status;
    public String reason;//审核说明
    public class CaseImage{
        public String thumb;
        public String original;
    }
    public List<String> getThumbList(){
        List<String> imagList = new ArrayList<>();

        for (int i = 0; i < image.size(); i++) {
            imagList.add(image.get(i).thumb);
        }
        return imagList;
    }
}
