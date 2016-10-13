package com.rilin.lzy.mybase.net;


public interface HttpUrl {

	//基地址
	String BASE_URL="http://123.57.31.61:8080/";
	//首页数据
	String HOME_DATA=BASE_URL+"/Api/index/getIndexListInfo";
	//搜索
	String SEARCH=BASE_URL+"/Api/goods/getGoodsSearchListInfo";
	//分类首页接口
	String CATEGORY=BASE_URL+"/Api/goods/getGoodsCategoryListInfo";
	
}
