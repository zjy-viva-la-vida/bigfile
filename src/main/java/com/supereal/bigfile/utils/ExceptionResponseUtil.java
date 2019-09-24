package com.supereal.bigfile.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supereal.bigfile.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 从异常中获取数据
 * 
 *
 * @author jianyang.zhao@bitmain.com
 * @version 1.0.0
 * @since 2019/08/09
 */
@Slf4j
public class ExceptionResponseUtil {



	/**
	 * 从cooke算法返回的exception里的获取response
	 * @param exc
	 * @return
	 */
	public static ExceptionRes getCodeAndMsgFromCookeResponse(Exception exc) {
		ExceptionRes result = new ExceptionRes();
		try{
			JSONObject response = JSON.parseObject(JSON.toJSONString(exc).replace("@type","type")).getJSONObject("responseBodyAsString");
			Integer code = response.getJSONObject("error").getInteger("code");
			String chineseMessage = response.getJSONObject("error").getString("chinese_message");
			if(StringUtils.isBlank(chineseMessage)){
				chineseMessage = response.getJSONObject("error").getString("message");
			}
			result.setCode(code);
			result.setMsg(chineseMessage);
		}catch (Exception e){
			log.error("获取cooke异常返回的response出错：" + JSON.toJSONString(e));
		}
		return result;
	}


	/**
	 * 从exception里拼接错误信息
	 * @param exc
	 * @return
	 */
	public static ExceptionRes spliceMsgFromException(Exception exc) {
		ExceptionRes result = new ExceptionRes();
		try{
			JSONObject response = JSON.parseObject(JSON.toJSONString(exc).replace("@type","type"));
			String type = response.getString("type");
			String message = response.getString("message");
			result.setCode(ErrorCode.BUSINESS_FAIL.getCode());
			result.setMsg(type + ":" + message);
		}catch (Exception e){
			log.error("获取异常返回的response出错：" + JSON.toJSONString(e));
		}
		return result;
	}



}
