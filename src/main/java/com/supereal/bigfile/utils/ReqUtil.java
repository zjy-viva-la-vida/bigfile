package com.supereal.bigfile.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 请求工具类
 *
 * @author ju.wang@bitmain.com
 */
public class ReqUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReqUtil.class);

    private ReqUtil() {
    }

    /**
     * 发送REST请求
     *
     * @param restTemplate {@link RestTemplate}对象
     * @param reqUrl       请求Url
     * @param method       Http请求方法
     * @param reqEntity    请求实体
     * @param uriVariables URI参数
     * @param <T>          返回类型
     * @return 响应结果
     */
    public static <T> Result<T> restExchange(RestTemplate restTemplate,
                                             String reqUrl,
                                             HttpMethod method,
                                             HttpEntity<?> reqEntity,
                                             Object... uriVariables) {
        ResponseEntity<Result<T>> respEntity = restTemplate.exchange(
                reqUrl,
                method,
                reqEntity,
                new ParameterizedTypeReference<Result<T>>() {},
                uriVariables
        );
        if (respEntity != null && respEntity.getStatusCode() == HttpStatus.OK) {
            Result<T> respResult = respEntity.getBody();
            if (respResult != null) {
                return respResult;
            }
        }
        return Result.error("请求发生错误");
    }

    /**
     * 发送REST列表请求
     *
     * @param restTemplate {@link RestTemplate}对象
     * @param reqUrl       请求Url
     * @param method       Http请求方法
     * @param reqEntity    请求实体
     * @param responseType 响应类型
     * @param uriVariables URI参数
     * @param <T>          返回类型
     * @return 响应结果
     */
    public static <T> Result<List<T>> restExchangeForList(RestTemplate restTemplate,
                                                          String reqUrl,
                                                          HttpMethod method,
                                                          HttpEntity<?> reqEntity,
                                                          ParameterizedTypeReference<Result<List<T>>> responseType,
                                                          Object... uriVariables) {
        ResponseEntity<Result<List<T>>> respEntity = restTemplate.exchange(
                reqUrl,
                method,
                reqEntity,
                responseType,
                uriVariables
        );
        if (respEntity != null && respEntity.getStatusCode() == HttpStatus.OK) {
            Result<List<T>> respResult = respEntity.getBody();
            if (respResult != null) {
                return respResult;
            }
        }
        return Result.error("请求发生错误");
    }

    /**
     * 发送REST请求
     *
     * @param restTemplate {@link RestTemplate}对象
     * @param reqUrl       请求Url
     * @param method       Http请求方法
     * @param reqEntity    请求实体
     * @param uriVariables URI参数
     * @param <T>          返回类型
     * @return 响应结果
     */
    public static <T> ResponseEntity<T> restExchangeForEntity(RestTemplate restTemplate,
                                                              String reqUrl,
                                                              HttpMethod method,
                                                              HttpEntity<?> reqEntity,
                                                              Object... uriVariables) {
        return restTemplate.exchange(
                reqUrl,
                method,
                reqEntity,
                new ParameterizedTypeReference<T>() {
                },
                uriVariables
        );
    }

    /**
     * 发送REST请求
     *
     * @param restTemplate {@link RestTemplate}对象
     * @param reqUrl       请求Url
     * @param method       Http请求方法
     * @param reqEntity    请求实体
     * @param responseType 响应类型
     * @param uriVariables URI参数
     * @param <T>          返回类型
     * @return 响应结果
     */
    public static <T> ResponseEntity<T> restExchangeForEntity(RestTemplate restTemplate,
                                                              String reqUrl,
                                                              HttpMethod method,
                                                              HttpEntity<?> reqEntity,
                                                              Class<T> responseType,
                                                              Object... uriVariables) {
        return restTemplate.exchange(
                reqUrl,
                method,
                reqEntity,
                responseType,
                uriVariables
        );
    }

    /**
     * 获取请求体
     *
     * @param request {@link HttpServletRequest}对象
     * @return 请求体
     */
    public static String getReqBody(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String s;
            while ((s = br.readLine()) != null) {
                builder.append(s);
            }
        } catch (IOException e) {
            LOGGER.error("获取请求体发生错误", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("关闭读取流发生错误", e);
                }
            }
        }
        return builder.toString();
    }

    /**
     * 获取请求的IP地址
     *
     * @param request {@link HttpServletRequest}对象
     * @return IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String xIp = request.getHeader("X-Real-IP");
        String xForwardFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(xForwardFor) && !"unknown".equalsIgnoreCase(xForwardFor)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xForwardFor.indexOf(",");
            if (index != -1) {
                return xForwardFor.substring(0, index);
            } else {
                return xForwardFor;
            }
        }

        xForwardFor = xIp;
        if (StringUtils.isNotEmpty(xForwardFor) && !"unknown".equalsIgnoreCase(xForwardFor)) {
            return xForwardFor;
        }
        if (StringUtils.isBlank(xForwardFor) || "unknown".equalsIgnoreCase(xForwardFor)) {
            xForwardFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xForwardFor) || "unknown".equalsIgnoreCase(xForwardFor)) {
            xForwardFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xForwardFor) || "unknown".equalsIgnoreCase(xForwardFor)) {
            xForwardFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xForwardFor) || "unknown".equalsIgnoreCase(xForwardFor)) {
            xForwardFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xForwardFor) || "unknown".equalsIgnoreCase(xForwardFor)) {
            xForwardFor = request.getRemoteAddr();
        }
        return xForwardFor;
    }

    /**
     * 添加Url参数
     *
     * @param url      请求Url
     * @param paramMap 参数映射
     * @return 新的Url
     */
    public static String addUrlParam(String url, Map<String, String> paramMap) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            LOGGER.error("解析Url发生错误", e);
        }
        if (uri == null) {
            return null;
        }

        Map<String, String> map = getUrlParamMap(url);
        map.putAll(paramMap);
        StringBuilder newQuery = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (index > 0) {
                newQuery.append("&");
            }
            newQuery.append(entry.getKey()).append("=").append(entry.getValue());
            index++;
        }

        return uri.getScheme() + "://" + uri.getAuthority() + uri.getPath() + "?" + newQuery;
    }

    /**
     * 获取Url参数
     *
     * @param url 请求Url
     * @return Url参数
     */
    public static Map<String, String> getUrlParamMap(String url) {
        Map<String, String> paramMap = new HashMap<>();
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            Stream.of(query.split("&")).forEach(str -> {
                String[] params = str.split("=");
                if (params.length < 2) {
                    return;
                }
                paramMap.put(params[0], params[1]);
            });
        } catch (URISyntaxException e) {
            LOGGER.error("解析Url发生错误", e);
        }
        return paramMap;
    }

    /**
     * 根据Url获取指定参数值
     *
     * @param url       请求Url
     * @param paramName 参数名
     * @return 参数值
     */
    public static String getUrlParam(String url, String paramName) {
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();
            for (String str : query.split("&")) {
                String[] params = str.split("=");
                if (params.length < 2) {
                    continue;
                }
                if (StringUtils.equals(paramName, params[0])) {
                    return params[1];
                }
            }
        } catch (URISyntaxException e) {
            LOGGER.error("解析Url发生错误", e);
        }
        return null;
    }

    /**
     * 转换请求结果
     *
     * @param reqResult    请求结果
     * @param businessCode 业务错误码
     * @param defaultMsg   默认错误信息
     * @return 转换后的结果对象
     */
    public static Result transReqResult(Result reqResult, int businessCode, String defaultMsg) {
        Result targetResult;
        if (reqResult != null) {
            targetResult = new Result();
            BeanUtils.copyProperties(reqResult, targetResult);
            targetResult.setCode(businessCode);
        } else {
            targetResult = Result.error(businessCode, defaultMsg);
        }
        return targetResult;
    }
}
