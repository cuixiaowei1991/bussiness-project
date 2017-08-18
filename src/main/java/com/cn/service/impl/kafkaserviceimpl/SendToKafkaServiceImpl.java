package com.cn.service.impl.kafkaserviceimpl;

import all.union.tools.basic.StringUtilsHelper;

import com.cn.common.LogHelper;
import com.cn.dao.*;
import com.cn.entity.po.*;
import com.cn.service.SendToKafkaService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cn.common.CommonHelper.formatTime;
import static com.cn.common.ExchangerKafkaHelper.encodeBase64;

import static com.cn.common.ExchangerKafkaHelper.getFileContentToByte;

/**
 * Created by cuixiaowei on 2016/11/24.
 */
@Service
public class SendToKafkaServiceImpl implements SendToKafkaService {
	
    @Autowired
    private AllpayMerchantDao allpayMerchantDao;
    
    @Autowired
    private AllpayStoreDao allpayStoreDao;

    @Autowired
    private TerminalDao terDao;

    @Autowired
    private agentDao ageDao;

    @Autowired
    private tmsMessageDao tmsDao;

    @Autowired
    private posParterDao posParDao;
    @Autowired
    private KafkaProducerServiceImpl kafkaProducerService;
    @Autowired
    private AllpayChannelSetDao channelSetDao;

    @Autowired
    private WftShopDao wftShopDao;

    @Value("${hosturl}")
    private String hosturl;//内网地址
    @Value("${fileUpload}")
    private String fileUpload;//内网地址

    /**
     * 推送商户信息至kafka
     * @param merchantId
     * @return
     */
    public boolean sentMerchantToKafka(String merchantId,String marked) throws Exception {
        LogHelper.info("商户信息推送kafka---->商户id：" + merchantId+"，删除标记marked："+marked);
        if(StringUtilsHelper.isEmpty(merchantId))
        {
            LogHelper.info("================未传入商户id================");
            return false;
        }
        Allpay_Merchant merchant = null;
		try {
			merchant = allpayMerchantDao.obtain(merchantId);
		} catch (Exception ex) {
			//ignore
		}
        if(merchant==null)
        {
            LogHelper.info("================未查到相关商户================");
            return false;
        }
        JSONObject js = new JSONObject();
        //维护kafka商户信息
        js.put("merchid",merchantId);//商户id
        js.put("shopIdNumber",merchant.getMerchant_shopnumber()==null?"":merchant.getMerchant_shopnumber());//商户9位编号
        js.put("merchName",merchant.getMerchant_merchname()==null?"":merchant.getMerchant_merchname());//商户名称
        js.put("merchShortName",merchant.getMerchant_shopshoptname()==null?"":merchant.getMerchant_shopshoptname());//商户简称
        if("del".equals(marked))
        {
            js.put("merchState","0");//商户状态
        }
        else
        {
            if(merchant.getALLPAY_STATE()==1)
            {
                if(merchant.getALLPAY_ISSTART()==1)
                {
                    js.put("merchState","1");
                }else
                {
                    js.put("merchState","0");
                }
            }
            else
            {
                js.put("merchState",merchant.getALLPAY_STATE());//商户状态
            }

        }

        js.put("cdate",formatTime(merchant.getALLPAY_CREATETIME(), "yyyyMMddHHmmss"));//创建日期
        js.put("publishDate","");//发布日期 已作废传空串
        js.put("mark","");//0:平台 1:商户 已作废，传空值
        js.put("codeTicket","");//商户code（生成串码专用） 已作废，传空值
        js.put("merchAddress",merchant.getMerchant_address()==null?"":merchant.getMerchant_address());//商户地址
        js.put("industry",merchant.getMerchant_industry()==null?"":merchant.getMerchant_industry());//行业
        js.put("industryNum",merchant.getMerchant_industrynum()==null?"":merchant.getMerchant_industrynum());//行业代码
        js.put("provinceID",merchant.getMerchant_provinceid()==null?"":merchant.getMerchant_provinceid());//省代码
        js.put("provinceName",merchant.getMerchant_provincename()==null?"":merchant.getMerchant_provincename());//省
        js.put("cityId",merchant.getMerchant_cityid()==null?"":merchant.getMerchant_cityid());//市代码
        js.put("cityName",merchant.getMerchant_cityname()==null?"":merchant.getMerchant_cityname());//市
        js.put("branchCompanyID",merchant.getMerchant_branchcompanyid()==null?"":merchant.getMerchant_branchcompanyid());//所属分公司主键 0：表示没有分公司*
        js.put("branchCompanyName",merchant.getMerchant_branchcompanyname()==null?"":merchant.getMerchant_branchcompanyname());//所属分公司名字
        js.put("imgURL",hosturl+merchant.getMerchant_imgurl());//商户图片logo（内部网址+图片名称）
        js.put("agentId",merchant.getMerchant_agentid()==null?"":merchant.getMerchant_agentid());//代理ID*
        js.put("isBigMarket","");//是否是大集商户,0不是,1是*/已作废，传空值
        js.put("shopQrCode",merchant.getMerchant_shopqrcode()==null?"":merchant.getMerchant_shopqrcode());//pos小票二维码信息
        js.put("shopAdvertising",merchant.getMerchant_shopadvertising()==null?"":merchant.getMerchant_shopadvertising());//pos广告语
        js.put("shopQrStartPrice",merchant.getMerchant_shopqrstartprice());//二维码广告使用起始价格
        js.put("shopQrEndPrice",merchant.getMerchant_shopqrendprice());//二维码广告使用封顶价格
        if(null != merchant.getMerchant_shopqrstartdate()){
            js.put("shopQrStartDate",formatTime(merchant.getMerchant_shopqrstartdate(),"yyyyMMddHHmmss"));//二维码广告使用起始日期(yyyyMMddHHmmss)
        }else{
            js.put("shopQrStartDate","");//二维码广告使用起始日期(yyyyMMddHHmmss)
        }
        if(null != merchant.getMerchant_shopqrenddate()){
            js.put("shopQrEndDate", formatTime(merchant.getMerchant_shopqrenddate(),"yyyyMMddHHmmss"));//二维码广告使用截止日期(yyyyMMddHHmmss)
        }else{
            js.put("shopQrEndDate", "");//二维码广告使用截止日期(yyyyMMddHHmmss)
        }

        LogHelper.info("推送商户kafka信息" + js.toString());
        Boolean sendToKafka=kafkaProducerService.send("apay_merchant",js.toString());
        LogHelper.info("推送商户信息至kafka，返回的结果：" + sendToKafka);
        return sendToKafka;
    }

