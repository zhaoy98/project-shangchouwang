package org.fall.exception;

/**
 * 登陆失败后抛出的异常
 * @author zhaoy
 * @date 2022/7/10 - 15:32
 */
public class LoginFailedException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public LoginFailedException() {
        super();
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }
}
