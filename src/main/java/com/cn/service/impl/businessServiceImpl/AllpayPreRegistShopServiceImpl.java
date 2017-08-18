package com.cn.service.impl.businessServiceImpl;

import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.common.LogHelper;
import com.cn.dao.AllpayPreRegistShopDao;
import com.cn.entity.dto.AllpayPreRegistShopDto;
import com.cn.entity.dto.AllpayTerminalDto;
import com.cn.entity.po.Allpay_Merchant;
import com.cn.entity.po.Allpay_PreRegistShop;
import com.cn.entity.po.Allpay_Store;
import com.cn.entity.po.Allpay_Terminal;
import com.cn.service.businessService.AllpayPreRegistShopService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sun.yayi on 2017/2/8.
 */
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@Service
public class AllpayPreRegistShopServiceImpl implements AllpayPreRegistShopService {

    @Autowired
    private AllpayPreRegistShopDao allpayPreRegistShopDaoImpl;

    @Override
    public JSONObject getList(AllpayPreRegistShopDto bean) throws Exception {
        JSONObject resultJO=new JSONObject();
        if(bean!=null){
            String currentPage=bean.getCurragePage();
            String pageSize=bean.getPageSize();
            if(CommonHelper.isEmpty(currentPage) || CommonHelper.isEmpty(pageSize) ){
                resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00110);
                resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00110);
                return resultJO;
            }
            List<Map<String, Object>> list=allpayPreRegistShopDaoImpl.obtainList(bean, Integer.parseInt(currentPage) - 1, Integer.parseInt(pageSize));
            int total=allpayPreRegistShopDaoImpl.count(bean);
            JSONArray array=new JSONArray();
            if(list!=null && list.size()>0){
                for(int i=0;i<list.size();i++){
                    Map<String, Object> map=list.get(i);
                    JSONObject json=new JSONObject();
                    json.put("rowNum",CommonHelper.nullToString(map.get("ROWNUM")));
                    json.put("cdata",CommonHelper.nullToString(map.get("CDATE")));
                    json.put("branchCompanyName",CommonHelper.nullToString(map.get("BRANCHCOMPANYNAME")));
                    json.put("branchCompanyShortName",CommonHelper.nullToString(map.get("BRANCHCOMPANYSHORTNAME")));
                    json.put("shopName",CommonHelper.nullToString(map.get("SHOPNAME")));
                    json.put("shopState",CommonHelper.nullToString(map.get("SHOPSTATE")));
                    json.put("storeName",CommonHelper.nullToString(map.get("STORENAME")));
                    json.put("storeprovinceName",CommonHelper.nullToString(map.get("STOREPROVINCENAME")));
                    json.put("storeAdress",CommonHelper.nullToString(map.get("STOREADRESS")));
                    json.put("person",CommonHelper.nullToString(map.get("PERSON")));
                    json.put("personPhone",CommonHelper.nullToString(map.get("PERSONPHONE")));
                    array.put(json);
                }
            }
            resultJO.put("lists",array);
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
            resultJO.put("curragePage",currentPage);
            resultJO.put("pageSize",pageSize);
            resultJO.put("total",total);
        }else{
            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.CODE_003_MSG);
            return resultJO;
        }
        return resultJO;
    }

    @Override
    public JSONObject importPreRegistShop(Map<String, Object> source) throws Exception {
        JSONObject resultJO = new JSONObject();
        if (source != null) {
            Object object = source.get("jsonStr");
            if(object instanceof List){
                List objectOne = (List)object;
                for(Object objectTwo : objectOne){
                    if(objectTwo instanceof Map){
                        Map objectThree = (Map)objectTwo;
                        String branchCompanyName = CommonHelper.nullOrEmptyToString(objectThree.get("branchCompanyName"));  //所属分子公司(全称)
                        String branchCompanyShortName = CommonHelper.nullOrEmptyToString(objectThree.get("branchCompanyShortName"));  //所属分子公司(简称))
                        String shopName = CommonHelper.nullOrEmptyToString(objectThree.get("shopName"));  //商户名称
                        String shopState = CommonHelper.nullOrEmptyToString(objectThree.get("shopState"));  //商户类别
                        String storeName = CommonHelper.nullOrEmptyToString(objectThree.get("storeName"));  //门店名称
                        String storeprovinceName = CommonHelper.nullOrEmptyToString(objectThree.get("storeprovinceName"));  //门店省份
                        String storeAdress = CommonHelper.nullOrEmptyToString(objectThree.get("storeAdress"));  //门店地址
                        String person = CommonHelper.nullOrEmptyToString(objectThree.get("person"));  //联系人
                        String personPhone = CommonHelper.nullOrEmptyToString(objectThree.get("personPhone"));  //联系电话
                        String dataSource = CommonHelper.nullOrEmptyToString(objectThree.get("dataSource"));  //数据来源
                        Date now = CommonHelper.getNowDateShort();
                        //插入预注册商户信息
                        Allpay_PreRegistShop preRegistShop = new Allpay_PreRegistShop();
                        preRegistShop.setBranchCompanyName(branchCompanyName);
                        preRegistShop.setBranchCompanyShortName(branchCompanyShortName);
                        preRegistShop.setShopName(shopName);
                        preRegistShop.setCdate(now);
                        preRegistShop.setShopState(shopState);
                        preRegistShop.setStoreName(storeName);
                        preRegistShop.setStoreprovinceName(storeprovinceName);
                        preRegistShop.setStoreAdress(storeAdress);
                        preRegistShop.setPerson(person);
                        preRegistShop.setPersonPhone(personPhone);
                        preRegistShop.setFlag("1");
                        preRegistShop.setDataSource(dataSource);

                        String result = allpayPreRegistShopDaoImpl.insert(preRegistShop);
                        if (!"success".equals(result)) {
                            resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_00601);
                            resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.MESSAGE_00601);
                            return resultJO;
                        }
                        resultJO.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
                        resultJO.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
                    }
                }
            }
        }
        return resultJO;
    }
}
