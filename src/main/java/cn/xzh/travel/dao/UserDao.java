package cn.xzh.travel.dao;

import cn.xzh.travel.pojo.User;
import cn.xzh.travel.utils.JdbcUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public class UserDao {
    /**
     * JdbcTemplate
     */
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    public User getUserByUserName(String username) {
        String sql="SELECT * FROM tab_user WHERE username=?";
        User user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
        }catch (EmptyResultDataAccessException e){

        }
        return user;

    }

    public void register(User user) {
        //定义插入用户sql语句
        String sql="INSERT INTO tab_user VALUES(NULL,?,?,?,?,?,?,?,?,?)";
        if("".equals(user.getBirthday())){
            user.setBirthday(null);
        }
        //执行sql语句,返回影响行数
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode()
        );
    }
    public int active(String code)throws SQLException {
        String sql="UPDATE tab_user SET STATUS ='Y' WHERE CODE=? AND STATUS='N'";
        return jdbcTemplate.update(sql,code);
    }
}
