package cn.xzh.travel.expection;

public class UserNameNotExistsException extends Exception {
    public UserNameNotExistsException(){}
    public UserNameNotExistsException(String msg){
        super(msg);
    }
}