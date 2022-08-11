package org.fall.util;

import org.fall.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
