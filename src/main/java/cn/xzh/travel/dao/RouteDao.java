package cn.xzh.travel.dao;

import cn.xzh.travel.pojo.Route;
import cn.xzh.travel.utils.JdbcUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JdbcUtils.getDataSource());

    public List<Route> getPopularityRouteList(){
        String sql="SELECT * FROM tab_route WHERE rflag='1' ORDER BY COUNT DESC LIMIT 0,4";
        List<Route> routeList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Route>(Route.class));
        return routeList;
    }

    public List<Route> getNewestRouteList() {
        String sql="SELECT * FROM tab_route WHERE rflag='1' ORDER BY rdate DESC LIMIT 0,4";
        List<Route> routeList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Route>(Route.class));
        return routeList;
    }

    public List<Route> getThemeRouteList() {
        String sql="SELECT * FROM tab_route WHERE rflag='1' AND isThemeTour='1' ORDER BY rdate DESC LIMIT 0,4";
        List<Route> routeList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Route>(Route.class));
        return routeList;
    }
}
