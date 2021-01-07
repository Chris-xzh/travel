package cn.xzh.travel.expection;

public class UserNameNotNullException extends Exception {
    public UserNameNotNullException() {
    }
    public UserNameNotNullException(String msg) {
        super(msg);
    }
}
