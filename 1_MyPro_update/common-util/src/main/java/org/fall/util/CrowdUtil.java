package org.fall.util;

import org.fall.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * @author Zhaoy
 * @creat 2022-03-29-15:51
 */
public class CrowdUtil {
    /**
     * 判断一个请求是否为ajax请求
     * @param request
     * @return  //true == json请求  //false == 普通页面请求
     */
    public static boolean judgeRequestType(HttpServletRequest request){

        // 1. 获取请求消息头消息
        String acceptInformation = request.getHeader("Accept");
        String xRequestInformation = request.getHeader("X-Requested-With");
        // 检查并返回
        return
                   (acceptInformation != null
                           &&
                           acceptInformation.length() > 0
                           &&
                           acceptInformation.contains("application/json"))
                           ||
                   (xRequestInformation != null
                           &&
                           xRequestInformation.length() > 0
                           &&
                           xRequestInformation.equals("XMLHttpRequest"));
    }

    /**
     * 对明文字符串进行MD5加密
     * @param source 传入的明文字符串
     * @return 加密结果
     */
    public static String md5(String source){
        // 1. 首先判断source是否有效
        if (source == null || source.length() == 0){
            // 2. 不是有效的字符串，那么就会抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        try{
            // 3. 获取MessageDigest对象
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 4. 获取明文字符串对应的字节数组
            byte[] input = source.getBytes();
            // 5. 执行加密
            byte[] output = messageDigest.digest(input);
            // 6. 创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);
            // 7. 按照16进制将biginteger的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param host  请求的地址
     * @param path  请求的后缀
     * @param appCode   购入的api的appCode
     * @param phoneNum  发送验证码的目的号码
     * @param sign      签名编号
     * @param skin      模板编号
     * @return          发送成功则返回发送的验证码，放在ResultEntity中，失败则返回失败的ResultEntity
     */
    public static ResultEntity<String> sendCodeByShortMessage(
            String host,
            String path,
            String appCode,
            String phoneNum,
            String sign,
            String skin
    ){
        // 生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++){
            int random = (int)(Math.random()*10);
            builder.append(random);
        }
        String param = builder.toString();  // 【4】请求参数，详见文档描述
        String urlSend = host + path + "?param=" + param + "&phone=" + phoneNum + "&sign=" + sign + "&skin=" + skin;  // 【5】拼接请求链接
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appCode);// 格式Authorization:APPCODE (中间是英文空格)
            int httpCode = httpURLCon.getResponseCode();

            // 买不起短信服务套餐包，强行让他==200
            httpCode = 200;

            if (httpCode == 200) {
                // 买不起短信服务套餐包，强行注释返回
                // String json = read(httpURLCon.getInputStream());
                // System.out.println("正常请求计费(其他均不计费)");
                // System.out.println("获取返回的json:");
                // System.out.print(json);
                return ResultEntity.successWithData(param);
            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode `not exists`")) {
                    return ResultEntity.failed("AppCode错误 ");
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    return ResultEntity.failed("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    return ResultEntity.failed("参数错误");
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    return ResultEntity.failed("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    return ResultEntity.failed("套餐包次数用完 ");
                } else {
                    return ResultEntity.failed("参数名错误 或 其他错误" + error);
                }
            }

        } catch (MalformedURLException e) {
            return ResultEntity.failed("URL格式错误");
        } catch (UnknownHostException e) {
            return ResultEntity.failed("URL地址错误");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed("套餐包次数用完 ");
        }
    }
    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), StandardCharsets.UTF_8);
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
