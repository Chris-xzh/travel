package cn.xzh.travel.dao;

import cn.xzh.travel.pojo.Route;
import cn.xzh.travel.pojo.RouteImg;
import cn.xzh.travel.utils.JdbcUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public int getCountByCid(String cid,String rname) {
        StringBuilder sqlBuilder= new StringBuilder("SELECT COUNT(*) FROM tab_route r,tab_category c WHERE c.cid=r.cid AND rflag='1' ");
        List<Object> paramList = new ArrayList<Object>();
        if(cid!=null && !cid.trim().equals("")){
            sqlBuilder.append(" AND c.cid=?");
            paramList.add(cid);
        }
        if(rname!=null && !rname.trim().equals("")){
            sqlBuilder.append(" AND c.cname like ?");
            try {
                paramList.add("%" + URLDecoder.decode(rname, "utf-8") + "%");
            }catch (Exception e){
                e.printStackTrace();//不需要别的地方处理异常，所以此处直接处理
            }
        }
        Object[] params = paramList.toArray();
        int count = jdbcTemplate.queryForObject(sqlBuilder.toString(),params,Integer.class);
        return count;
    }

    public List<Map<String,Object>> getRoutesByPage(String cid, int curPage,int pageSize,String rname) {
        StringBuilder sqlBuilder= new StringBuilder("SELECT * FROM tab_route r,tab_category c WHERE c.cid=r.cid");
        List<Object> paramList = new ArrayList<Object>();
        if(cid!=null && !cid.trim().equals("")){
            sqlBuilder.append(" AND r.cid=?");
            paramList.add(cid);
        }
        if(rname!=null && !rname.trim().equals("")){
            sqlBuilder.append(" AND c.cname like ?");
            try {
                paramList.add("%" + URLDecoder.decode(rname, "utf-8") + "%");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        sqlBuilder.append(" LIMIT ?,?");
        int start = (curPage-1)*pageSize;
        int length=pageSize;
        paramList.add(start);
        paramList.add(length);
        Object[] params = paramList.toArray();
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList(sqlBuilder.toString(),params);
        return mapList;
    }

    public Map<String,Object> findRouteByRid(String rid) {
        String sql="SELECT * FROM tab_route r,tab_category c,tab_seller s WHERE r.cid = c.cid AND r.sid=s.sid AND r.rflag='1' AND r.rid=?";
        return jdbcTemplate.queryForMap(sql,new Object[]{rid});
    }

    public List<RouteImg> findRouteImgsByRid(String rid) {
        String sql="SELECT * FROM tab_route_img WHERE rid=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class),rid);
    }

}
