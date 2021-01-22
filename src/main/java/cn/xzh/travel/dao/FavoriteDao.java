package cn.xzh.travel.dao;

import cn.xzh.travel.pojo.Favorite;
import cn.xzh.travel.utils.JdbcUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class FavoriteDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    public Favorite findFavoriteByRidAndUserId(String rid, int uid) throws SQLException {
        String sql="SELECT * FROM tab_favorite WHERE rid=? AND uid=?";
        try {
            return jdbcTemplate.queryForObject(
                    sql, new BeanPropertyRowMapper<Favorite>(Favorite.class),
                    rid, uid);
        }catch (Exception e){
            return null;//没有查找到数据会发生异常，所以这里没有查找到数据返回null
        }
    }

    public int addFavorite(JdbcTemplate jdbcTemplate,Favorite favorite) throws SQLException {
        String sql="INSERT INTO tab_favorite VALUES(?,?,?)";
        return jdbcTemplate.update(sql,
                favorite.getRoute().getRid(),
                favorite.getDate(),
                favorite.getUser().getUid()
        );
    }

    public int updateRouteFavoriteNum(JdbcTemplate jdbcTemplate, int rid) throws SQLException {
        String sql="UPDATE tab_route SET COUNT=COUNT+1 WHERE rid=?";
        return jdbcTemplate.update(sql,rid);
    }

    public int getCount(int uid) throws SQLException {
        String sql="SELECT COUNT(*) FROM tab_favorite WHERE uid=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,uid);
    }

    public List<Map<String, Object>> findFavoriteListByPage(int uid, int curPage, int pageSize) throws SQLException {
        int start = (curPage-1)*pageSize;//分页查询开始索引位置
        int length = pageSize;//每页大小
        String sql="SELECT * FROM tab_favorite f,tab_route r WHERE f.rid=r.rid AND  f.uid=? LIMIT ?,?";
        return jdbcTemplate.queryForList(sql,uid,start,length);
    }
}
