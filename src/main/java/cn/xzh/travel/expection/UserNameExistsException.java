package cn.xzh.travel.expection;

public class UserNameExistsException extends Exception {
    /**
     * 用户名已存在异常
     */
    public UserNameExistsException() {
    }

    public UserNameExistsException(String msg) {
        super(msg);
    }
}
