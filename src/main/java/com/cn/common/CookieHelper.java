package com.cn.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Cookie 辅助
 * 
 * @author cuixiaowei
 * 
 */
public class CookieHelper {
	/**
	 * 每页条数cookie名称
	 */
	public static final String COOKIE_PAGE_SIZE = "_cookie_page_size";
	/**
	 * 默认每页条数
	 */
	public static final int DEFAULT_SIZE = 20;
	/**
	 * 每页条数
	 */
	public static final int MAX_SIZE = 200;

	/**
	 * 获取当前Request对象.
	 *
	 * @return 当前Request对象 可能为null
	 * @throws IllegalStateException 当前线程不是web请求抛出此异常.
	 */
	protected static HttpServletRequest currentRequest() throws IllegalStateException {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null) {
			throw new IllegalStateException("当前线程中不存在 Request 上下文");
		}
		return attrs.getRequest();
	}

	/**
	 * 获得cookie的每页条
	 * 
	 * 使用_cookie_page_size作为cookie name
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return default:20 max:200
	 */

	public static int getPageSize(HttpServletRequest request) {
		Assert.notNull(request);
		Cookie cookie = getCookie(request, COOKIE_PAGE_SIZE);
		int count = 0;
		if (cookie != null) {
			if (NumberUtils.isDigits(cookie.getValue())) {
				count = Integer.parseInt(cookie.getValue());
			}
		}
		if (count <= 0) {
			count = DEFAULT_SIZE;
		} else if (count > MAX_SIZE) {
			count = MAX_SIZE;
		}
		return count;
	}

	/**
	 * 获得cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param name
	 *            cookie name
	 * @return if exist return cookie, else return null.
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}
	/**
	 * 获得cookie
	 *
	 *            HttpServletRequest
	 * @param name
	 *            cookie name
	 * @return if exist return cookie, else return null.
	 */
	public static String getCookieByName( String name) {
		Assert.notNull(currentRequest());
		Cookie[] cookies = currentRequest().getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals("BusinessUserCookie")) {
					//获取用户cookie信息
					String userInfo=c.getValue();
					try {
						userInfo=URLDecoder.decode(userInfo,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					JSONObject json=new JSONObject(userInfo);
					return json.getString(name);
				}
			}
		}
		return null;
	}

	/**
	 * 根据部署路径，将cookie保存在根目录
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry//有效时间以S为单位，-1代表关闭浏览
	 * @param domain//域名 比如www.baidu.com
	 * @return
	 */
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		if (expiry != null) {
			cookie.setMaxAge(expiry);
		}
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		response.addCookie(cookie);
		return cookie;
	}

	/**
	 * 取消cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void cancleCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String domain) {
		Cookie cookie = new Cookie(name, "");
		cookie.setMaxAge(0);
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	public static Map<String,String> ReadCookieMap(HttpServletRequest request){
		Map<String,String> cookieMap = new HashMap<String,String>();
		Cookie[] cookies = request.getCookies();
		if(null!=cookies){
			for(Cookie cookie : cookies){
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}
		return cookieMap;
	}
}
