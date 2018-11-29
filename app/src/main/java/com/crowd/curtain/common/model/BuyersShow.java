package com.crowd.curtain.common.model;

import java.util.List;

/**
 * Created by zhangpeng on 2018/4/5.
 */

public class BuyersShow {
    public String showsId;//": "1",
    public List<Images> showsImage;
     public String showsDesc;//": "jkkkk",
     public String showsTime;//": "2018-03-26 14:11:00"
        public static class Images {
            public String thumb;
            public String original;

        }

}
