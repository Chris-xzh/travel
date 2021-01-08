package cn.xzh.travel.expection;

public class PasswordErrorException extends Exception {
    public PasswordErrorException() {
    }
    public PasswordErrorException(String msg) {
        super(msg);
    }
}