    /**
     * 推送网点信息至kafka
     * @param storeId
     * @return
     */
    public boolean sentWangDianToKafka(String storeId,String marked) throws Exception
    {
        LogHelper.info("网点信息推送kafka---->网点id：" + storeId+"，删除标记marked："+marked);
        if(StringUtilsHelper.isEmpty(storeId))
        {
            LogHelper.info("================未传入网点id================");
            return false;
        }
        Allpay_Store wangDian = null;
		try {
			wangDian = allpayStoreDao.obtain(storeId);
		} catch (Exception e) {
		}
        if(wangDian==null)
        {
            LogHelper.info("================未查到相关网点================");
            return false;
        }
        JSONObject js_merchant = new JSONObject();
        JSONObject js_wangdian = new JSONObject();
        js_merchant.put("merchid",wangDian.getStore_merchant_id());//商户主键
        js_wangdian.put("wangdian_shopId",wangDian.getStore_id());/**网点主键**/
        js_wangdian.put("wangdian_shopIdNumber",wangDian.getStore_shopidnumber()==null?"":wangDian.getStore_shopidnumber());/**网点9位编号**/
        js_wangdian.put("wangdian_shopNumber", wangDian.getStore_shopnumber()==null ? "" : wangDian.getStore_shopnumber()); /**15位编码（百度是18位的门店号）**/
        js_wangdian.put("wangdian_shopName",wangDian.getStore_shopname()==null?"":wangDian.getStore_shopname());/**网点名称**/
        js_wangdian.put("wangdian_shopShortName",wangDian.getStore_shopshoptname()==null?"":wangDian.getStore_shopshoptname());/**网点简称**/
        js_wangdian.put("wangdian_cdate",formatTime(wangDian.getALLPAY_CREATETIME(),"yyyyMMddHHmmss"));/**网点录入日期**/
        js_wangdian.put("wangdian_shopAddress",wangDian.getStore_address()==null?"":wangDian.getStore_address());/**网点地址**/
        js_wangdian.put("wangdian_provinceName",wangDian.getStore_provincename()==null?"":wangDian.getStore_provincename());/**省份名称**/
        js_wangdian.put("wangdian_provinceID",wangDian.getStore_provinceid()==null?"":wangDian.getStore_provinceid());/**省份ID**/
        js_wangdian.put("wangdian_cityName",wangDian.getStore_cityname()==null?"":wangDian.getStore_cityname()); /**城市名称**/
        js_wangdian.put("wangdian_cityId",wangDian.getStore_cityid()==null?"":wangDian.getStore_cityid());/**城市ID**/
        js_wangdian.put("wangdian_poiId","");/**微信网点id（poiID） 已作废，传空值**/
        js_wangdian.put("wangdian_locationTude",wangDian.getStore_locationtude()==null?"":wangDian.getStore_locationtude());/**网点经纬度(正常格式39.9528,116.3679)**/
        if(wangDian.getStore_businessstarthours()==null || wangDian.getStore_businessstarthours().equals(""))
        {
            js_wangdian.put("wangdian_businessStartHours","");/**营业开始时间**/
        }
        else
        {
            js_wangdian.put("wangdian_businessStartHours",formatTime(wangDian.getStore_businessstarthours(),"HHmmss"));/**营业开始时间**/
        }
        if(wangDian.getStore_businessendhours()==null || wangDian.getStore_businessendhours().equals(""))
        {
            js_wangdian.put("wangdian_businessEndHours","");/**营业结束时间**/
        }
        else
        {
            js_wangdian.put("wangdian_businessEndHours",formatTime(wangDian.getStore_businessendhours(),"HHmmss"));/**营业结束时间**/
        }
        js_wangdian.put("wangdian_customerServicePhone",wangDian.getStore_customerservicephone()==null?"":wangDian.getStore_customerservicephone());/**客服电话**/
        js_wangdian.put("wangdian_shopBaiDuNumber","");/**百度银商号 已作废，传空值**/
        js_wangdian.put("specialServices",wangDian.getStore_specialservices()==null?"":wangDian.getStore_specialservices());/*特色服务*/
        js_wangdian.put("imgFile1","");/*微信网点图片1 （内部网址+图片名称） 已作废，传空值*/
        js_wangdian.put("imgFile2","");/*微信网点图片2 （内部网址+图片名称） 已作废，传空值*/
        js_wangdian.put("imgFile3","");/*微信网点图片3 （内部网址+图片名称） 已作废，传空值*/
        js_wangdian.put("posShopName",wangDian.getStore_posshopname()==null?"":wangDian.getStore_posshopname());/*pos显示网点名称*/
        if("del".equals(marked))
        {
            js_wangdian.put("wangdian_state","0");/*网点状态*/
        }
        else
        {
            js_wangdian.put("wangdian_state",wangDian.getALLPAY_ISSTART());/*网点状态*/
        }

        js_wangdian.put("koubeiID",wangDian.getStore_ReputationId()==null?"":wangDian.getStore_ReputationId());
        js_wangdian.put("wangdian_business","");//所包含的业务（以##分割）已作废，传空值
        js_merchant.put("wangdian", js_wangdian);

        LogHelper.info("推送网点kafka信息：" + js_merchant.toString());
        Boolean result= kafkaProducerService.send("apay_wangdian", js_merchant.toString());
        LogHelper.info("推送网点kafka信息,返回结果：" + result);
        return result;
    }

