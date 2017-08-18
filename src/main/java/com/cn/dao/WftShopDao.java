package com.cn.dao;

import com.cn.entity.po.Wft_PayType;
import com.cn.entity.po.Wft_Shop;

import java.util.List;
import java.util.Map;

import com.cn.entity.po.Wft_Shop;

/**
 * Created by cuixiaowei on 2017/1/11.
 */
public interface WftShopDao {
    /**
     * 威富通商户列表
     * @param source
     * @return
     * @throws Exception
     */

    public List<Map<String, Object>> getWftShopList(Map<String, Object> source,String mark)throws Exception;

    public int getWftShopListCount(Map<String, Object> source,String mark) throws Exception;
    /**
     * 保存威富通商户信息
     * @param wftShop
     * @return
     * @throws Exception
     */
    public String insertWftShopInfo(Wft_Shop wftShop) throws Exception;

    /**
     * 根据某字段查询实体
     * @param filds 数据库字段名
     * @param paramters 参数
     * @return
     * @throws Exception
     */
    public Wft_Shop checkWftShopInfoByParamters(String filds,String paramters) throws Exception;

    public List<Wft_Shop> checkWftShopInfoByParamters(String paramters) throws Exception;

    /**
     * 保存威富通商户支付类型信息
     * @param wftPayType
     * @return
     * @throws Exception
     */
    public String insertWftPayTypeInfo(Wft_PayType wftPayType) throws Exception;

    /**
     * 根据主键查询实体
     * @param id 数据库字段名
     * @return
     * @throws Exception
     */
    public Wft_Shop getWftShopInfoByID(String id) throws Exception;

    /**
     * 根据主键查询实体
     * @param wftid 威富通主键
     * @return
     * @throws Exception
     */
    public List<Wft_PayType> getWftPayTypeByParamters(String wftid) throws Exception;


    /**
     * 根据主键ID查询Wft_PayType 信息
     * @param ID
     * @return
     * @throws Exception
     */
    public Wft_PayType getWft_PayTypeByID(String ID) throws Exception;

    /****/
    public String insert(Wft_Shop shop)throws Exception;
    /****/
    public String updateStatus(String wftId,String status)throws Exception;
    /****/
    public String updateBill(String wftId,Object wftWXBill,Object wftZFBBill)throws Exception;
    /****/
    public Map<String, Object> obtainShopDetailMSg(String wftId)throws Exception;
    /****/
    public Wft_Shop obtainById(String wftId)throws Exception;
    /****/
    public String update(Wft_Shop shop)throws Exception;
    /****/
    public List<Map<String, Object>> obtainPayType(String wftId)throws Exception;
    /*****/
    public List<Map<String, Object>> obtainBankCodeList(int currentPage,int pageSize,
    		Object bankCode,Object bankName)throws Exception;
    /****/
    public int obtainBankCodeCount(Object bankCode,Object bankName)throws Exception;

}
