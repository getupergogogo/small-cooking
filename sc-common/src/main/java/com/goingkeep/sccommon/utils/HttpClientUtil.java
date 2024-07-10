package com.goingkeep.sccommon.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http工具类，用于通过请求从指定的URL获取数据，并支持向URL添加查询参数。
 */
public class HttpClientUtil {

    static final int TIMEOUT_MSEC = 5*1000;

    /**
     * GET请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String doGet(String url, Map<String, String> paramMap){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String result = "";
        CloseableHttpResponse response = null;

        try {
            //使用URIBuilder类根据传入的URL和参数映射构建一个完整的URI。如果有参数映射，则将每对参数添加到URI中。
            URIBuilder uriBuilder = new URIBuilder(url);
            if(paramMap != null){
                for (String key : paramMap.keySet()){
                    uriBuilder.addParameter(key, paramMap.get(key));
                }
            }
            URI uri = uriBuilder.build();

            //创建GET请求
            HttpGet httpGet = new HttpGet(uri);

            //发送请求
            response = httpClient.execute(httpGet);

            //判断响应状态
            if(response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 以表单形式的POST请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String doPOST(String url, Map<String, String> paramMap){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;

        try {

            //创建POST请求
            HttpPost httpPost = new HttpPost(url);
            //创建参数列表
            if(paramMap != null){
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> param : paramMap.entrySet()){
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                //模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            //设置请求的配置信息（通过 builderRequestConfig() 方法配置）
            httpPost.setConfig(builderRequestConfig());

            //发送请求
            response = httpClient.execute(httpPost);

            if(response.getStatusLine().getStatusCode() == 200){
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 以json格式的POST请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String doPOST2Json(String url, Map<String, String> paramMap) throws JSONException, IOException {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            if(paramMap != null) {
                //构造json格式数据
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    jsonObject.put(param.getKey(), param.getValue());
                }
                StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
                //设置请求编码
                entity.setContentEncoding("UTF-8");
                //设置数据类型
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            //设置请求的配置信息（通过 builderRequestConfig() 方法配置）
            httpPost.setConfig(builderRequestConfig());
            // 执行http请求
            httpClient.execute(httpPost);

            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MSEC)
                .setConnectionRequestTimeout(TIMEOUT_MSEC)
                .setSocketTimeout(TIMEOUT_MSEC)
                .build();
    }
}
