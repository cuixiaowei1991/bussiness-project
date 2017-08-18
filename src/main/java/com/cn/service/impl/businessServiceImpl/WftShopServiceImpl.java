package com.cn.service.impl.businessServiceImpl;

import com.cn.common.JsonHelper;
import com.cn.common.LogHelper;
import com.cn.common.WriteExcelFile;
import com.cn.dao.AllpayMerchantDao;
import org.springframework.beans.factory.annotation.Autowired;


import com.cn.MsgCode.MsgAndCode;
import com.cn.common.CommonHelper;
import com.cn.dao.WftShopDao;
import com.cn.entity.po.Wft_PayType;
import com.cn.entity.po.Wft_Shop;
import com.cn.service.businessService.WftShopService;
import org.codehaus.jackson.map.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.codehaus.jackson.node.ArrayNode;

import org.codehaus.jackson.node.ObjectNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;


/**
 * Created by cuixiaowei on 2017/1/11.
 */
@Service("WftShopService")
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class WftShopServiceImpl implements WftShopService {


    @Autowired
    private WftShopDao wftShopDao;
    @Autowired
    private AllpayMerchantDao allpayMerchantDao;

    @Value("${excelpath}")
    private String excelpath;

    @Value("${outpath}")
    private String outpath;
    @Override
    public String getWftShopList(Map<String, Object> source) throws Exception
    {
        if(CommonHelper.isNullOrEmpty(source.get("curragePage")))
        {
            return returnMissParamMessage("当前页：curragePage");
        }
        if(CommonHelper.isNullOrEmpty(source.get("pageSize")))
        {
            return returnMissParamMessage("记录数：pageSize");
        }
        List<Map<String, Object>> wftListMap= wftShopDao.getWftShopList(source,"pageCurrage");
        if(wftListMap==null || wftListMap.size()<=0)
        {
            return returnNullObjectMsg("信息");
        }
        JSONArray jsonArray=new JSONArray();
        JSONObject wftJo=new JSONObject();

        for(Map<String, Object> wftMap:wftListMap)
        {

           /* if(!CommonHelper.isNullOrEmpty(source.get("isZFTypeEx")))
            {
                if("1".equals(String.valueOf(source.get("isZFTypeEx"))))
                {
                    if("未完成".equals(String.valueOf(wftMap.get("PAYNAME"))))
                    {
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("wftId",wftMap.get("WFT_SHOP_ID")==null?"":wftMap.get("WFT_SHOP_ID"));
                        jsonObject.put("shopId",wftMap.get("WFT_SHOP_OUTMERCHANTID")==null?"":wftMap.get("WFT_SHOP_OUTMERCHANTID"));
                        jsonObject.put("shopName",wftMap.get("MERCHANT_MERCHNAME")==null?"":wftMap.get("MERCHANT_MERCHNAME"));
                        jsonObject.put("branchId",wftMap.get("MERCHANT_BRANCHCOMPANYID")==null?"":wftMap.get("MERCHANT_BRANCHCOMPANYID"));
                        jsonObject.put("branchName",wftMap.get("ORGANIZATION_NAME")==null?"":wftMap.get("ORGANIZATION_NAME"));
                        jsonObject.put("wftShopName",wftMap.get("WFT_SHOP_NAME")==null?"":wftMap.get("WFT_SHOP_NAME"));
                        jsonObject.put("wftShopCode",wftMap.get("WFT_SHOP_MERCHANTID")==null?"":wftMap.get("WFT_SHOP_MERCHANTID"));
                        jsonObject.put("wftBankType",wftMap.get("WFT_SHOP_BANKTYPE")==null?"":wftMap.get("WFT_SHOP_BANKTYPE"));
                        jsonObject.put("wftWXBill",wftMap.get("WFT_SHOP_WXBILL")==null?"":wftMap.get("WFT_SHOP_WXBILL"));
                        jsonObject.put("wftZFBBill",wftMap.get("WFT_SHOP_ZFBBILL")==null?"":wftMap.get("WFT_SHOP_ZFBBILL"));
                        jsonObject.put("isState",wftMap.get("WFT_SHOP_STATE")==null?"":wftMap.get("WFT_SHOP_STATE"));
                        jsonObject.put("createTime",wftMap.get("ALLPAY_CREATETIME")==null?"":wftMap.get("ALLPAY_CREATETIME"));
                        jsonObject.put("wftAgent",wftMap.get("AGENT_ID")==null?"":wftMap.get("AGENT_ID"));
                        jsonObject.put("wftAgentName",wftMap.get("AGENT_NAME")==null?"":wftMap.get("AGENT_NAME"));

                        jsonObject.put("payType","未完成");


                        jsonObject.put("wftBackMess",wftMap.get("WFT_SHOP_BACKMESS")==null?"":wftMap.get("WFT_SHOP_BACKMESS"));
                        jsonObject.put("createName",wftMap.get("ALLPAY_CREATER")==null?"":wftMap.get("ALLPAY_CREATER"));
                        jsonObject.put("shopCodeBackTime",wftMap.get("WFT_SHOP_BACKDATE")==null?"":wftMap.get("WFT_SHOP_BACKDATE"));
                        jsonObject.put("linenum",wftMap.get("LINENUM"));

                        jsonArray.put(jsonObject);
                    }
                    LogHelper.info("----进入未完成---");
                }
                else
                {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("wftId",wftMap.get("WFT_SHOP_ID")==null?"":wftMap.get("WFT_SHOP_ID"));
                    jsonObject.put("shopId",wftMap.get("WFT_SHOP_OUTMERCHANTID")==null?"":wftMap.get("WFT_SHOP_OUTMERCHANTID"));
                    jsonObject.put("shopName",wftMap.get("MERCHANT_MERCHNAME")==null?"":wftMap.get("MERCHANT_MERCHNAME"));
                    jsonObject.put("branchId",wftMap.get("MERCHANT_BRANCHCOMPANYID")==null?"":wftMap.get("MERCHANT_BRANCHCOMPANYID"));
                    jsonObject.put("branchName",wftMap.get("ORGANIZATION_NAME")==null?"":wftMap.get("ORGANIZATION_NAME"));
                    jsonObject.put("wftShopName",wftMap.get("WFT_SHOP_NAME")==null?"":wftMap.get("WFT_SHOP_NAME"));
                    jsonObject.put("wftShopCode",wftMap.get("WFT_SHOP_MERCHANTID")==null?"":wftMap.get("WFT_SHOP_MERCHANTID"));
                    jsonObject.put("wftBankType",wftMap.get("WFT_SHOP_BANKTYPE")==null?"":wftMap.get("WFT_SHOP_BANKTYPE"));
                    jsonObject.put("wftWXBill",wftMap.get("WFT_SHOP_WXBILL")==null?"":wftMap.get("WFT_SHOP_WXBILL"));
                    jsonObject.put("wftZFBBill",wftMap.get("WFT_SHOP_ZFBBILL")==null?"":wftMap.get("WFT_SHOP_ZFBBILL"));
                    jsonObject.put("isState",wftMap.get("WFT_SHOP_STATE")==null?"":wftMap.get("WFT_SHOP_STATE"));
                    jsonObject.put("createTime",wftMap.get("ALLPAY_CREATETIME")==null?"":wftMap.get("ALLPAY_CREATETIME"));
                    jsonObject.put("wftAgent",wftMap.get("AGENT_ID")==null?"":wftMap.get("AGENT_ID"));
                    jsonObject.put("wftAgentName",wftMap.get("AGENT_NAME")==null?"":wftMap.get("AGENT_NAME"));

                    jsonObject.put("payType","完成");


                    jsonObject.put("wftBackMess",wftMap.get("WFT_SHOP_BACKMESS")==null?"":wftMap.get("WFT_SHOP_BACKMESS"));
                    jsonObject.put("createName",wftMap.get("ALLPAY_CREATER")==null?"":wftMap.get("ALLPAY_CREATER"));
                    jsonObject.put("shopCodeBackTime",wftMap.get("WFT_SHOP_BACKDATE")==null?"":wftMap.get("WFT_SHOP_BACKDATE"));
                    jsonObject.put("linenum",wftMap.get("LINENUM")==null);
                    jsonArray.put(jsonObject);

                    LogHelper.info("----进入已完成---");
                }
            }*/
            /*else
            {*/
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("wftId",wftMap.get("WFT_SHOP_ID")==null?"":wftMap.get("WFT_SHOP_ID"));
                jsonObject.put("shopId",wftMap.get("WFT_SHOP_OUTMERCHANTID")==null?"":wftMap.get("WFT_SHOP_OUTMERCHANTID"));
                jsonObject.put("shopName",wftMap.get("MERCHANT_MERCHNAME")==null?"":wftMap.get("MERCHANT_MERCHNAME"));
                jsonObject.put("branchId",wftMap.get("MERCHANT_BRANCHCOMPANYID")==null?"":wftMap.get("MERCHANT_BRANCHCOMPANYID"));
                jsonObject.put("branchName",wftMap.get("ORGANIZATION_NAME")==null?"":wftMap.get("ORGANIZATION_NAME"));
                jsonObject.put("wftShopName",wftMap.get("WFT_SHOP_NAME")==null?"":wftMap.get("WFT_SHOP_NAME"));
                jsonObject.put("wftShopCode",wftMap.get("WFT_SHOP_MERCHANTID")==null?"":wftMap.get("WFT_SHOP_MERCHANTID"));
                jsonObject.put("wftBankType",wftMap.get("WFT_SHOP_BANKTYPE")==null?"":wftMap.get("WFT_SHOP_BANKTYPE"));
                jsonObject.put("wftWXBill",wftMap.get("WFT_SHOP_WXBILL")==null?"":wftMap.get("WFT_SHOP_WXBILL"));
                jsonObject.put("wftZFBBill",wftMap.get("WFT_SHOP_ZFBBILL")==null?"":wftMap.get("WFT_SHOP_ZFBBILL"));
                jsonObject.put("isState",wftMap.get("WFT_SHOP_STATE")==null?"":wftMap.get("WFT_SHOP_STATE"));
                jsonObject.put("createTime",wftMap.get("ALLPAY_CREATETIME")==null?"":wftMap.get("ALLPAY_CREATETIME"));
                jsonObject.put("wftAgent",wftMap.get("AGENT_ID")==null?"":wftMap.get("AGENT_ID"));
                jsonObject.put("wftAgentName",wftMap.get("AGENT_NAME")==null?"":wftMap.get("AGENT_NAME"));

                jsonObject.put("payType",wftMap.get("PAYNAME"));


                jsonObject.put("wftBackMess",wftMap.get("WFT_SHOP_BACKMESS")==null?"":wftMap.get("WFT_SHOP_BACKMESS"));
                jsonObject.put("createName",wftMap.get("ALLPAY_CREATER")==null?"":wftMap.get("ALLPAY_CREATER"));
                jsonObject.put("shopCodeBackTime",wftMap.get("WFT_SHOP_BACKDATE")==null?"":wftMap.get("WFT_SHOP_BACKDATE"));
                jsonObject.put("secretKey",wftMap.get("WFT_SHOP_SECRETKEY")==null?"":wftMap.get("WFT_SHOP_SECRETKEY"));
                jsonObject.put("appid",wftMap.get("WFT_SHOP_APPID")==null?"":wftMap.get("WFT_SHOP_APPID"));
                jsonObject.put("linenum",wftMap.get("LINENUM"));
                jsonArray.put(jsonObject);

                //LogHelper.info("----进入全部---");
           // }

        }
        int count= wftShopDao.getWftShopListCount(source,"count");
        wftJo.put("lists",jsonArray);
        wftJo.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
        wftJo.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
        wftJo.put("total",count);
        wftJo.put("curragePage",source.get("curragePage"));
        wftJo.put("pageSize",source.get("pageSize"));
        return wftJo.toString();
    }

    @Override
    public String insertWftShopInfo(Map<String, Object> source) throws Exception
    {
        String shopId = ""+source.get("shopId");  //外部商户id（业务系统商户id）
        String shopName = ""+source.get("shopName");  //商户名称
        String isImport = ""+source.get("isImport");  //是否是导入数据标识	0:不是导入,1是导入
        String opration = MsgAndCode.OPERATION_NEW;  //操作
        if(CommonHelper.isNullOrEmpty(shopName))
        {
            return returnMissParamMessage("shopName");
        }
        if(CommonHelper.isNullOrEmpty(isImport))
        {
            return returnMissParamMessage("isImport");
        }
        if(!"0".equals(isImport) && !"1".equals(isImport)){
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();
            node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_002);
            node.put(MsgAndCode.RSP_DESC, "是否是导入数据标识字段不正确！");
            return node.toString();
        }
        if("0".equals(isImport) && CommonHelper.isNullOrEmpty(shopId)){
            return returnMissParamMessage("shopId");
        }
        if("1".equals(isImport) && CommonHelper.isNullOrEmpty(shopId)){
            //查询业务系统商户id(批量导入)
            List<Map<String, Object>> list = (List<Map<String, Object>>)allpayMerchantDao.checkMerName(shopName,null);
            if(null != list && list.size() > 0){
                shopId = ""+list.get(0).get("MERCHANT_ID");
                opration = MsgAndCode.OPERATION_BULK_IMPORT;
            }else{
                return returnNullObjectMsg("根据商户名称");
            }
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftIndustry")))
        {
            return returnMissParamMessage("wftIndustry");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftBank")))
        {
            return returnMissParamMessage("wftBank");
        }
        if(CommonHelper.isNullOrEmpty(source.get("province")))
        {
            return returnMissParamMessage("province");
        }
        if(CommonHelper.isNullOrEmpty(source.get("city")))
        {
            return returnMissParamMessage("city");
        }
        if(CommonHelper.isNullOrEmpty(source.get("address")))
        {
            return returnMissParamMessage("address");
        }
        if(CommonHelper.isNullOrEmpty(source.get("principal")))
        {
            return returnMissParamMessage("principal");
        }
        if(CommonHelper.isNullOrEmpty(source.get("principalEmail")))
        {
            return returnMissParamMessage("principalEmail");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankAccountName")))
        {
            return returnMissParamMessage("bankAccountName");
        }
        if(CommonHelper.isNullOrEmpty(source.get("accountType")))
        {
            return returnMissParamMessage("accountType");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankCardNum")))
        {
            return returnMissParamMessage("bankCardNum");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankId")))
        {
            return returnMissParamMessage("bankId");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankProvince")))
        {
            return returnMissParamMessage("bankProvince");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankCity")))
        {
            return returnMissParamMessage("bankCity");
        }
        if(CommonHelper.isNullOrEmpty(source.get("lhBankCode")))
        {
            return returnMissParamMessage("lhBankCode");
        }
        if(CommonHelper.isNullOrEmpty(source.get("lhBankName")))
        {
            return returnMissParamMessage("lhBankName");
        }
        if(CommonHelper.isNullOrEmpty(source.get("idCardType")))
        {
            return returnMissParamMessage("idCardType");
        }
        if(CommonHelper.isNullOrEmpty(source.get("idCardNo")))
        {
            return returnMissParamMessage("idCardNo");
        }
        if(CommonHelper.isNullOrEmpty(source.get("idCardPhone")))
        {
            return returnMissParamMessage("idCardPhone");
        }
        if(CommonHelper.isNullOrEmpty(source.get("idCardAddress")))
        {
            return returnMissParamMessage("idCardAddress");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftWXBill")))
        {
            return returnMissParamMessage("wftWXBill");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftZFBBill")))
        {
            return returnMissParamMessage("wftZFBBill");
        }
        if(CommonHelper.isNullOrEmpty(source.get("isState")))
        {
            return returnMissParamMessage("isState");
        }

        Wft_Shop wftShop=new Wft_Shop();
        wftShop.setWft_shop_address(String.valueOf(source.get("address")));
        //wftShop.setWft_shop_backdate(String.valueOf(source.get("address")));
        wftShop.setWft_shop_name(String.valueOf(source.get("shopName")));
        wftShop.setWft_shop_outmerchantid(shopId);
        wftShop.setWft_shop_industryid(String.valueOf(source.get("wftIndustry")));
        wftShop.setWft_shop_banktype(String.valueOf(source.get("wftBank")));
        wftShop.setWft_shop_provinceid(String.valueOf(source.get("province")));
        wftShop.setWft_shop_cityid(String.valueOf(source.get("city")));
        wftShop.setWft_shop_principal(String.valueOf(source.get("principal")));
        wftShop.setWft_shop_principalemail(String.valueOf(source.get("principalEmail")));
        wftShop.setWft_shop_bankaccountname(String.valueOf(source.get("bankAccountName")));
        wftShop.setWft_shop_bankaccounttype(String.valueOf(source.get("accountType")));
        wftShop.setWft_shop_bankcardnum(String.valueOf(source.get("bankCardNum")));
        wftShop.setWft_shop_bankid(String.valueOf(source.get("bankId")));
        wftShop.setWft_shop_bankcodeproviceid(String.valueOf(source.get("bankProvince")));
        wftShop.setWft_shop_bankcodecityid(String.valueOf(source.get("bankCity")));
        wftShop.setWft_shop_bankcodeid(String.valueOf(source.get("lhBankCode")));
        wftShop.setWft_shop_bankcodename(String.valueOf(source.get("lhBankName")));
        wftShop.setWft_shop_bankidcardtype(String.valueOf(source.get("idCardType")));
        wftShop.setWft_shop_bankidcard(String.valueOf(source.get("idCardNo")));
        wftShop.setWft_shop_bankaccounttel(String.valueOf(source.get("idCardPhone")));
        wftShop.setWft_shop_wxbill(String.valueOf(source.get("wftWXBill")));
        wftShop.setWft_shop_zfbbill(String.valueOf(source.get("wftZFBBill")));
        wftShop.setWft_shop_state(String.valueOf(source.get("isState")));
        wftShop.setWft_shop_bankAccountAddress(String.valueOf(source.get("idCardAddress")));

        JSONObject publicFileds = CommonHelper.getPublicFileds(opration, "",CommonHelper.nullToString(source.get("userNameFromBusCookie")));
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");

        wftShop.setALLPAY_CREATETIME(now);
        wftShop.setALLPAY_UPDATETIME(now);
        wftShop.setALLPAY_LOGICDEL("1");
        wftShop.setALLPAY_CREATER(userName);
        wftShop.setALLPAY_LOGRECORD(record);
        wftShop.setALLPAY_ISSTART(Integer.parseInt(String.valueOf(source.get("isState"))));
        wftShop.setALLPAY_STATE(Integer.parseInt(String.valueOf(source.get("isState"))));
        List<Wft_Shop> wftShopList= wftShopDao.checkWftShopInfoByParamters(shopId);
        if(wftShopList.size()>0)
        {
            JSONObject wftJo=new JSONObject();
            wftJo.put(MsgAndCode.RSP_CODE,MsgAndCode.CODE_00501);
            wftJo.put(MsgAndCode.RSP_DESC,MsgAndCode.MESSAGE_00501);
            return wftJo.toString();
        }
        //主表保存
        String result=wftShopDao.insertWftShopInfo(wftShop);
        if("success".equals(result))
        {
            List<Wft_Shop> wftShopList_1= wftShopDao.checkWftShopInfoByParamters(shopId);
            Wft_Shop wftShop1=wftShopList_1.get(0);
            if(wftShop1!=null)
            {
                String payCode[]={"10000167","10000168","10000169","141","142","143"};
                //String payCode[]={"221","222","223"};
                String payName[]={"支付宝-js支付(兴业总行2)","支付宝-扫码支付(兴业总行2)","支付宝-小额支付(兴业总行2)" ,
                        "线下扫码支付(兴业)-微信","线下小额支付(兴业)-微信","公众账号支付(兴业)-微信"};
                /*String payName[]={
                        "线下扫码支付(兴业)-微信","线下小额支付(兴业)-微信","公众账号支付(兴业)-微信"};*/
                for(int i=0;i<payCode.length;i++)
                {
                    Wft_PayType wft_payType=new Wft_PayType();
                    wft_payType.setWft_paytype_wftshop_id(wftShop1.getWft_shop_id());
                    if(String.valueOf(payCode[i]).equals("10000167") || String.valueOf(payCode[i]).equals("10000168")||
                            String.valueOf(payCode[i]).equals("10000169"))
                    {
                        wft_payType.setWft_paytype_bill(String.valueOf(source.get("wftZFBBill")));
                        wft_payType.setWft_paytype_type("0");
                        //wft_payType.setWft_paytype_thirdcode("");
                        if(String.valueOf(payCode[i]).equals("10000167"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.alipay.jspayv3");
                        }
                        if(String.valueOf(payCode[i]).equals("10000168"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.alipay.nativev3");
                        }
                        if(String.valueOf(payCode[i]).equals("10000169"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.alipay.micropayv3");
                        }
                    }
                    if(String.valueOf(payCode[i]).equals("141") || String.valueOf(payCode[i]).equals("142")||
                            String.valueOf(payCode[i]).equals("143"))
                    {
                        wft_payType.setWft_paytype_bill(String.valueOf(source.get("wftWXBill")));
                        wft_payType.setWft_paytype_type("1");
                        wft_payType.setWft_paytype_thirdcode("15303099");
                        if(String.valueOf(payCode[i]).equals("141"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.weixin.native");
                        }
                        if(String.valueOf(payCode[i]).equals("142"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.weixin.micropay");
                        }
                        if(String.valueOf(payCode[i]).equals("143"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.weixin.jspay");
                        }
                    }
                    if(String.valueOf(payCode[i]).equals("221") || String.valueOf(payCode[i]).equals("222")||
                            String.valueOf(payCode[i]).equals("223"))
                    {
                        wft_payType.setWft_paytype_bill(String.valueOf(source.get("wftWXBill")));
                        wft_payType.setWft_paytype_type("1");
                        wft_payType.setWft_paytype_thirdcode("15303099");
                        if(String.valueOf(payCode[i]).equals("221"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.weixin.native");
                        }
                        if(String.valueOf(payCode[i]).equals("222"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.weixin.micropay");
                        }
                        if(String.valueOf(payCode[i]).equals("223"))
                        {
                            wft_payType.setWft_paytype_wftcode("pay.weixin.jspay");
                        }
                    }
                    wft_payType.setWft_paytype_name(payName[i]);
                    wft_payType.setWft_paytype_code(payCode[i]);
                    wft_payType.setALLPAY_UPDATETIME(new Date());
                    wft_payType.setALLPAY_CREATETIME(new Date());
                    wft_payType.setALLPAY_STATE(1);
                    wft_payType.setALLPAY_ISSTART(1);
                    wftShopDao.insertWftPayTypeInfo(wft_payType);
                }
                JSONObject wftJo=new JSONObject();
                wftJo.put(MsgAndCode.RSP_CODE,"000");
                wftJo.put(MsgAndCode.RSP_DESC,"成功");
                wftJo.put("wftId",wftShop1.getWft_shop_id());
                return wftJo.toString();
            }
            else
            {
                return returnNullObjectMsg("已保存的威富通商户主键，");
            }

        }
        else
        {
            JSONObject wftJo=new JSONObject();
            wftJo.put(MsgAndCode.RSP_CODE,MsgAndCode.CODE_00500);
            wftJo.put(MsgAndCode.RSP_DESC,MsgAndCode.MESSAGE_00500);
            return wftJo.toString();
        }
    }

    @Override
    public String updateWftShop(Map<String, Object> source) throws Exception
    {
        if(CommonHelper.isNullOrEmpty(source.get("shopName")))
        {
            return returnMissParamMessage("shopName");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftId")))
        {
            return returnMissParamMessage("wftId");
        }
        if(CommonHelper.isNullOrEmpty(source.get("shopId")))
        {
            return returnMissParamMessage("shopId");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftIndustry")))
        {
            return returnMissParamMessage("wftIndustry");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftBank")))
        {
            return returnMissParamMessage("wftBank");
        }
        if(CommonHelper.isNullOrEmpty(source.get("province")))
        {
            return returnMissParamMessage("province");
        }
        if(CommonHelper.isNullOrEmpty(source.get("city")))
        {
            return returnMissParamMessage("city");
        }
        if(CommonHelper.isNullOrEmpty(source.get("address")))
        {
            return returnMissParamMessage("address");
        }
        if(CommonHelper.isNullOrEmpty(source.get("principal")))
        {
            return returnMissParamMessage("principal");
        }
        if(CommonHelper.isNullOrEmpty(source.get("principalEmail")))
        {
            return returnMissParamMessage("principalEmail");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankAccountName")))
        {
            return returnMissParamMessage("bankAccountName");
        }
        if(CommonHelper.isNullOrEmpty(source.get("accountType")))
        {
            return returnMissParamMessage("accountType");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankCardNum")))
        {
            return returnMissParamMessage("bankCardNum");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankId")))
        {
            return returnMissParamMessage("bankId");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankProvince")))
        {
            return returnMissParamMessage("bankProvince");
        }
        if(CommonHelper.isNullOrEmpty(source.get("bankCity")))
        {
            return returnMissParamMessage("bankCity");
        }
        if(CommonHelper.isNullOrEmpty(source.get("lhBankCode")))
        {
            return returnMissParamMessage("lhBankCode");
        }
        if(CommonHelper.isNullOrEmpty(source.get("lhBankName")))
        {
            return returnMissParamMessage("lhBankName");
        }
        if(CommonHelper.isNullOrEmpty(source.get("idCardType")))
        {
            return returnMissParamMessage("idCardType");
        }
        if(CommonHelper.isNullOrEmpty(source.get("idCardNo")))
        {
            return returnMissParamMessage("idCardNo");
        }
        if(CommonHelper.isNullOrEmpty(source.get("idCardPhone")))
        {
            return returnMissParamMessage("idCardPhone");
        }
        if(CommonHelper.isNullOrEmpty(source.get("idCardAddress")))
        {
            return returnMissParamMessage("idCardAddress");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftWXBill")))
        {
            return returnMissParamMessage("wftWXBill");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftZFBBill")))
        {
            return returnMissParamMessage("wftZFBBill");
        }
        if(CommonHelper.isNullOrEmpty(source.get("isState")))
        {
            return returnMissParamMessage("isState");
        }
        Wft_Shop wftShop= wftShopDao.getWftShopInfoByID(String.valueOf(source.get("wftId")));
        if(wftShop==null)
        {
            return returnNullObjectMsg("wftId");
        }
        else
        {
            wftShop.setWft_shop_address(String.valueOf(source.get("address")));
            //wftShop.setWft_shop_backdate(String.valueOf(source.get("address")));
            wftShop.setWft_shop_name(String.valueOf(source.get("shopName")));
            wftShop.setWft_shop_outmerchantid(String.valueOf(source.get("shopId")));
            wftShop.setWft_shop_industryid(String.valueOf(source.get("wftIndustry")));
            wftShop.setWft_shop_banktype(String.valueOf(source.get("wftBank")));
            wftShop.setWft_shop_provinceid(String.valueOf(source.get("province")));
            wftShop.setWft_shop_cityid(String.valueOf(source.get("city")));
            wftShop.setWft_shop_principal(String.valueOf(source.get("principal")));
            wftShop.setWft_shop_principalemail(String.valueOf(source.get("principalEmail")));
            wftShop.setWft_shop_bankaccountname(String.valueOf(source.get("bankAccountName")));
            wftShop.setWft_shop_bankaccounttype(String.valueOf(source.get("accountType")));
            wftShop.setWft_shop_bankcardnum(String.valueOf(source.get("bankCardNum")));
            wftShop.setWft_shop_bankid(String.valueOf(source.get("bankId")));
            wftShop.setWft_shop_bankcodeproviceid(String.valueOf(source.get("bankProvince")));
            wftShop.setWft_shop_bankcodecityid(String.valueOf(source.get("bankCity")));
            wftShop.setWft_shop_bankcodeid(String.valueOf(source.get("lhBankCode")));
            wftShop.setWft_shop_bankcodename(String.valueOf(source.get("lhBankName")));
            wftShop.setWft_shop_bankidcardtype(String.valueOf(source.get("idCardType")));
            wftShop.setWft_shop_bankidcard(String.valueOf(source.get("idCardNo")));
            wftShop.setWft_shop_bankaccounttel(String.valueOf(source.get("idCardPhone")));
            wftShop.setWft_shop_wxbill(String.valueOf(source.get("wftWXBill")));
            wftShop.setWft_shop_zfbbill(String.valueOf(source.get("wftZFBBill")));
            wftShop.setWft_shop_state(String.valueOf(source.get("isState")));
            wftShop.setWft_shop_bankAccountAddress(String.valueOf(source.get("idCardAddress")));
            wftShop.setALLPAY_ISSTART(Integer.parseInt(String.valueOf(source.get("isState"))));
            wftShop.setALLPAY_UPDATETIME(new Date());
            JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, wftShop.getALLPAY_LOGRECORD(),CommonHelper.nullToString(source.get("userNameFromBusCookie")));
            String userName = publicFileds.getString("userName");
            Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
            String record = publicFileds.getString("record");
            wftShop.setALLPAY_LOGRECORD(record);
            wftShop.setALLPAY_UPDATER(userName);
            wftShop.setALLPAY_STATE(Integer.parseInt(String.valueOf(source.get("isState"))));
            //主表保存
            String result=wftShopDao.insertWftShopInfo(wftShop);
            if("success".equals(result))
            {
                List<Wft_PayType> wftPayTypeList= wftShopDao.getWftPayTypeByParamters(String.valueOf(source.get("wftId")));
                for(Wft_PayType wftPayType : wftPayTypeList)
                {
                    if("0".equals(wftPayType.getWft_paytype_type()))
                    {
                        wftPayType.setWft_paytype_bill(String.valueOf(source.get("wftZFBBill")));
                    }
                    if("1".equals(wftPayType.getWft_paytype_type()))
                    {
                        wftPayType.setWft_paytype_bill(String.valueOf(source.get("wftWXBill")));
                    }
                    wftPayType.setALLPAY_STATE(1);
                    wftPayType.setALLPAY_ISSTART(1);
                    wftShopDao.insertWftPayTypeInfo(wftPayType);
                }
                return returnSuccessMessage();
            }
            else
            {
                JSONObject wftJo=new JSONObject();
                wftJo.put(MsgAndCode.RSP_CODE,MsgAndCode.CODE_00500);
                wftJo.put(MsgAndCode.RSP_DESC,MsgAndCode.MESSAGE_00500);
                return wftJo.toString();
            }
        }
     }




    @Override
	public String insertShop(Map<String, Object> source) throws Exception {
		
		Wft_Shop shop = new Wft_Shop();
		String result = insertupdateShopForSource(shop, source,true);
		if(result != null){
			return result;
		}
		return returnSuccessMessage();
	}
	
	@Override
	public String updateMerchantStatus(Map<String, Object> source)
			throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("wftId"))
				|| CommonHelper.isNullOrEmpty(source.get("isState"))){
			returnMissParamMessage("wftId或者isState");
		}
        Wft_Shop wftShop= wftShopDao.obtainById(source.get("wftId").toString());
        wftShop.setWft_shop_state(source.get("isState").toString());
        wftShop.setALLPAY_UPDATETIME(new Date());
        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, wftShop.getALLPAY_LOGRECORD(),CommonHelper.nullToString(source.get("userNameFromBusCookie")));
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");
        wftShop.setALLPAY_UPDATER(userName);
        wftShop.setALLPAY_LOGRECORD(record);
        wftShopDao.insertWftShopInfo(wftShop);
        //wftShopDao.updateStatus(source.get("wftId").toString(), source.get("isState").toString());
		return returnSuccessMessage();
	}

    /**
     * 修改费率
     * @param source
     * @return
     * @throws Exception
     */
	@Override
	public String updateBill(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("wftId"))){
			returnMissParamMessage("wftId");
		}
		if(CommonHelper.isNullOrEmpty(source.get("wftWXBill"))
				&& CommonHelper.isNullOrEmpty(source.get("wftZFBBill"))){
			returnMissParamMessage("wftWXBill或者wftZFBBill");
		}
        wftShopDao.updateBill(source.get("wftId").toString(), source.get("wftWXBill"), source.get("wftZFBBill"));
        List<Wft_PayType> wftPayTypeList= wftShopDao.getWftPayTypeByParamters(String.valueOf(source.get("wftId")));
        for(Wft_PayType wftPayType : wftPayTypeList)
        {
            if("0".equals(wftPayType.getWft_paytype_type()))
            {
                wftPayType.setWft_paytype_bill(String.valueOf(source.get("wftZFBBill")));
            }
            if("1".equals(wftPayType.getWft_paytype_type()))
            {
                wftPayType.setWft_paytype_bill(String.valueOf(source.get("wftWXBill")));
            }
            wftPayType.setALLPAY_UPDATETIME(new Date());
            wftPayType.setALLPAY_STATE(1);
            wftPayType.setALLPAY_ISSTART(1);
            wftShopDao.insertWftPayTypeInfo(wftPayType);
        }



		return returnSuccessMessage();
	}
	

	@Override
	public String updateShop(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("wftId"))){
			returnMissParamMessage("wftId");
		}
		/****/
		Wft_Shop shop = wftShopDao.obtainById(source.get("wftId").toString());
		if(CommonHelper.isNullOrEmpty(shop)){
			return returnNullObjectMsg("根据wftId:"+source.get("wftId").toString());
		}
		String result = insertupdateShopForSource(shop, source, false);
		if(result != null){
			return result;
		}
		return returnSuccessMessage();
	}
	
	@Override
	public String obtainShop(Map<String, Object> source) throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("wftId"))){
			returnMissParamMessage("wftId");
		}
		Map<String, Object> shop = wftShopDao.obtainShopDetailMSg(source.get("wftId").toString());
		if(CommonHelper.isNullOrEmpty(shop)){
			return returnNullObjectMsg("根据wftId:"+source.get("wftId").toString());
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("shopName", checkTextIsOrNotNull(shop.get("MERCHANT_MERCHNAME")));
		node.put("wftShopName", checkTextIsOrNotNull(shop.get("WFT_SHOP_NAME")));
		node.put("wftId", checkTextIsOrNotNull(source.get("wftId")));
		node.put("shopId", checkTextIsOrNotNull(shop.get("WFT_SHOP_OUTMERCHANTID")));
		node.put("wftIndustry", checkTextIsOrNotNull(shop.get("WFT_SHOP_INDUSTRYID")));
		node.put("wftBank", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKTYPE")));
		node.put("province", checkTextIsOrNotNull(shop.get("WFT_SHOP_PROVINCEID")));
		node.put("city", checkTextIsOrNotNull(shop.get("WFT_SHOP_CITYID")));
		node.put("address", checkTextIsOrNotNull(shop.get("WFT_SHOP_ADDRESS")));
		node.put("principal", checkTextIsOrNotNull(shop.get("WFT_SHOP_PRINCIPAL")));
		node.put("principalEmail", checkTextIsOrNotNull(shop.get("WFT_SHOP_PRINCIPALEMAIL")));
		node.put("bankAccountName", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKACCOUNTNAME")));
		node.put("accountType", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKACCOUNTTYPE")));
		node.put("bankCardNum", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKCARDNUM")));
		node.put("bankId", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKID")));
		node.put("bankProvince", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKCODEPROVICEID")));
		node.put("bankCity", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKCODECITYID")));
		node.put("lhBankCode", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKCODEID")));
		node.put("lhBankName", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKCODENAME")));
		node.put("idCardType", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKIDCARDTYPE")));
		node.put("idCardNo", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKIDCARD")));
		node.put("idCardPhone", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKACCOUNTTEL")));
		node.put("idCardAddress", checkTextIsOrNotNull(shop.get("WFT_SHOP_BANKACCOUNTADDRESS")));
		node.put("wftWXBill", checkTextIsOrNotNull(shop.get("WFT_SHOP_WXBILL")));
		node.put("wftZFBBill", checkTextIsOrNotNull(shop.get("WFT_SHOP_ZFBBILL")));
		node.put("thirdCode", checkTextIsOrNotNull(shop.get("WFT_SHOP_THIRDCODE")));
		node.put("wftBackMess", checkTextIsOrNotNull(shop.get("WFT_SHOP_BACKMESS")));
		node.put("isState", checkTextIsOrNotNull(shop.get("WFT_SHOP_STATE")));
		node.put("backAccountId", checkTextIsOrNotNull(shop.get("WFT_SHOP_WFTACCOUNTID")));
		node.put("wftShopCode", checkTextIsOrNotNull(shop.get("WFT_SHOP_MERCHANTID")));
		return returnSuccessMessage(node);
	}
	
	@Override
	public String obtainPayTypeList(Map<String, Object> source)
			throws Exception {
        List<Map<String, Object>> list =null;
		if(!CommonHelper.isNullOrEmpty(source.get("wftId"))){
			//returnMissParamMessage("wftId");
            list = wftShopDao.obtainPayType(source.get("wftId").toString());
		}
        else
        {
            list = wftShopDao.obtainPayType("");
        }
		//List<Map<String, Object>> list = wftShopDao.obtainPayType(source.get("wftId").toString());
		/****/
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Map<String, Object> map : list){
			ObjectNode nodeOne = mapper.createObjectNode();
            nodeOne.put("payTypeId", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_ID")));
			nodeOne.put("payTypeName", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_NAME")));
			nodeOne.put("payTypeCode", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_CODE")));
			nodeOne.put("payTypeBill", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_BILL")));
			nodeOne.put("payType", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_TYPE")));
			nodeOne.put("thirdCode", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_THIRDCODE")));
			nodeOne.put("backmess", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_BACKMESS")));
			nodeOne.put("backCode", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_WFTCODE")));
			nodeOne.put("backState", checkTextIsOrNotNull(map.get("WFT_PAYTYPE_BACKSTATE")));
			if(map.get("WFT_PAYTYPE_BACKDATE") instanceof Date){
				Date date = (Date)map.get("WFT_PAYTYPE_BACKDATE");
				nodeOne.put("backTime", format.format(date));
			}
            nodeOne.put("wftId", map.get("WFT_PAYTYPE_WFTSHOP_ID").toString());
			array.add(nodeOne);
		}
		ObjectNode node = mapper.createObjectNode();

        node.put("lists", array);
		return returnSuccessMessage(node);
	}
	
	@Override
	public String obtainBankCodeList(Map<String, Object> source)
			throws Exception {
		
		if(CommonHelper.isNullOrEmpty(source.get("curragePage"))
				|| CommonHelper.isNullOrEmpty(source.get("pageSize"))){
			returnMissParamMessage("curragePage或者pageSize");
		}
		int currentPage = Integer.parseInt(source.get("curragePage").toString());
		int pageSize = Integer.parseInt(source.get("pageSize").toString());
		List<Map<String, Object>> list = wftShopDao.obtainBankCodeList(currentPage, pageSize,
				source.get("bankCode"), source.get("bankName"));
		int total = wftShopDao.obtainBankCodeCount(source.get("bankCode"), source.get("bankName"));
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode array = mapper.createArrayNode();
		for(Map<String, Object> map : list){
			ObjectNode nodeOne = mapper.createObjectNode();
			nodeOne.put("bankCode", checkTextIsOrNotNull(map.get("WFT_BANKCODE_BANKCODE")));
			nodeOne.put("bankName", checkTextIsOrNotNull(map.get("WFT_BANKCODE_BANKNAME")));
			array.add(nodeOne);
		}
		ObjectNode node = mapper.createObjectNode();
		node.put("lists", array);
        node.put("curragePage", currentPage);
		node.put("pageSize", pageSize);
		node.put("total", total);
		return returnSuccessMessage(node);
	}

    @Override
    public void exportExcelForList(Map<String, Object> source,HttpServletResponse response) throws Exception
    {
        /**查询列表**/
        List<Map<String, Object>> list = wftShopDao.getWftShopList(source, "exportExcel");
        /**
         * 码表字典（银行）
         */
        Map<String, String> dictionaryData_bank = allpayMerchantDao.obtainMerchantStateFromDictionary("wftBank");
        /**
         * 威富通商户状态
         */
       // Map<String, String> dictionaryData_state = allpayMerchantDao.obtainMerchantStateFromDictionary("");

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        if(!list.isEmpty()){
            List<String[]> exportList = new ArrayList<String[]>();
            for(Map<String, Object> map : list){
                String[] export = new String[23];
                if (!CommonHelper.isNullOrEmpty(source.get("isZFTypeEx"))) {
                    if ("1".equals(String.valueOf(source.get("isZFTypeEx")))) {
                        if ("未完成".equals(String.valueOf(map.get("PAYNAME")))) {
                            export[0] = checkTextIsOrNotNull(map.get("MERCHANT_MERCHNAME"));
                            export[1] = checkTextIsOrNotNull(map.get("ORGANIZATION_NAME"));
                            export[2] = checkTextIsOrNotNull(map.get("AGENT_NAME"));
                            export[3] = checkTextIsOrNotNull(map.get("WFT_SHOP_NAME"));
                            export[4] = checkTextIsOrNotNull(map.get("WFT_SHOP_MERCHANTID"));
                            if(map.get("WFT_SHOP_BANKTYPE")==null || "".equals(map.get("WFT_SHOP_BANKTYPE")))
                            {
                                export[5] = "";
                            }
                            else
                            {
                                export[5] = dictionaryData_bank.get(map.get("WFT_SHOP_BANKTYPE"));
                            }
                            if(map.get("WFT_SHOP_STATE")!=null && !"".equals(map.get("WFT_SHOP_STATE")))
                            {
                                int value = Integer.parseInt(map.get("WFT_SHOP_STATE").toString());
                                if(value==1)
                                {
                                    export[6] ="启用";
                                }
                                else if(value==0)
                                {
                                    export[6] = "停用";
                                }
                                else {
                                    export[6] ="启用";
                                }
                            }
                            else
                            {
                                export[6] = "停用";
                            }

                            export[7] =  checkTextIsOrNotNull(map.get("INDUSTRYNAME"));//威富通行业类别
                            export[8] =  checkTextIsOrNotNull(map.get("WFT_SHOP_ZFBBILL"));
                            export[9] =  checkTextIsOrNotNull(map.get("WFT_SHOP_WXBILL"));
                            export[10] = "未完成";
                            export[11] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTNAME"));
                            export[12] = checkTextIsOrNotNull(map.get("BANKNAME"));//开户银行
                            if("1".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"))))
                            {
                                export[13] = "个人";
                            }
                            else if("2".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"))))
                            {
                                export[13] = "企业";
                            }
                            //export[13] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"));//账户类型 1、个人 2、企业

                            if("1".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"))))
                            {
                                export[14] = "身份证";
                            }
                            else if("2".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"))))
                            {
                                export[14] = "护照";
                            }

                            //export[14] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"));//持卡人证件类型 1、身份证 2、护照
                            export[15] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKCODEID"));//联行号
                            export[16] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKCODENAME"));//开户支行
                            export[17] = checkTextIsOrNotNull(map.get("ALLPAY_UPDATER"));//录入者
                            export[18] = checkTextIsOrNotNull(map.get("ALLPAY_UPDATETIME"));//录入时间
                            export[19] = checkTextIsOrNotNull(map.get("WFT_SHOP_BACKDATE"));//商户号返回时间
                            export[20] = checkTextIsOrNotNull(map.get("WFT_SHOP_BACKMESS"));//威富通信息
                            export[21] = checkTextIsOrNotNull(map.get("PROVICENAME"));//省
                            export[22] = checkTextIsOrNotNull(map.get("CITYNAME"));//市
                            exportList.add(export);
                        }

                    } else
                    {
                        export[0] = checkTextIsOrNotNull(map.get("MERCHANT_MERCHNAME"));
                        export[1] = checkTextIsOrNotNull(map.get("ORGANIZATION_NAME"));
                        export[2] = checkTextIsOrNotNull(map.get("AGENT_NAME"));
                        export[3] = checkTextIsOrNotNull(map.get("WFT_SHOP_NAME"));
                        export[4] = checkTextIsOrNotNull(map.get("WFT_SHOP_MERCHANTID"));
                        if(map.get("WFT_SHOP_BANKTYPE")==null || "".equals(map.get("WFT_SHOP_BANKTYPE")))
                        {
                            export[5] = "";
                        }
                        else
                        {
                            export[5] = dictionaryData_bank.get(map.get("WFT_SHOP_BANKTYPE"));
                        }
                        if(map.get("WFT_SHOP_STATE")!=null && !"".equals(map.get("WFT_SHOP_STATE")))
                        {
                            int value = Integer.parseInt(map.get("WFT_SHOP_STATE").toString());
                            if(value==1)
                            {
                                export[6] ="启用";
                            }
                            else if(value==0)
                            {
                                export[6] = "停用";
                            }
                            else {
                                export[6] ="启用";
                            }
                        }
                        else
                        {
                            export[6] = "停用";
                        }

                        export[7] =  checkTextIsOrNotNull(map.get("INDUSTRYNAME"));//威富通行业类别
                        export[8] =  checkTextIsOrNotNull(map.get("WFT_SHOP_ZFBBILL"));
                        export[9] =  checkTextIsOrNotNull(map.get("WFT_SHOP_WXBILL"));
                        export[10] = "已完成";
                        export[11] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTNAME"));
                        export[12] = checkTextIsOrNotNull(map.get("BANKNAME"));//开户银行
                        if("1".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"))))
                        {
                            export[13] = "个人";
                        }
                        else if("2".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"))))
                        {
                            export[13] = "企业";
                        }
                        //export[13] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"));//账户类型 1、个人 2、企业

                        if("1".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"))))
                        {
                            export[14] = "身份证";
                        }
                        else if("2".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"))))
                        {
                            export[14] = "护照";
                        }

                        //export[14] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"));//持卡人证件类型 1、身份证 2、护照
                        export[15] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKCODEID"));//联行号
                        export[16] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKCODENAME"));//开户支行
                        export[17] = checkTextIsOrNotNull(map.get("ALLPAY_UPDATER"));//录入者
                        export[18] = checkTextIsOrNotNull(map.get("ALLPAY_UPDATETIME"));//录入时间
                        export[19] = checkTextIsOrNotNull(map.get("WFT_SHOP_BACKDATE"));//商户号返回时间
                        export[20] = checkTextIsOrNotNull(map.get("WFT_SHOP_BACKMESS"));//威富通信息
                        export[21] = checkTextIsOrNotNull(map.get("PROVICENAME"));//省
                        export[22] = checkTextIsOrNotNull(map.get("CITYNAME"));//市
                        exportList.add(export);
                    }
                }
                else
                {
                    export[0] = checkTextIsOrNotNull(map.get("MERCHANT_MERCHNAME"));
                    export[1] = checkTextIsOrNotNull(map.get("ORGANIZATION_NAME"));
                    export[2] = checkTextIsOrNotNull(map.get("AGENT_NAME"));
                    export[3] = checkTextIsOrNotNull(map.get("WFT_SHOP_NAME"));
                    export[4] = checkTextIsOrNotNull(map.get("WFT_SHOP_MERCHANTID"));
                    if(map.get("WFT_SHOP_BANKTYPE")==null || "".equals(map.get("WFT_SHOP_BANKTYPE")))
                    {
                        export[5] = "";
                    }
                    else
                    {
                        export[5] = dictionaryData_bank.get(map.get("WFT_SHOP_BANKTYPE"));
                    }
                    if(map.get("WFT_SHOP_STATE")!=null && !"".equals(map.get("WFT_SHOP_STATE")))
                    {
                        int value = Integer.parseInt(map.get("WFT_SHOP_STATE").toString());
                        if(value==1)
                        {
                            export[6] ="启用";
                        }
                        else if(value==0)
                        {
                            export[6] = "停用";
                        }
                        else {
                            export[6] ="启用";
                        }
                    }
                    else
                    {
                        export[6] = "停用";
                    }



                    export[7] =  checkTextIsOrNotNull(map.get("INDUSTRYNAME"));//威富通行业类别
                    export[8] =  checkTextIsOrNotNull(map.get("WFT_SHOP_ZFBBILL"));
                    export[9] =  checkTextIsOrNotNull(map.get("WFT_SHOP_WXBILL"));
                    export[10] = checkTextIsOrNotNull(map.get("PAYNAME"));
                    export[11] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTNAME"));
                    export[12] = checkTextIsOrNotNull(map.get("BANKNAME"));//开户银行
                    if("1".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"))))
                    {
                        export[13] = "个人";
                    }
                    else if("2".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"))))
                    {
                        export[13] = "企业";
                    }
                    //export[13] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKACCOUNTTYPE"));//账户类型 1、个人 2、企业

                    if("1".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"))))
                    {
                        export[14] = "身份证";
                    }
                    else if("2".equals(checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"))))
                    {
                        export[14] = "护照";
                    }

                    //export[14] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKIDCARDTYPE"));//持卡人证件类型 1、身份证 2、护照
                    export[15] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKCODEID"));//联行号
                    export[16] = checkTextIsOrNotNull(map.get("WFT_SHOP_BANKCODENAME"));//开户支行
                    export[17] = checkTextIsOrNotNull(map.get("ALLPAY_UPDATER"));//录入者
                    export[18] = checkTextIsOrNotNull(map.get("ALLPAY_UPDATETIME"));//录入时间
                    export[19] = checkTextIsOrNotNull(map.get("WFT_SHOP_BACKDATE"));//商户号返回时间
                    export[20] = checkTextIsOrNotNull(map.get("WFT_SHOP_BACKMESS"));//威富通信息
                    export[21] = checkTextIsOrNotNull(map.get("PROVICENAME"));//省
                    export[22] = checkTextIsOrNotNull(map.get("CITYNAME"));//市
                    exportList.add(export);
                }
            }
            String[] columnName =
                    { "系统商户名", "所属分公司", "所属代理商", "威富通商户名称","威富通商户号", "银行渠道","商户状态","威富通行业类别"
                    ,"支付宝费率","微信费率","支付类型完成状态","结算户名","开户银行","账户类型","持卡人证件类型","联行号"
                    , "开户支行","录入者","录入时间","商户号返回时间","威富通信息","省","市"};
            WriteExcelFile.writeExcel(exportList, "WFT_MERCHANT_LIST", columnName, "WFT_MERCHANT_LIST", outpath, response);
        }
        LogHelper.info("导出返回：" + node);
    }

    @Override
    public String updateWftShopByWFT(Map<String, Object> source) throws Exception
    {
        if(CommonHelper.isNullOrEmpty(source.get("marked")))
        {
            return returnMissParamMessage("marked");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftId"))) {
            return returnMissParamMessage("wftId");
        }
        String market = String.valueOf(source.get("marked"));
        Wft_Shop wftShop = wftShopDao.getWftShopInfoByID(String.valueOf(source.get("wftId")));

        if("0".equals(market))
        {//发送信息
            wftShop.setWft_shop_senddate(new Date());
        }
        if("2".equals(market))
        {//手动修改威富通信息
            wftShop.setWft_shop_merchantid(String.valueOf(source.get("wftShopCode")));//商户号
            wftShop.setWft_shop_secretKey(String.valueOf(source.get("secretKey")));//威富通商户秘钥
            wftShop.setWft_shop_appid(String.valueOf(source.get("appid")));//威富通商户appid
        }
        else
        {//返回信息
            wftShop.setWft_shop_backdate(new Date());
            wftShop.setWft_shop_backmess(String.valueOf(source.get("wftBackMess")));
            wftShop.setWft_shop_merchantid(String.valueOf(source.get("wftShopCode")));
        }
        wftShop.setALLPAY_UPDATETIME(new Date());
        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, wftShop.getALLPAY_LOGRECORD(), CommonHelper.nullToString(source.get("userNameFromBusCookie")));
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");
        wftShop.setALLPAY_UPDATER(userName);
        wftShop.setALLPAY_LOGRECORD(record);
        wftShopDao.insertWftShopInfo(wftShop);
        return returnSuccessMessage();
    }

    @Override
    public String updateWftPayTypeByWFT(Map<String, Object> source) throws Exception
    {
        if(CommonHelper.isNullOrEmpty(source.get("marked")))
        {
            return returnMissParamMessage("marked");
        }
        if(CommonHelper.isNullOrEmpty(source.get("wftPayId")))
        {
            return returnMissParamMessage("wftPayId");
        }
        String market=String.valueOf(source.get("marked"));
        Wft_PayType wftPayType=wftShopDao.getWft_PayTypeByID(String.valueOf(source.get("wftPayId")));
        if("0".equals(market))
        {//发送信息
            wftPayType.setWft_paytype_senddate(new Date());
        }
        else
        {//返回信息
            wftPayType.setWft_paytype_backdate(new Date());
            wftPayType.setWft_paytype_backmess(String.valueOf(source.get("wftBackMess")));
            wftPayType.setWft_paytype_backstate(String.valueOf(source.get("wftBackState")));
        }
        wftPayType.setALLPAY_UPDATETIME(new Date());
        wftPayType.setALLPAY_ISSTART(1);
        wftPayType.setALLPAY_STATE(1);
        wftShopDao.insertWftPayTypeInfo(wftPayType);
        return returnSuccessMessage();
    }

    /**
	 * 
	 * @param shop
	 * @param source
	 * @return
	 */
	private String insertupdateShopForSource(Wft_Shop shop,Map<String, Object> source,boolean must) throws Exception {
		
		//威富通返回账户id
		if(!CommonHelper.isNullOrEmpty(source.get("backAccountId"))){
			shop.setWft_shop_wftaccountid(source.get("backAccountId").toString());
		}else{
			if(must){
				returnMissParamMessage("backAccountId");
			}
		}
		//银行持卡人名称
		if(!CommonHelper.isNullOrEmpty(source.get("bankAccountName"))){
			shop.setWft_shop_bankaccountname(source.get("bankAccountName").toString());
		}else{
			if(must){
				returnMissParamMessage("bankAccountName");
			}
		}
		//账户类型
		if(!CommonHelper.isNullOrEmpty(source.get("accountType"))){
			shop.setWft_shop_bankaccounttype(source.get("accountType").toString());
		}else{
			if(must){
				returnMissParamMessage("accountType");
			}
		}
		//银行卡号
		if(!CommonHelper.isNullOrEmpty(source.get("bankCardNum"))){
			shop.setWft_shop_bankcardnum(source.get("bankCardNum").toString());
		}else{
			if(must){
				returnMissParamMessage("bankCardNum");
			}
		}
		//开户银行
		if(!CommonHelper.isNullOrEmpty(source.get("bankId"))){
			shop.setWft_shop_bankid(source.get("bankId").toString());
		}else{
			if(must){
				returnMissParamMessage("bankId");
			}
		}
		//联行省id
		if(!CommonHelper.isNullOrEmpty(source.get("bankProvince"))){
			shop.setWft_shop_bankcodeproviceid(source.get("bankProvince").toString());
		}else{
			if(must){
				returnMissParamMessage("bankProvince");
			}
		}
		//联行市id
		if(!CommonHelper.isNullOrEmpty(source.get("bankCity"))){
			shop.setWft_shop_bankcodecityid(source.get("bankCity").toString());
		}else{
			if(must){
				returnMissParamMessage("bankCity");
			}
		}
		//联行号
		if(!CommonHelper.isNullOrEmpty(source.get("lhBankCode"))){
			shop.setWft_shop_bankcodeid(source.get("lhBankCode").toString());
		}else{
			if(must){
				returnMissParamMessage("lhBankCode");
			}
		}
		//联行名称
		if(!CommonHelper.isNullOrEmpty(source.get("lhBankName"))){
			shop.setWft_shop_bankcodename(source.get("lhBankName").toString());
		}else{
			if(must){
				returnMissParamMessage("lhBankName");
			}
		}
		//持卡人证件类型
		if(!CommonHelper.isNullOrEmpty(source.get("idCardType"))){
			shop.setWft_shop_bankidcardtype(source.get("idCardType").toString());
		}else{
			if(must){
				returnMissParamMessage("idCardType");
			}
		}
		//持卡人证件号
		if(!CommonHelper.isNullOrEmpty(source.get("idCardNo"))){
			shop.setWft_shop_bankidcard(source.get("idCardNo").toString());
		}else{
			if(must){
				returnMissParamMessage("idCardNo");
			}
		}
		//持卡人手机号
		if(!CommonHelper.isNullOrEmpty(source.get("idCardPhone"))){
			shop.setWft_shop_bankaccounttel(source.get("idCardPhone").toString());
		}else{
			if(must){
				returnMissParamMessage("idCardPhone");
			}
		}
		//持卡人地址
		if(!CommonHelper.isNullOrEmpty(source.get("idCardAddress"))){
			shop.setWft_shop_bankAccountAddress(source.get("idCardAddress").toString());
		}else{
			if(must){
				returnMissParamMessage("idCardAddress");
			}
		}
        shop.setALLPAY_ISSTART(1);
        shop.setALLPAY_STATE(1);
        shop.setALLPAY_UPDATETIME(new Date());

        JSONObject publicFileds = CommonHelper.getPublicFileds(MsgAndCode.OPERATION_UPDATE, shop.getALLPAY_LOGRECORD(),CommonHelper.nullToString(source.get("userNameFromBusCookie")));
        String userName = publicFileds.getString("userName");
        Date now = CommonHelper.getStringToDate(publicFileds.getString("now"), "yyyy-MM-dd HH:mm:ss");
        String record = publicFileds.getString("record");
        shop.setALLPAY_UPDATER(userName);
        shop.setALLPAY_LOGRECORD(record);


        wftShopDao.insertWftShopInfo(shop);
		return returnSuccessMessage();
	}

	/**
	 * 
	 * @param errorMessage
	 * @return
	 */
	private String returnMissParamMessage(String errorMessage){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.PARAM_MISSING_CODE);
		node.put(MsgAndCode.RSP_DESC, errorMessage+MsgAndCode.PARAM_MISSING_MSG);
		return node.toString();
	}
	
	/**
	 * 返回处理成功信息
	 * @return
	 */
	private String returnSuccessMessage(){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
		return node.toString();
	}
	/**
	 * 
	 * @param node
	 * @return
	 */
	private String returnSuccessMessage(ObjectNode node){
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.SUCCESS_CODE);
		node.put(MsgAndCode.RSP_DESC, MsgAndCode.SUCCESS_MESSAGE);
		return node.toString();
	}
   
	/**
	 * 根据参数无法　查询到相应的信息
	 * @param errorMsg
	 * @return
	 */
	private String returnNullObjectMsg(String errorMsg){
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put(MsgAndCode.RSP_CODE, MsgAndCode.CODE_003);
		node.put(MsgAndCode.RSP_DESC, errorMsg+MsgAndCode.CODE_003_MSG);
		return node.toString();
	}
    
	/**
	 * 校验　非空
	 * @param obj
	 * @return
	 */
	private String checkTextIsOrNotNull(Object obj){
		
		if(!CommonHelper.isNullOrEmpty(obj)){
			return obj.toString();
		}
		return "";
	}

}











