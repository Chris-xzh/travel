package cn.xzh.travel.dao;

import cn.xzh.travel.pojo.Route;
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
        //查找上架当前分类旅游线路的总记录数
        StringBuilder sqlBuilder= new StringBuilder("SELECT COUNT(*) FROM tab_route WHERE rflag='1'");
        //定义存储sql参数占位符对应值的动态集合
        List<Object> paramList = new ArrayList<Object>();
        //当前cid条件有效是进行sql语句拼接过滤
        if(cid!=null && !cid.trim().equals("")){
            sqlBuilder.append(" AND cid=?");
            paramList.add(cid);
        }
        //当前rname条件有效是进行sql语句拼接过滤
        if(rname!=null && !rname.trim().equals("")){
            sqlBuilder.append(" AND rname like ?");
            try {
                paramList.add("%" + URLDecoder.decode(rname, "utf-8") + "%");
            }catch (Exception e){
                e.printStackTrace();//不需要别的地方处理异常，所以此处直接处理
            }
        }
        //将List<Object>转换为执行时需要Object[]
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
            sqlBuilder.append(" AND r.rname like ?");
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



}
