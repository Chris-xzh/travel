package cn.xzh.travel.service;

import cn.xzh.travel.dao.UserDao;
import cn.xzh.travel.expection.PasswordErrorException;
import cn.xzh.travel.expection.UserNameExistsException;
import cn.xzh.travel.expection.UserNameNotNullException;
import cn.xzh.travel.pojo.ResultInfo;
import cn.xzh.travel.pojo.User;
import cn.xzh.travel.utils.Md5Util;
import cn.xzh.travel.utils.UuidUtil;

public class UserService {
    //实例用户数据访问类
    private UserDao userDao = new UserDao();

    public boolean register(User user, ResultInfo resultInfo ) throws Exception {
        if(user.getUsername()==null || "".equals(user.getUsername().trim())){
            throw new UserNameNotNullException("用户名不能为空");
        }
        User dbUser = userDao.getUserByUserName(user.getUsername());
        if(dbUser!=null){
            throw new UserNameExistsException("用户名已被注册");
        }
        user.setStatus("N");
        user.setCode(UuidUtil.getUuid());
        user.setPassword(Md5Util.encodeByMd5(user.getPassword()));
        userDao.register(user);
        resultInfo.setUserCode(user.getCode());
        return true;
    }

    public boolean active(String code)throws Exception {
        int rows= userDao.active(code);
        return rows>0;
    }
    public User login(String username, String password) throws Exception {
        if(username==null || "".equals(username.trim())){
            throw new UserNameNotNullException("用户名不能为空");
        }
        User dbUser = userDao.getUserByUserName(username);
        if(dbUser==null){
            throw new UserNameNotNullException("用户名不存在");
        }
        if(password!=null && !password.equals(dbUser.getPassword())){
            throw new PasswordErrorException("密码错误");
        }
        return dbUser;
    }
}
