package com.cn.dao;

import java.util.List;
import java.util.Map;

import com.cn.entity.po.Allpay_ChannelSet;
import com.cn.entity.po.KouBeiShop;


/**
 * Created by cuixiaowei on 2017/1/11.
 */
public interface ZfbKouBeiShopDao
{
    /**
     * 2.2.1新增支付宝口碑信息
     * @param
     * @return
     * @throws Exception
     */
    public String insertZfbShop(KouBeiShop kouBeiShop)throws Exception;
    /****/
    public List<Map<String, Object>> obtainShopList(Object merchantName,Object branchId,Object storeState
    		,int currentPage,int pageSize)throws Exception;
    /****/
    public int obtainShopNumber(Object merchantName,Object branchId,Object storeState)throws Exception;
    /****/
    public Map<String, Object> obtainShop(String shopId)throws Exception;
    /****/
    public String update(KouBeiShop shop)throws Exception;
    /****/
    public String updateStoreNumber(String storeId,String storeNum)throws Exception;
    /****/
    public KouBeiShop obtainForId(String shopId)throws Exception;

    public List<Map<String, Object>> getKouBeiByPID(String shopPID) throws Exception;
    public List<Allpay_ChannelSet> getKouBeiShopIDandToken(String shopid,String code,String marked) throws Exception;
    public List<KouBeiShop> getKouBeiByStoreID(String storeId) throws Exception;
}





