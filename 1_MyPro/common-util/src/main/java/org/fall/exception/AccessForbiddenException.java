package org.fall.exception;

/**
 * 创建一个自定义异常AccessForbiddenException，在用户未登录时访问受保护资源时抛出
 * @author zhaoy
 * @date 2022/7/10 - 19:27
 */
public class AccessForbiddenException extends RuntimeException{
    public AccessForbiddenException() {
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    public AccessForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