    /**
     * 推送渠道信息至kafka
     * @param merchantId
     * @return
     */
    public boolean sentQvDaoToKafka(String merchantId,String code) throws Exception
    {
        LogHelper.info("渠道信息推送kafka---->商户id：" + merchantId);
        if(StringUtilsHelper.isEmpty(merchantId))
        {
            LogHelper.info("================未传入商户id================");
            return false;
        }
        Allpay_Merchant merchant = null;
		try {
			merchant = allpayMerchantDao.obtain(merchantId);
		} catch (Exception ex) {
			//ignore
		}
        if(merchant == null)
        {
            LogHelper.info("================未查到相关商户================");
            return false;
        }
        List<Allpay_ChannelSet> channelSets = null;
        try {
            channelSets =  channelSetDao.getChannelsByMerchantId(merchantId,code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null==channelSets || channelSets.size()<=0)
        {
            LogHelper.info("================该商户未查到相关渠道配置================");
            return false;
        }
        JSONObject js_channel = new JSONObject();
        js_channel.put("merchid",merchant.getMerchant_id());/*商户主键UUID*/
        js_channel.put("merchName",merchant.getMerchant_merchname());/*商户名称*/
        js_channel.put("merchShortName",merchant.getMerchant_shopshoptname());/*商户简称*/
        js_channel.put("merchstate",merchant.getALLPAY_STATE());/*商户状态 已作废传空值*/

        //支付渠道工商
        List<Allpay_ChannelSet> list_gsyh = new ArrayList<>();
        //支付渠道微信联鑫付
        List<Allpay_ChannelSet> list_zfwxlxf = new ArrayList<>();
        //支付渠道微信商银信  114
        List<Allpay_ChannelSet> list_zfwxsyx = new ArrayList<>();
        //支付渠道微信创新支付 115
        List<Allpay_ChannelSet> list_zfwxcxzf = new ArrayList<>();
        //支付渠道微信触联 116
        List<Allpay_ChannelSet> list_zfwxcl = new ArrayList<>();
        //支付渠道微信众联享付 112
        List<Allpay_ChannelSet> list_zfwxzl = new ArrayList<>();
        //支付宝威富通 192  二清
        List<Allpay_ChannelSet> list_wft = new ArrayList<>();
        //支付渠道和包众联 120
        List<Allpay_ChannelSet> list_zfydhbzl = new ArrayList<>();
        //支付渠道微信公众号 113
        List<Allpay_ChannelSet> list_zfwxgzh = new ArrayList<>();
        //支付渠道百度钱包 12
        List<Allpay_ChannelSet> list_zfbdqb = new ArrayList<>();
        //支付渠道支付宝联鑫付 14
        List<Allpay_ChannelSet> list_zfzfblxf = new ArrayList<>();
        //支付渠道支付宝商银信 143
        List<Allpay_ChannelSet> list_zfzfbsyx = new ArrayList<>();
        //支付渠道支付宝创新支付 144
        List<Allpay_ChannelSet> list_zfzfbcxzf = new ArrayList<>();
        //支付渠道支付宝触联 145
        List<Allpay_ChannelSet> list_zfzfbcl = new ArrayList<>();
        //支付渠道支付宝触联(餐饮) 146
        List<Allpay_ChannelSet> list_zfzfbclcy = new ArrayList<>();
        //支付渠道支付宝众联 142
        List<Allpay_ChannelSet> list_zfzfbzl = new ArrayList<>();
        //支付渠道京东钱包众联 13
        List<Allpay_ChannelSet> list_zfjdqb = new ArrayList<>();
        //支付渠道京东钱包联鑫付 131
        List<Allpay_ChannelSet> list_zfjdqblxf = new ArrayList<>();
        //支付渠道大众闪惠（15）
        List<Allpay_ChannelSet> list_zfdzsh = new ArrayList<>();
        //支付渠道美团 31
        List<Allpay_ChannelSet> list_zfmt = new ArrayList<>();
        //支付渠道翼支付 16
        List<Allpay_ChannelSet> list_zfyzf = new ArrayList<>();
        //支付渠道支付宝威富通 17
        ArrayList<Allpay_ChannelSet> list_zfzfbwft = new ArrayList<>();
        //支付渠道手Q 18
        List<Allpay_ChannelSet> list_zfqqlxf = new ArrayList<>();
        //支付渠道联通沃支付  191
        List<Allpay_ChannelSet> list_zfltwzf = new ArrayList<>();
        //支付渠道手Q  181
        List<Allpay_ChannelSet> list_zfqqzl = new ArrayList<>();
        //支付渠道移动和包 120
        List<Allpay_ChannelSet> list_zfydhb = new ArrayList<>();
        //营销渠道飞惠 26
        List<Allpay_ChannelSet> list_yxfh = new ArrayList<>();
        //营销渠道支付宝口碑  210
        List<Allpay_ChannelSet> list_yxzfbzl = new ArrayList<>();
        //营销渠道百度众联
        List<Allpay_ChannelSet> list_yxbdzl = new ArrayList<>();
        //线上支付微信  811
        List<Allpay_ChannelSet> list_xswx = new ArrayList<>();
        //线上支付支付宝  812
        List<Allpay_ChannelSet> list_xszfb = new ArrayList<>();
        //线上支付百度  813
        List<Allpay_ChannelSet> list_xsbd = new ArrayList<>();
        //线上支付京东  814
        List<Allpay_ChannelSet> list_xsjd = new ArrayList<>();
        //线上支付翼支付  815
        List<Allpay_ChannelSet> list_xsyzf = new ArrayList<>();
        //团购大众闪惠 （32）
        List<Allpay_ChannelSet> list_tgdzsh = new ArrayList<>();
        //小票打印订制  221
        List<Allpay_ChannelSet> list_yxxpdydz = new ArrayList<>();
        //营销活动列表  222
        List<Allpay_ChannelSet> list_yxyxhdlb = new ArrayList<>();
        //商品管理  223
        List<Allpay_ChannelSet> list_yxspgl = new ArrayList<>();
        //商品销售  224
        List<Allpay_ChannelSet> list_yxspxs = new ArrayList<>();
        //团购美团 33
        List<Allpay_ChannelSet> list_tgmt = new ArrayList<>();
        //银行卡辽宁银商  411
        List<Allpay_ChannelSet> list_yhkln = new ArrayList<>();
        //银行卡成都  414
        List<Allpay_ChannelSet> list_yhkcd = new ArrayList<>();
        //营销 大众点评 215
        List<Allpay_ChannelSet> list_yxdzdp = new ArrayList<>();
        //营销 美团网 216
        List<Allpay_ChannelSet> list_yxmt = new ArrayList<>();
        //彩生活  19
        List<Allpay_ChannelSet> list_qdcsh = new ArrayList<>();
        //邮政优友宝
        List<Allpay_ChannelSet> list_yzuub = new ArrayList<>();

        //海科支付通
        List<Allpay_ChannelSet> list_hkzft = new ArrayList<>();

        for(Allpay_ChannelSet channelSet:channelSets)
        {
            if("193".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道工商
                list_gsyh.add(channelSet);
            }
            if("11".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道微信联鑫付
                list_zfwxlxf.add(channelSet);
            }
            if("114".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道微信商银信  114
                list_zfwxsyx.add(channelSet);
            }
            if("115".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道微信创新支付 115
                list_zfwxcxzf.add(channelSet);
            }
            if("116".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道微信触联 116
                list_zfwxcl.add(channelSet);
            }
            if("112".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道微信众联享付 112
                list_zfwxzl.add(channelSet);
            }
            if("192".equals(channelSet.getChannelset_channel_code()))
            {
                //支付宝威富通 192  二清
                list_wft.add(channelSet);
            }
            if("120".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道和包众联 120
                list_zfydhbzl.add(channelSet);
            }
            if("113".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道微信公众号 113
                list_zfwxgzh.add(channelSet);
            }
            if("12".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道百度钱包 12
                list_zfbdqb.add(channelSet);
            }
            if("14".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道支付宝联鑫付 14
                list_zfzfblxf.add(channelSet);
            }
            if("143".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道支付宝商银信 143
                list_zfzfbsyx.add(channelSet);
            }
            if("144".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道支付宝创新支付 144
                list_zfzfbcxzf.add(channelSet);
            }
            if("145".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道支付宝触联 145
                list_zfzfbcl.add(channelSet);
            }
            if("146".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道支付宝触联(餐饮) 146
                list_zfzfbclcy.add(channelSet);
            }
            if("142".equals(channelSet.getChannelset_channel_code()) || "147".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道支付宝众联 142 南京支付宝口碑 147
                list_zfzfbzl.add(channelSet);
            }
            if("13".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道京东钱包众联 13
                list_zfjdqb.add(channelSet);
            }
            if("131".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道京东钱包联鑫付 131
                list_zfjdqblxf.add(channelSet);
            }
            if("15".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道大众闪惠（15）
                list_zfdzsh.add(channelSet);
            }
            if("31".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道美团 31
                list_zfmt.add(channelSet);
            }
            if("16".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道翼支付 16
                list_zfyzf.add(channelSet);
            }
            if("17".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道支付宝威富通 17
                list_zfzfbwft.add(channelSet);
            }
            if("18".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道手Q 18
                list_zfqqlxf.add(channelSet);
            }
            if("191".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道联通沃支付  191
                list_zfltwzf.add(channelSet);
            }
            if("181".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道手Q  181
                list_zfqqzl.add(channelSet);
            }
            if("120".equals(channelSet.getChannelset_channel_code()))
            {
                //支付渠道移动和包 120
                list_zfydhb.add(channelSet);
            }
            if("26".equals(channelSet.getChannelset_channel_code()))
            {
                //营销渠道飞惠 26
                list_yxfh.add(channelSet);
            }
            if("210".equals(channelSet.getChannelset_channel_code()))
            {
                //营销渠道支付宝口碑  210
                list_yxzfbzl.add(channelSet);
            }
            if("".equals(channelSet.getChannelset_channel_code()))
            {
                //百度众联
            }
            if("811".equals(channelSet.getChannelset_channel_code()))
            {
                //线上支付微信  811
                list_xswx.add(channelSet);
            }
            if("812".equals(channelSet.getChannelset_channel_code()))
            {
                //线上支付支付宝  812
                list_xszfb.add(channelSet);
            }
            if("813".equals(channelSet.getChannelset_channel_code()))
            {
                //线上支付百度  813
                list_xsbd.add(channelSet);
            }
            if("814".equals(channelSet.getChannelset_channel_code()))
            {
                ////线上支付京东  814
                list_xsjd.add(channelSet);
            }
            if("815".equals(channelSet.getChannelset_channel_code()))
            {
                //线上支付翼支付  815
                list_xsyzf.add(channelSet);
            }
            if("32".equals(channelSet.getChannelset_channel_code()))
            {
                //团购大众闪惠 （32）
                list_tgdzsh.add(channelSet);
            }
            if("221".equals(channelSet.getChannelset_channel_code()))
            {
                //小票打印订制  221
                list_yxxpdydz.add(channelSet);
            }
            if("222".equals(channelSet.getChannelset_channel_code()))
            {
                //营销活动列表  222
                list_yxyxhdlb.add(channelSet);
            }
            if("223".equals(channelSet.getChannelset_channel_code()))
            {
                //商品管理  223
                list_yxspgl.add(channelSet);
            }
            if("224".equals(channelSet.getChannelset_channel_code()))
            {
                //商品销售  224
                list_yxspxs.add(channelSet);
            }
            if("33".equals(channelSet.getChannelset_channel_code()))
            {
                //团购美团  33
                list_tgmt.add(channelSet);
            }
            if("411".equals(channelSet.getChannelset_channel_code()))
            {
                //银行卡辽宁银商  411
                list_yhkln.add(channelSet);
            }
            if("414".equals(channelSet.getChannelset_channel_code()))
            {
                //银行卡成都  414
                list_yhkcd.add(channelSet);
            }
            if("215".equals(channelSet.getChannelset_channel_code()))
            {
                //营销 大众点评 215
                list_yxdzdp.add(channelSet);
            }
            if("216".equals(channelSet.getChannelset_channel_code()))
            {
                //营销 美团网 216
                list_yxmt.add(channelSet);
            }
            if("19".equals(channelSet.getChannelset_channel_code()))
            {
                //彩生活  19
                list_qdcsh.add(channelSet);
            }
            if("194".equals(channelSet.getChannelset_channel_code()))
            {
                //邮政UU宝
                list_yzuub.add(channelSet);
            }
            if("413".equals(channelSet.getChannelset_channel_code()))
            {
                //邮政UU宝
                list_hkzft.add(channelSet);
            }
        }
        JSONObject js_gsyh = new JSONObject();//支付渠道工商银行
        if(list_gsyh != null && list_gsyh.size()>0 )
        {
            js_gsyh.put("code","193");
            js_gsyh.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_gsyh)
            {
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_gsyh.put("shopMachId",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_gsyh.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }

                if("crtFile".equals(channelSet.getChannelset_parameter_name()))
                {
                    String fileio="";
                    String file=fileUpload//服务器地址
                            + channelSet.getChannelset_parameter_value();
                    LogHelper.info("crtFile=============="+file);
                    try {
                        fileio=encodeBase64(
                                getFileContentToByte(file));
                    } catch (Exception e) {
                        LogHelper.error(e,"推送工商银行证书文件异常！！！",false);
                        e.printStackTrace();
                    }
                    js_gsyh.put("crtFile",fileio);
                }
                if("keyFile".equals(channelSet.getChannelset_parameter_name()))
                {
                    String fileio="";
                    String file=fileUpload//服务器地址
                            + channelSet.getChannelset_parameter_value();
                    LogHelper.info("keyFile=============="+file);
                    try {
                        fileio=encodeBase64(
                                getFileContentToByte(file));
                    } catch (Exception e) {
                        LogHelper.error(e,"推送工商银行证书文件异常！！！",false);
                        e.printStackTrace();
                    }
                    js_gsyh.put("keyFile",fileio);
                }
                if("keyPW".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_gsyh.put("keyPW",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("gsyh",js_gsyh);
        }
        JSONObject js_zfwxlxf = new JSONObject();//支付渠道微信联鑫付
        if(list_zfwxlxf != null && list_zfwxlxf.size()>0 )
        {
            js_zfwxlxf.put("code","11");
            js_zfwxlxf.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfwxlxf)
            {
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxlxf.put("shopMachId",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxlxf.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            /*if(js_zfwxlxf.getString("state")!=null && "2".equals(js_zfwxlxf.getString("state")))
            {*/
                js_channel.put("zfwxlxf",js_zfwxlxf);
            /*}
            else
            {*/
               // js_channel.put("zfwxlxf",new JSONObject());
            //}

        }
        JSONObject js_zfwxsyx = new JSONObject();//支付渠道微信商银信
        if(list_zfwxsyx != null && list_zfwxsyx.size()>0 )
        {
            js_zfwxsyx.put("code","114");
            js_zfwxsyx.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfwxsyx)
            {
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxsyx.put("shopMachId",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxsyx.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfwxsyx",js_zfwxsyx);
            /*if(js_zfwxsyx.getString("state")!=null && "2".equals(js_zfwxsyx.getString("state")))
            {
                js_channel.put("zfwxsyx",js_zfwxsyx);
            }
            else
            {
                js_channel.put("zfwxsyx",new JSONObject());
            }*/

        }
        JSONObject js_zfwxcxzf = new JSONObject();//支付渠道微信创新支付 115
        if(list_zfwxcxzf != null && list_zfwxcxzf.size()>0 )
        {
            js_zfwxcxzf.put("code","115");
            js_zfwxcxzf.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfwxcxzf)
            {
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxcxzf.put("shopMachId",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxcxzf.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfwxcxzf",js_zfwxcxzf);
            /*if(js_zfwxcxzf.getString("state")!=null&&"2".equals(js_zfwxcxzf.getString("state")))
            {
                js_channel.put("zfwxcxzf",js_zfwxcxzf);
            }
            else
            {
                js_channel.put("zfwxcxzf",new JSONObject());
            }*/

        }
        JSONObject js_zfwxcl = new JSONObject();//支付渠道微信触联
        if(list_zfwxcl != null && list_zfwxcl.size()>0 )
        {
            js_zfwxcl.put("code","116");
            js_zfwxcl.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfwxcl)
            {
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxcl.put("shopMachId",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxcl.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfwxcl",js_zfwxcl);
            /*if(js_zfwxcl.getString("state")!=null&&"2".equals(js_zfwxcl.getString("state")))
            {
                js_channel.put("zfwxcl",js_zfwxcl);
            }
            else
            {
                js_channel.put("zfwxcl",new JSONObject());
            }*/

        }
        JSONObject js_zfwxzl = new JSONObject();//支付渠道微信众联享付 112
        if(list_zfwxzl != null && list_zfwxzl.size()>0 )
        {
            js_zfwxzl.put("code","112");
            js_zfwxzl.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfwxzl)
            {
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxzl.put("shopMachId",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxzl.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfwxzl",js_zfwxzl);
            /*if(js_zfwxzl.getString("state")!=null && "2".equals(js_zfwxzl.getString("state")))
            {
                js_channel.put("zfwxzl",js_zfwxzl);
            }
            else
            {
                js_channel.put("zfwxzl",new JSONObject());
            }*/


        }
        JSONObject js_wft = new JSONObject();//支付宝威富通 17
        if(list_wft != null && list_wft.size()>0 )
        {
            js_wft.put("code","17");
            js_wft.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_wft)
            {
                if("wftShopId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_wft.put("wftShopId",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    if("2".equals(channelSet.getChannelset_parameter_value()))
                    {
                        js_wft.put("state","1");
                    }
                    else
                    {
                        js_wft.put("state","0");
                    }

                }
                if("whichBank".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_wft.put("whichBank",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("zfbBillRate".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_wft.put("zfbBillRate",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("wxBillRate".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_wft.put("wxBillRate",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("wft",js_wft);
        }
        JSONObject js_zfydhbzl = new JSONObject();//支付渠道和包众联 120
        if(list_zfydhbzl != null && list_zfydhbzl.size()>0 )
        {
            js_zfydhbzl.put("code","120");
            js_zfydhbzl.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfydhbzl)
            {
                if("privateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfydhbzl.put("privateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfydhbzl.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfydhbzl.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("description".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfydhbzl.put("description",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfydhbzl",js_zfydhbzl);
        }
        JSONObject js_zfwxgzh = new JSONObject();//支付渠道微信公众号 113
        if(list_zfwxgzh != null && list_zfwxgzh.size()>0 )
        {
            js_zfwxgzh.put("code","113");
            js_zfwxgzh.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfwxgzh)
            {
                if("appID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxgzh.put("appID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxgzh.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("privateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxgzh.put("privateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("certPassword".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxgzh.put("certPassword",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("apiclient_cert.p12".equals(channelSet.getChannelset_parameter_name()))
                {
                    String fileio="";
                    try {
                        fileio=encodeBase64(
                                getFileContentToByte(fileUpload//服务器地址
                                        + channelSet.getChannelset_parameter_value()
                                        .trim()));
                    } catch (Exception e) {
                        LogHelper.error(e,"推送微信公众号证书文件异常！！！",false);
                        e.printStackTrace();
                    }
                    js_zfwxgzh.put("apiclient_cert.p12",fileio);
                }
                if("apiclient_cert.pem".equals(channelSet.getChannelset_parameter_name()))
                {

                    String fileio="";
                    try {
                        fileio=encodeBase64(
                                getFileContentToByte(fileUpload//服务器地址
                                        + channelSet.getChannelset_parameter_value()
                                        .trim()));
                    } catch (Exception e) {
                        LogHelper.error(e,"推送微信公众号证书文件异常！！！",false);
                        e.printStackTrace();
                    }
                    js_zfwxgzh.put("apiclient_cert.pem",fileio);
                }
                if("apiclient_key.pem".equals(channelSet.getChannelset_parameter_name()))
                {
                    String fileio="";
                    try {
                        fileio=encodeBase64(
                                getFileContentToByte(fileUpload//服务器地址
                                        + channelSet.getChannelset_parameter_value()
                                        .trim()));
                    } catch (Exception e) {
                        LogHelper.error(e,"推送微信公众号证书文件异常！！！",false);
                        e.printStackTrace();
                    }
                    js_zfwxgzh.put("apiclient_key.pem",fileio);
                }
                if("rootca.pem".equals(channelSet.getChannelset_parameter_name()))
                {
                    String fileio="";
                    try {
                        fileio=encodeBase64(
                                getFileContentToByte(fileUpload//服务器地址
                                        + channelSet.getChannelset_parameter_value()
                                        .trim()));
                    } catch (Exception e) {
                        LogHelper.error(e,"推送微信公众号证书文件异常！！！",false);
                        e.printStackTrace();
                    }
                    js_zfwxgzh.put("rootca.pem",fileio);
                }
                if("category1".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxgzh.put("category1",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("category2".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxgzh.put("category2",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("category3".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxgzh.put("category3",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfwxgzh.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfwxgzh",js_zfwxgzh);
        }
        JSONObject js_zfbdqb = new JSONObject();//支付渠道百度钱包 12
        if(list_zfbdqb != null && list_zfbdqb.size()>0 )
        {
            js_zfbdqb.put("code","12");
            js_zfbdqb.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfbdqb)
            {

                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfbdqb.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("privateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfbdqb.put("privateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfbdqb.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfbdqb",js_zfbdqb);
        }
        JSONObject js_zfzfblxf = new JSONObject();//支付渠道支付宝联鑫付 14
        if(list_zfzfblxf != null && list_zfzfblxf.size()>0 )
        {
            js_zfzfblxf.put("code","14");
            js_zfzfblxf.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfzfblxf)
            {

                if("appID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfblxf.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("publicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfblxf.put("publicKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPub_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfblxf.put("shopPub_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPri_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfblxf.put("shopPri_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("lxfPID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfblxf.put("lxfPID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfblxf.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }

            }
            js_channel.put("zfzfblxf",js_zfzfblxf);
            /*if(js_zfzfblxf.getString("state")!=null && "2".equals(js_zfzfblxf.getString("state")))
            {
                js_channel.put("zfzfblxf",js_zfzfblxf);
            }
            else
            {
                js_channel.put("zfzfblxf",new JSONObject());
            }*/

        }
        JSONObject js_zfzfbsyx = new JSONObject();//支付渠道支付宝商银信 143
        if(list_zfzfbsyx != null && list_zfzfbsyx.size()>0 )
        {
            js_zfzfbsyx.put("code","143");
            js_zfzfbsyx.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfzfbsyx)
            {

                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbsyx.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("publicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbsyx.put("publicKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPub_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbsyx.put("shopPub_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPri_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbsyx.put("shopPri_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("lxfPID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbsyx.put("lxfPID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbsyx.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfzfbsyx",js_zfzfbsyx);
        }
        JSONObject js_zfzfbcxzf = new JSONObject();//支付渠道支付宝创新支付{ 144
        if(list_zfzfbcxzf != null && list_zfzfbcxzf.size()>0 )
        {
            js_zfzfbcxzf.put("code","144");
            js_zfzfbcxzf.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfzfbcxzf)
            {

                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcxzf.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("publicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcxzf.put("publicKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPub_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcxzf.put("shopPub_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPri_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcxzf.put("shopPri_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("PID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcxzf.put("PID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcxzf.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfzfbcxzf",js_zfzfbcxzf);
        }
        JSONObject js_zfzfbcl = new JSONObject();//支付渠道支付宝触联{ 145
        if(list_zfzfbcl != null && list_zfzfbcl.size()>0 )
        {
            js_zfzfbcl.put("code","145");
            js_zfzfbcl.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfzfbcl)
            {

                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcl.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("publicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcl.put("publicKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPub_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcl.put("shopPub_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPri_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcl.put("shopPri_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("PID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcl.put("PID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbcl.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfzfbcl",js_zfzfbcl);
        }
        JSONObject js_zfzfbclcy = new JSONObject();//支付渠道支付宝触联(餐饮) 146
        if(list_zfzfbclcy != null && list_zfzfbclcy.size()>0 )
        {
            js_zfzfbclcy.put("code","146");
            js_zfzfbclcy.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfzfbclcy)
            {

                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbclcy.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("publicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbclcy.put("publicKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPub_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbclcy.put("shopPub_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPri_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbclcy.put("shopPri_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("PID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbclcy.put("PID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbclcy.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfzfbclcy",js_zfzfbclcy);
        }
        JSONObject js_zfzfbzl = new JSONObject();//支付渠道支付宝众联 142
        if(list_zfzfbzl != null && list_zfzfbzl.size()>0 )
        {
            js_zfzfbzl.put("code",code);
            js_zfzfbzl.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfzfbzl)
            {

                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("myPID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("myPID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("shopPID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("publicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("publicKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPub_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("shopPub_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopPri_key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("shopPri_key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("tokenState".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("tokenState",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("token".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("token",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbzl.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfzfbzl",js_zfzfbzl);
            /*if(js_zfzfbzl.getString("state")!=null && "2".equals(js_zfzfbzl.getString("state")))
            {
                js_channel.put("zfzfbzl",js_zfzfbzl);
            }
            else
            {
                js_channel.put("zfzfbzl",new JSONObject());
            }*/

        }
        JSONObject js_zfjdqb = new JSONObject();//支付渠道京东钱包众联 13
        if(list_zfjdqb != null && list_zfjdqb.size()>0 )
        {
            js_zfjdqb.put("code","13");
            js_zfjdqb.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfjdqb)
            {

                /*if("privateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("privateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }*/
                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("appID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("appID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("appIDB".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("appIDB",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("h5AppID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("h5AppID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("h5Securtykey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("h5Securtykey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("h5PublicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("h5PublicKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("h5PrivateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("h5PrivateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("securtykeyZ".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("securtykeyZ",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                    js_zfjdqb.put("privateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("securtykeyB".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("securtykeyB",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqb.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfjdqb",js_zfjdqb);
            /*if(js_zfjdqb.getString("state")!=null&&"2".equals(js_zfjdqb.getString("state")))
            {
                js_channel.put("zfjdqb",js_zfjdqb);
            }
            else
            {
                js_channel.put("zfjdqb",new JSONObject());
            }*/

        }
        JSONObject js_zfjdqblxf = new JSONObject();//支付渠道京东钱包联鑫付 131
        if(list_zfjdqblxf != null && list_zfjdqblxf.size()>0 )
        {
            int count =0;
            js_zfjdqblxf.put("code","131");
            js_zfjdqblxf.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfjdqblxf)
            {
                /*if("privateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("privateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }*/

                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("appID".equals(channelSet.getChannelset_parameter_name()))
                {

                    js_zfjdqblxf.put("appID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("appIDB".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("appIDB",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("h5AppID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("h5AppID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("h5Securtykey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("h5Securtykey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("h5PublicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("h5PublicKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("h5PrivateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("h5PrivateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("securtykeyZ".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("securtykeyZ",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                    js_zfjdqblxf.put("privateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("securtykeyB".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("securtykeyB",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }


                //京东2.0
                if("rsaPrivateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    if(channelSet.getChannelset_parameter_value()!=null && !channelSet.getChannelset_parameter_value().equals(""))
                    {
                        count++;
                    }
                }
                if("rsaPublicKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    if(channelSet.getChannelset_parameter_value()!=null && !channelSet.getChannelset_parameter_value().equals(""))
                    {
                        count++;
                    }
                }
                if("desKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    if(channelSet.getChannelset_parameter_value()!=null && !channelSet.getChannelset_parameter_value().equals(""))
                    {
                        count++;
                    }
                }
                if("rsaPrivateKeyB".equals(channelSet.getChannelset_parameter_name()))
                {
                    if(channelSet.getChannelset_parameter_value()!=null && !channelSet.getChannelset_parameter_value().equals(""))
                    {
                        count++;
                    }
                }
                if("rsaPublicKeyB".equals(channelSet.getChannelset_parameter_name()))
                {
                    if(channelSet.getChannelset_parameter_value()!=null && !channelSet.getChannelset_parameter_value().equals(""))
                    {
                        count++;
                    }
                }
                if("desKeyB".equals(channelSet.getChannelset_parameter_name()))
                {
                    if(channelSet.getChannelset_parameter_value()!=null && !channelSet.getChannelset_parameter_value().equals(""))
                    {
                        count++;
                    }
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfjdqblxf.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            if(count==6)
            {
                LogHelper.info("进入京东2.0渠道配置推送！！！！！");
                js_zfjdqblxf=new JSONObject();
                for(Allpay_ChannelSet channelSet : list_zfjdqblxf)
                {
                    if ("rsaPrivateKey".equals(channelSet.getChannelset_parameter_name())) {
                        js_zfjdqblxf.put("rsaPrivateKey", channelSet.getChannelset_parameter_value()
                                == null ? "" : channelSet.getChannelset_parameter_value());
                    }
                    if ("desKeyB".equals(channelSet.getChannelset_parameter_name())) {
                        js_zfjdqblxf.put("desKeyB", channelSet.getChannelset_parameter_value()
                                == null ? "" : channelSet.getChannelset_parameter_value());
                    }
                    if ("rsaPublicKeyB".equals(channelSet.getChannelset_parameter_name())) {
                        js_zfjdqblxf.put("rsaPublicKeyB", channelSet.getChannelset_parameter_value()
                                == null ? "" : channelSet.getChannelset_parameter_value());
                    }
                    if ("rsaPrivateKeyB".equals(channelSet.getChannelset_parameter_name())) {
                        js_zfjdqblxf.put("rsaPrivateKeyB", channelSet.getChannelset_parameter_value()
                                == null ? "" : channelSet.getChannelset_parameter_value());
                    }
                    if ("desKey".equals(channelSet.getChannelset_parameter_name())) {
                        js_zfjdqblxf.put("desKey", channelSet.getChannelset_parameter_value()
                                == null ? "" : channelSet.getChannelset_parameter_value());
                    }
                    if ("rsaPublicKey".equals(channelSet.getChannelset_parameter_name())) {
                        js_zfjdqblxf.put("rsaPublicKey", channelSet.getChannelset_parameter_value()
                                == null ? "" : channelSet.getChannelset_parameter_value());
                    }
                    if ("rsaPrivateKey".equals(channelSet.getChannelset_parameter_name())) {
                        js_zfjdqblxf.put("rsaPrivateKey", channelSet.getChannelset_parameter_value()
                                == null ? "" : channelSet.getChannelset_parameter_value());
                    }
                    if("state".equals(channelSet.getChannelset_parameter_name()))
                    {
                        js_zfjdqblxf.put("state",channelSet.getChannelset_parameter_value()
                                ==null?"":channelSet.getChannelset_parameter_value());
                    }
                }
            }
            js_channel.put("zfjdqblxf",js_zfjdqblxf);
            /*if(js_zfjdqblxf.getString("state")!=null&&"2".equals(js_zfjdqblxf.getString("state")))
            {
                js_channel.put("zfjdqblxf",js_zfjdqblxf);
            }
            else
            {
                js_channel.put("zfjdqblxf",new JSONObject());
            }*/

        }
        JSONObject js_zfdzsh = new JSONObject();//支付渠道大众闪惠（15）
        if(list_zfdzsh != null && list_zfdzsh.size()>0 )
        {
            js_zfdzsh.put("code","15");
            js_zfdzsh.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfdzsh)
            {

                if("privateKey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfdzsh.put("privateKey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("appid".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfdzsh.put("appid",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfdzsh.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfdzsh",js_zfdzsh);
        }
        JSONObject js_zfmt = new JSONObject();//支付渠道美团 31
        if(list_zfmt != null && list_zfmt.size()>0 )
        {
            js_zfmt.put("code","31");
            js_zfmt.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfmt)
            {

                if("appSecret".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfmt.put("appSecret",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfmt.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("description".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfmt.put("description",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfmt.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfmt",js_zfmt);
        }
        JSONObject js_zfyzf = new JSONObject();//支付渠道翼支付 16
        if(list_zfyzf != null && list_zfyzf.size()>0 )
        {
            js_zfyzf.put("code","16");
            js_zfyzf.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfyzf)
            {

                if("securekey".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfyzf.put("securekey",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("mchID".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfyzf.put("mchID",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("interfacePassword".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfyzf.put("interfacePassword",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("accountNo".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfyzf.put("accountNo",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("cashierChannelNo".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfyzf.put("cashierChannelNo",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("securekeyB".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfyzf.put("securekeyB",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfyzf.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfyzf",js_zfyzf);
        }
        JSONObject js_zfzfbwft = new JSONObject();//支付渠道支付宝威富通 17
        if(list_zfzfbwft != null && list_zfzfbwft.size()>0 )
        {
            js_zfzfbwft.put("code","17");
            js_zfzfbwft.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfzfbwft)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfzfbwft.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfzfbwft",js_zfzfbwft);
        }
        JSONObject js_zfqqlxf = new JSONObject();//支付渠道手Q 18
        if(list_zfqqlxf != null && list_zfqqlxf.size()>0 )
        {
            js_zfqqlxf.put("code","18");
            js_zfqqlxf.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfqqlxf)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfqqlxf.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfqqlxf.put("mch_id",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfqqlxf",js_zfqqlxf);
            /*if(js_zfqqlxf.getString("state")!=null&&"2".equals(js_zfqqlxf.getString("state")))
            {
                js_channel.put("zfqqlxf",js_zfqqlxf);
            }
            else
            {
                js_channel.put("zfqqlxf",new JSONObject());
            }*/
            //js_channel.put("zfqqlxf",js_zfqqlxf);
        }
        JSONObject js_zfltwzf = new JSONObject();//支付渠道联通沃支付  191
        if(list_zfltwzf != null && list_zfltwzf.size()>0 )
        {
            js_zfltwzf.put("code","191");
            js_zfltwzf.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfltwzf)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfltwzf.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfltwzf.put("shopMachId",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfltwzf",js_zfltwzf);
        }
        JSONObject js_zfqqzl = new JSONObject();//支付渠道手Q  181
        if(list_zfqqzl != null && list_zfqqzl.size()>0 )
        {
            js_zfqqzl.put("code","181");
            js_zfqqzl.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfqqzl)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfqqzl.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopMachId".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfqqzl.put("mch_id",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfqqzl",js_zfqqzl);
            /*if(js_zfqqzl.getString("state")!=null&&"2".equals(js_zfqqzl.getString("state")))
            {
                js_channel.put("zfqqzl",js_zfqqzl);
            }
            else
            {
                js_channel.put("zfqqzl",new JSONObject());
            }*/
            //js_channel.put("zfqqzl",js_zfqqzl);
        }
        JSONObject js_zfydhb = new JSONObject();//支付渠道移动和包 120
        if(list_zfydhb != null && list_zfydhb.size()>0 )
        {
            js_zfydhb.put("code","120");
            js_zfydhb.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfydhb)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_zfydhb.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("zfydhb",js_zfydhb);
        }
        JSONObject js_yxfh = new JSONObject();//营销渠道飞惠 26
        if(list_yxfh != null && list_yxfh.size()>0 )
        {
            js_yxfh.put("code","26");
            js_yxfh.put("choose","1");
            js_channel.put("yxfh",js_yxfh);
        }
        JSONObject js_yxzfbzl = new JSONObject();//营销渠道支付宝口碑  210
        if(list_yxzfbzl != null && list_yxzfbzl.size()>0 )
        {
            js_yxzfbzl.put("code","210");
            js_yxzfbzl.put("choose","1");
            js_channel.put("yxzfbzl",js_yxzfbzl);
        }
        JSONObject js_yxbdzl = new JSONObject();//营销渠道百度众联
        if(list_yxbdzl != null && list_yxbdzl.size()>0 )
        {
            js_yxbdzl.put("code","");
            js_yxbdzl.put("choose","1");
            js_channel.put("yxbdzl",js_yxbdzl);
        }
        JSONObject js_xswx = new JSONObject();//线上支付微信  811
        if(list_xswx != null && list_xswx.size()>0 )
        {
            js_xswx.put("code","811");
            js_xswx.put("choose","1");
            js_channel.put("xswx",js_xswx);
        }
        JSONObject js_xszfb = new JSONObject();//线上支付支付宝  812
        if(list_xszfb != null && list_xszfb.size()>0 )
        {
            js_xszfb.put("code","812");
            js_xszfb.put("choose","1");
            js_channel.put("xszfb",js_xszfb);
        }
        JSONObject js_xsbd = new JSONObject();//线上支付百度  813
        if(list_xsbd != null && list_xsbd.size()>0 )
        {
            js_xsbd.put("code","813");
            js_xsbd.put("choose","1");
            js_channel.put("xsbd",js_xsbd);
        }
        JSONObject js_xsjd = new JSONObject();//线上支付京东  814
        if(list_xsjd != null && list_xsjd.size()>0 )
        {
            js_xsjd.put("code","814");
            js_xsjd.put("choose","1");
            js_channel.put("xsjd",js_xsjd);
        }
        JSONObject js_xsyzf = new JSONObject();//线上支付翼支付  815
        if(list_xsyzf != null && list_xsyzf.size()>0 )
        {
            js_xsyzf.put("code","815");
            js_xsyzf.put("choose","1");
            js_channel.put("xsyzf",js_xsyzf);
        }
        JSONObject js_tgdzsh = new JSONObject();//团购大众闪惠 （32）
        if(list_tgdzsh != null && list_tgdzsh.size()>0 )
        {
            js_tgdzsh.put("code","32");
            js_tgdzsh.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_zfqqzl)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_tgdzsh.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("tgdzsh",js_tgdzsh);
        }
        JSONObject js_yxxpdydz = new JSONObject();//小票打印订制  221
        if(list_yxxpdydz != null && list_yxxpdydz.size()>0 )
        {
            js_yxxpdydz.put("code","221");
            js_yxxpdydz.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_yxxpdydz)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_yxxpdydz.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("yxxpdydz",js_yxxpdydz);
        }
        JSONObject js_yxyxhdlb = new JSONObject();//营销活动列表  222
        if(list_yxyxhdlb != null && list_yxyxhdlb.size()>0 )
        {
            js_yxyxhdlb.put("code","222");
            js_yxyxhdlb.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_yxyxhdlb)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_yxyxhdlb.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("yxyxhdlb",js_yxyxhdlb);
        }
        JSONObject js_yxspgl = new JSONObject();//商品管理  223
        if(list_yxspgl != null && list_yxspgl.size()>0 )
        {
            js_yxspgl.put("code","223");
            js_yxspgl.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_yxspgl)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_yxspgl.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("yxspgl",js_yxspgl);
        }
        JSONObject js_yxspxs = new JSONObject();//商品销售  224
        if(list_yxspxs != null && list_yxspxs.size()>0 )
        {
            js_yxspxs.put("code","224");
            js_yxspxs.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_yxspxs)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_yxspxs.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("yxspxs",js_yxspxs);
        }
        JSONObject js_tgmt = new JSONObject();//团购美团 33
        if(list_tgmt != null && list_tgmt.size()>0 )
        {
            js_tgmt.put("code","33");
            js_tgmt.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_tgmt)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_tgmt.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("tgmt",js_tgmt);
        }
        JSONObject js_yhkln = new JSONObject();//银行卡辽宁银商  411
        if(list_yhkln != null && list_yhkln.size()>0 )
        {
            js_yhkln.put("code","411");
            js_yhkln.put("choose","1");
            js_channel.put("yhkln",js_yhkln);
        }
        JSONObject js_yhkcd = new JSONObject();//银行卡成都  414
        if(list_yhkcd != null && list_yhkcd.size()>0 )
        {
            js_yhkcd.put("code","414");
            js_yhkcd.put("choose","1");
            js_channel.put("yhkcd",js_yhkcd);
        }
        JSONObject js_yxdzdp = new JSONObject();//营销 大众点评 215
        if(list_yxdzdp != null && list_yxdzdp.size()>0 )
        {
            js_yxdzdp.put("code","215");
            js_yxdzdp.put("choose","1");
            js_channel.put("yxdzdp",js_yxdzdp);
        }
        JSONObject js_yxmt = new JSONObject();//营销 美团网 216
        if(list_yxmt != null && list_yxmt.size()>0 )
        {
            js_yxmt.put("code","216");
            js_yxmt.put("choose","1");
            js_channel.put("yxmt",js_yxmt);
        }
        JSONObject js_qdcsh = new JSONObject();//彩生活  19
        if(list_qdcsh != null && list_qdcsh.size()>0 )
        {
            js_qdcsh.put("code","19");
            js_qdcsh.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_qdcsh)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_qdcsh.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("shopIdNumber".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_qdcsh.put("shopIdNumber",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }

            js_channel.put("qdcsh",js_qdcsh);
        }

        JSONObject js_yzuu = new JSONObject();//邮政优友宝
        if(list_yzuub != null && list_yzuub.size()>0 )
        {
            js_yzuu.put("code","194");
            js_yzuu.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_yzuub)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_yzuu.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("merno".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_yzuu.put("merno",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("app_id".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_yzuu.put("app_id",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("key".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_yzuu.put("key",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("uupay",js_yzuu);
        }

        JSONObject js_hkzft = new JSONObject();//海科支付通
        if(list_hkzft != null && list_hkzft.size()>0 )
        {
            js_yzuu.put("code","413");
            js_yzuu.put("choose","1");
            for(Allpay_ChannelSet channelSet : list_hkzft)
            {
                if("state".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("state",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("bank".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("bank",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("id".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("id",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("bankname".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("bankname",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("merchcode".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("merchcode",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("channelcode".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("channelcode",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("merchant".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("merchant",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("terminal".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("terminal",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("bankcode".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("bankcode",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("bankprovince".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("bankprovince",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("bankcity".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("bankcity",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("bankcounty".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("bankcounty",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("rate".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("rate",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("bankoutlet".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("bankoutlet",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
                if("acounttype".equals(channelSet.getChannelset_parameter_name()))
                {
                    js_hkzft.put("acounttype",channelSet.getChannelset_parameter_value()
                            ==null?"":channelSet.getChannelset_parameter_value());
                }
            }
            js_channel.put("hkzft",js_hkzft);
        }

        LogHelper.info("推送渠道  kafka信息：" + js_channel.toString());
        Boolean result=kafkaProducerService.send("apay_qvdao", js_channel.toString());
        LogHelper.info("推送渠道 kafka信息,返回结果：" + result);
        return result;
    }

    /**
     * 推送POS信息至kafka
     * @param posid
     * @return
     */
    public boolean sendApayPosToKafka(String posid,String marked) throws Exception
    {
        LogHelper.info("POS信息推送kafka---->POS_id：" + posid+"删除标记marked："+marked);
        if(StringUtilsHelper.isEmpty(posid))
        {
            LogHelper.info("================未传入POSid================");
            return false;
        }
        Allpay_Terminal terminal=terDao.getTerminal(posid);
        if(terminal==null)
        {
            LogHelper.info("================未查到相关POS信息================");
            return false;
        }
        Allpay_Store store=null;

        try {
             store=allpayStoreDao.obtain(terminal.getTerminal_store_id());
        } catch (Exception e) {
            LogHelper.error(e, "未查到Pos对应门店信息，门店ID：" + terminal.getTerminal_store_id(), false);
            e.printStackTrace();
            return false;
        }
        Allpay_Merchant merchant=null;
        try {
            merchant=allpayMerchantDao.obtain(terminal.getTerminal_merchant_id());
        } catch (Exception e) {
            LogHelper.error(e, "未查到Pos对应商户信息，商户ID：" + terminal.getTerminal_merchant_id(), false);
            e.printStackTrace();
            return false;
        }
        JSONObject js_terminal = new JSONObject();
        js_terminal.put("posID",terminal.getTerminal_posid());/*主键*/
        js_terminal.put("merchCode",terminal.getTerminal_merchcode()==null?"":terminal.getTerminal_merchcode());/*商户编号  （shopNumber）*/
        js_terminal.put("shopCode",terminal.getTerminal_store_id()==null?"":terminal.getTerminal_store_id());/*门店ID UUID*/
        js_terminal.put("shopIdNumber",store.getStore_shopidnumber()==null?"":store.getStore_shopidnumber());/*门店数字ID（暂时大众点评用）*/
        js_terminal.put("termCode",terminal.getTerminal_termcode()==null?"":terminal.getTerminal_termcode());/*终端编号*/
        /*if(terminal.getALLPAY_ISSTART()==0)
        {
            js_terminal.put("status","1");*//*//**//*状态，0启用1停用*//*
        }
        else
        {
            js_terminal.put("status","0");*//*//**//*状态，0启用1停用*//*
        }*/
        js_terminal.put("status",terminal.getALLPAY_ISSTART());/*状态，1启用0停用*/
        if("3".equals(terminal.getTerminal_postype()))
        {
            js_terminal.put("permission","1");/*菜单权限*/
        }
        else
        {
            js_terminal.put("permission",terminal.getTerminal_permission()==null?"":terminal.getTerminal_permission());/*菜单权限*/
        }

        js_terminal.put("createDate",formatTime(terminal.getALLPAY_CREATETIME(),"yyyyMMddHHmmss"));/*创建时间*/
        js_terminal.put("posType",terminal.getTerminal_postype());/*1：pos  2:软pos,3虚拟终端(统一付款码) 4:pc pos*/
        js_terminal.put("posNote",terminal.getTerminal_posnote()==null?"":terminal.getTerminal_posnote());/*备注*/
        js_terminal.put("shopName",merchant.getMerchant_shopshoptname()==null?"":merchant.getMerchant_shopshoptname());/*商户简称*/
        js_terminal.put("branchId",terminal.getTerminal_branchid()==null?"":terminal.getTerminal_branchid());/*pos所属分公司*/
        js_terminal.put("posShopName",store.getStore_posshopname()==null?"":store.getStore_posshopname());/*pos显示网点名称*/
        js_terminal.put("agentId",terminal.getTerminal_agentid()==null?"":terminal.getTerminal_agentid());/*pos所属代理*/
        js_terminal.put("posparterId",terminal.getTerminal_posparterid()==null?"":terminal.getTerminal_posparterid());/*pos所属pos合作者*/
        js_terminal.put("peijianposparterId",terminal.getTerminal_peijianposparterid()==null?"":terminal.getTerminal_peijianposparterid());/*pos配件所属pos合作者*/
        js_terminal.put("contraShopId",terminal.getTerminal_merchant_id()==null?"":terminal.getTerminal_merchant_id());/*商户ID*/
        js_terminal.put("changeText",terminal.getALLPAY_STATE());/*启停用备注*/
        js_terminal.put("shopSmallName",store.getStore_shopshoptname()==null?"":store.getStore_shopshoptname());/*pos网点简称*/
        js_terminal.put("userType",terminal.getTerminal_usertype());/*角色0网点管理,1网点业务,2商户管理*/
        js_terminal.put("posOwn","");/*pos归属 0自有pos,1银商pos,2杉德pos,3通联pos,4触联天下pos  已作废，传空值*/
        js_terminal.put("supeTermCode",terminal.getTerminal_supertermcode()==null?"":terminal.getTerminal_supertermcode()); /*如果为“”，则表示无关联pos，如果为32位uuid，则表示关联pos的id*/
        js_terminal.put("payChannel",terminal.getTerminal_paychannel()==null?"":terminal.getTerminal_paychannel());/*终端应用系统(1-allpay,2-浦发，3-荣邦，4-银盛)(单选)*/
        js_terminal.put("useAppSendMess", terminal.getTerminal_userappsendmess()==null?"":terminal.getTerminal_userappsendmess()); /*软pos是否使用一码付通知(0不使用,1使用)*/
        LogHelper.info("推送POS  kafka信息：" + js_terminal.toString());
        Boolean result=kafkaProducerService.send("apay_terminal", js_terminal.toString());
        LogHelper.info("推送POS kafka信息,返回结果：" + result);
        return result;
    }

    /**
     * 推送代理信息至kafka
     * @param agentId
     * @return
     */
    public boolean sendApayagentToKafka(String agentId,String marked) throws Exception
    {
        LogHelper.info("代理信息推送kafka---->代理id：" + agentId+"删除标记marked："+marked);
        if(StringUtilsHelper.isEmpty(agentId))
        {
            LogHelper.info("================未传入代理id================");
            return false;
        }
        Allpay_Agent agent=ageDao.getMerchant(agentId);
        if(agent==null)
        {
            LogHelper.info("================未查到相关代理================");
            return false;
        }
        JSONObject js_agent = new JSONObject();
        js_agent.put("agentId",agent.getAgent_id());/*代理id*/
        js_agent.put("agentLevel",agent.getAgent_level()); /*代理级别  1 ：一级代理，2： 二级代理*/
        js_agent.put("agentLocation",agent.getAgent_location()==null?"":agent.getAgent_location()); /*上级代理id  0:没有上级*/

        if("del".equals(marked))
        {
            js_agent.put("state","0");/*代理状态 0停用，1启用*/
        }
        else
        {
            js_agent.put("state",agent.getALLPAY_ISSTART());/*代理状态 0停用，1启用*/
        }
        js_agent.put("branchId",agent.getAgent_branch_id()==null?"":agent.getAgent_branch_id()); /*代理所属分公司id*/
        js_agent.put("cdate",formatTime(agent.getALLPAY_CREATETIME(),"yyyyMMddHHmmss"));/*创建时间yyyyMMddHHmmss*/
        js_agent.put("agentName",agent.getAgent_name()==null?"":agent.getAgent_name()); /*代理全称*/
        js_agent.put("agentShortName",agent.getAgent_shortname()==null?"":agent.getAgent_shortname()); /*代理简称*/
        js_agent.put("operator","");/*业务运营方 已作废，传空值*/
        js_agent.put("provinceID",agent.getAgent_province_id()==null?"":agent.getAgent_province_id());/*省ID*/
        js_agent.put("provinceName",agent.getAgent_province_name()==null?"":agent.getAgent_province_name()); /*省名称*/
        js_agent.put("cityId",agent.getAgent_city_id()==null?"":agent.getAgent_city_id()); /*城市ID*/
        js_agent.put("cityName",agent.getAgent_city_name()==null?"":agent.getAgent_city_name());/*城市名称*/
        js_agent.put("countyId",agent.getAgent_country_id()==null?"":agent.getAgent_country_id());/*区县ID*/
        js_agent.put("countyName",agent.getAgent_country_name()==null?"":agent.getAgent_country_name());/*区县名称*/
        js_agent.put("agentAddress",agent.getAgent_address()==null?"":agent.getAgent_address());/*代理地址*/
        js_agent.put("legalPersonName",agent.getAgent_legalpersonname()==null?"":agent.getAgent_legalpersonname());/*代理联系人姓名*/
        js_agent.put("legalPersonPhone",agent.getAgent_legalpersonphone()==null?"":agent.getAgent_legalpersonphone());/*代理联系人手机号*/
        js_agent.put("reMark",agent.getAgent_remark()==null?"":agent.getAgent_remark());/*备注*/
        js_agent.put("agentAllpayId",agent.getAgent_allpayid()==null?"":agent.getAgent_allpayid());/*Allpay分配给代理商接入ID*/
        js_agent.put("agentAllpaykey",agent.getAgent_allpaykey()==null?"":agent.getAgent_allpaykey());/*Allpay分配给代理商接入key1*/
        js_agent.put("agentShopPeopleName",agent.getAgent_shoppeoplename()==null?"":agent.getAgent_shoppeoplename());/*代理商拓展人*/
        LogHelper.info("推送代理kafka信息" + js_agent.toString());
        Boolean result=kafkaProducerService.send("apay_agent", js_agent.toString());
        LogHelper.info("推送代理信息至kafka，返回结果：" + result);
        return result;
    }

    /**
     * 推送POS合作方信息至kafka
     * @param posParterId
     * @return
     */
    public boolean sendApayPOSParter(String posParterId,String marked) throws Exception
    {
        LogHelper.info("pos合作方信息推送kafka---->POS合作方id：" + posParterId+"删除标记marked："+marked);
        if(StringUtilsHelper.isEmpty(posParterId))
        {
            LogHelper.info("================未传入POS合作方id================");
            return false;
        }
        Allpay_PosParter posParter=posParDao.getPosParter(posParterId);
        if(posParter==null)
        {
            LogHelper.info("================未查到相关POS合作方信息================");
            return false;
        }
       /* Allpay_Branch branch=*/
        JSONObject js_posParter = new JSONObject();
        js_posParter.put("pos_parterID",posParter.getPosparter_id());/*POS合作方 主键*/
        js_posParter.put("createDate",formatTime(posParter.getALLPAY_CREATETIME(), "yyyyMMddHHmmss"));/*用户创建时间*/
        js_posParter.put("parterId",posParter.getPosparter_parterid()==null?"":posParter.getPosparter_parterid());/*合作方id*/
        js_posParter.put("parterAllName",posParter.getPosparter_parterallname()==null?"":posParter.getPosparter_parterallname());/*合作方全称*/
        js_posParter.put("parterShortName",posParter.getPosparter_partershortname()==null?"":posParter.getPosparter_partershortname());/*合作方简称*/
        js_posParter.put("connectPerson",posParter.getPosparter_connectperson()==null?"":posParter.getPosparter_connectperson());/*联系人*/
        js_posParter.put("connectTel",posParter.getPosparter_connecttel()==null?"":posParter.getPosparter_connecttel());/*联系方式*/
        js_posParter.put("address",posParter.getPosparter_address()==null?"":posParter.getPosparter_address());/*地址*/
        js_posParter.put("parterLevel",posParter.getPosparter_parterlevel());/*合作方级别 1：第一级合作方，2：第二级合作方*/
        js_posParter.put("branchId",posParter.getPosparter_branch_id()==null?"":posParter.getPosparter_branch_id());/*分公司id*/
        js_posParter.put("branchName","");/*分公司名称*/
        js_posParter.put("belongParterId",posParter.getPosparter_belongparterid()==null?"":posParter.getPosparter_belongparterid());/*所属上级合作方*/
        js_posParter.put("provinceId",posParter.getPosparter_province_id()==null?"":posParter.getPosparter_province_id()); /*省id*/
        js_posParter.put("provinceName",posParter.getPosparter_province_name()==null?"":posParter.getPosparter_province_name());/*省*/
        js_posParter.put("cityId",posParter.getPosparter_city_id()==null?"":posParter.getPosparter_city_id());/*市id*/
        js_posParter.put("cityName",posParter.getPosparter_city_name()==null?"":posParter.getPosparter_city_name());/*市*/
        js_posParter.put("countryId",posParter.getPosparter_country_id()==null?"":posParter.getPosparter_country_id());/*县区id*/
        js_posParter.put("countryName",posParter.getPosparter_country_name()==null?"":posParter.getPosparter_country_name());/*县区*/

        if("del".equals(marked))
        {
            js_posParter.put("parter_state","0");/*合作方状态  0:停用，1：启用*/
        }
        else
        {
            js_posParter.put("parter_state",posParter.getALLPAY_ISSTART());/*合作方状态  0:停用，1：启用*/
        }
        js_posParter.put("remark",posParter.getPosparter_remark()==null?"":posParter.getPosparter_remark());/*备注*/
        LogHelper.info("推送Pos合作方kafka信息" + js_posParter.toString());
        Boolean result=kafkaProducerService.send("apay_posparter", js_posParter.toString());
        LogHelper.info("推送Pos合作方信息至kafka，返回结果：" + result);
        return result;
    }

    /**
     * 推送TMS信息至kafka
     * @param tmsMessageId
     * @return
     */
    public boolean sendApayTmsmessage(String tmsMessageId,String marked) throws Exception
    {
        LogHelper.info("TMS信息推送kafka---->TMS信息id：" + tmsMessageId +",删除标记marked："+marked);
        if(StringUtilsHelper.isEmpty(tmsMessageId))
        {
            LogHelper.info("================未传入TMS信息id================");
            return false;
        }
        Allpay_TMS tms=tmsDao.getTerminal(tmsMessageId);
        if(tms==null)
        {
            LogHelper.info("================未查到相关TMS信息================");
            return false;
        }
        JSONObject js_TMS = new JSONObject();
        js_TMS.put("tmsMessageId",tms.getTms_id()); /*TMS信息id 主键*/
        js_TMS.put("enterminalName",tms.getTms_terminalname()==null?"":tms.getTms_terminalname()); /*厂商*/
        js_TMS.put("terminalName",tms.getTms_enterterminalname()==null?"":tms.getTms_enterterminalname());/*厂商名称*/
        js_TMS.put("applicationName",tms.getTms_applicationname()==null?"":tms.getTms_applicationname());/*应用名称*/
        js_TMS.put("verNum",tms.getTms_vernum()==null?"":tms.getTms_vernum());/*版本号*/
        js_TMS.put("fileType",tms.getTms_feiltype());/*文件类型* 0：主控，1：应用，2：参数*/
        js_TMS.put("fileName",tms.getTms_feilname()==null?"":tms.getTms_feilname());/*文件名*/
        js_TMS.put("updateCount",tms.getTms_updatecontent()==null?"":tms.getTms_updatecontent());/*更新内容*/
        js_TMS.put("modelsName",tms.getTms_modelsname()==null?"":tms.getTms_modelsname());/*机型*/
        if("del".equals(marked))
        {
            js_TMS.put("state","1");/*启用状态 0：启用，1：禁用*/
        }
        else
        {
            js_TMS.put("state",tms.getALLPAY_ISSTART());/*启用状态 0：启用，1：禁用*/
            if(tms.getALLPAY_ISSTART()==0)
            {
                js_TMS.put("state","1");/*启用状态 0：启用，1：禁用*/
            }
            else
            {
                js_TMS.put("state","0");/*启用状态 0：启用，1：禁用*/
            }
        }

        LogHelper.info("推送TMS kafka信息" + js_TMS.toString());
        Boolean result=kafkaProducerService.send("apay_tmsmessage", js_TMS.toString());
        LogHelper.info("推送TMS信息至kafka，返回结果：" + result);
        return result;
    }

    /**
     * 推送威富通信息至kafka
     * @param wftId
     * @return
     */
    @Override
    public boolean sendWFTToKafka(String wftId) throws Exception {
        Wft_Shop wftShop= wftShopDao.getWftShopInfoByID(wftId);
        if(wftShop==null)
        {
            LogHelper.info("================未查到相关威富通信息================");
            return false;
        }
        JSONObject js_wft = new JSONObject();
        JSONObject js_channel = new JSONObject();
        js_channel.put("merchid",wftShop.getWft_shop_outmerchantid());/*商户主键UUID*/
        js_channel.put("merchName",wftShop.getWft_shop_name());/*商户名称*/
        js_channel.put("merchShortName",wftShop.getWft_shop_name());/*商户简称*/
        js_channel.put("merchstate","");/*商户状态 已作废传空值*/
        js_wft.put("choose","1");
        js_wft.put("code","192");
        js_wft.put("wftShopId",wftShop.getWft_shop_merchantid()==null?"":wftShop.getWft_shop_merchantid());
        js_wft.put("state",wftShop.getWft_shop_state());
        js_wft.put("whichBank",wftShop.getWft_shop_banktype());
        js_wft.put("zfbBillRate",wftShop.getWft_shop_zfbbill());
        js_wft.put("wxBillRate",wftShop.getWft_shop_wxbill());
        js_wft.put("mch_wftbank_key",wftShop.getWft_shop_secretKey()==null?"":wftShop.getWft_shop_secretKey());
        js_wft.put("mch_appid",wftShop.getWft_shop_appid()==null?"":wftShop.getWft_shop_appid());
        js_channel.put("wft",js_wft);

        LogHelper.info("推送威富通 kafka信息" + js_channel.toString());
        Boolean result=kafkaProducerService.send("apay_qvdao", js_channel.toString());
        LogHelper.info("推送威富通信息至kafka，返回结果：" + result);
        return result;
    }

    public boolean sendTestToKafka(String posParterId)
    {
        Boolean result=kafkaProducerService.send("apay_merchant", "测试");
        LogHelper.info("推送测试信息至kafka，返回结果：" + result);
        return result;
    }


}
