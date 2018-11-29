package com.crowd.curtain.common.model;

import java.util.List;

/**
 * Created by zhangpeng on 2018/4/2.
 */

public class ProductDetail{
    public String productId;
    public String productName;
    public List<ProImage> productImage;
    public String parentCateId;
    public String productNumber;
    public String parentCateName;
    public String brandStory;
    public String cateId;
    public String cateName;
    public String cloth;
    public String origin;

    public List<ProImage> display;//产品展示
    public List<ProImage> details;//产品细节
    public ProImage productParameter;//产品参数图片
    public ProImage materialImage;//产品素材图片
    public List<BuyersShow> buyersShow;

}
