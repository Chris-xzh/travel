package cn.xzh.travel.dao;

import cn.xzh.travel.pojo.Category;
import cn.xzh.travel.utils.JdbcUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class CategoryDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    public List<Category> findAllCategory()throws SQLException {
        String sql="SELECT * FROM tab_category ORDER BY cid ";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Category.class));
    }
}
