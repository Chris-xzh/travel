package cn.xzh.travel.dao;

import cn.xzh.travel.pojo.Route;
import cn.xzh.travel.pojo.RouteImg;
import cn.xzh.travel.utils.JdbcUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URLDecoder;
import java.sql.SQLException;
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

    public int getCountByFavoriteRank(Map<String,Object> conditionMap) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) FROM tab_route WHERE rflag='1'");
        List<Object> paramList = new ArrayList<Object>();
        Object rnameObj = conditionMap.get("rname");
        if(rnameObj!=null && !rnameObj.toString().trim().equals("")){
            sqlBuilder.append(" and rname like ?");
            paramList.add("%"+rnameObj.toString().trim()+"%");
        }
        Object startPriceObj = conditionMap.get("startPrice");
        if(startPriceObj!=null && !startPriceObj.toString().trim().equals("")){
            sqlBuilder.append(" and price >= ?");
            paramList.add(startPriceObj.toString());
        }
        Object endPriceObj = conditionMap.get("endPrice");
        if(endPriceObj!=null && !endPriceObj.toString().trim().equals("")){
            sqlBuilder.append(" and price <= ?");
            paramList.add(endPriceObj.toString().trim());
        }
        Object[] params = paramList.toArray();
        return jdbcTemplate.queryForObject(sqlBuilder.toString(),params,Integer.class);
    }
    /**
     * 获取旅游线路收藏数量降序的排行榜当前页数据列表
     * @param curPage
     * @param pageSize
     * @return List<Route>
     */
    public List<Route> getRoutesFavoriteRankByPage(int curPage,int pageSize,Map<String,Object> conditionMap)throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM tab_route WHERE rflag='1'");
        List<Object> paramList = new ArrayList<Object>();
        Object rnameObj = conditionMap.get("rname");
        if(rnameObj!=null && !rnameObj.toString().trim().equals("")){
            sqlBuilder.append(" and rname like ?");
            paramList.add("%"+rnameObj.toString().trim()+"%");
        }
        Object startPriceObj = conditionMap.get("startPrice");
        if(startPriceObj!=null && !startPriceObj.toString().trim().equals("")){
            sqlBuilder.append(" and price >= ?");
            paramList.add(startPriceObj.toString());
        }
        Object endPriceObj = conditionMap.get("endPrice");
        if(endPriceObj!=null && !endPriceObj.toString().trim().equals("")){
            sqlBuilder.append(" and price <= ?");
            paramList.add(endPriceObj.toString().trim());
        }
        sqlBuilder.append(" ORDER BY COUNT DESC LIMIT ?,?");
        int start = (curPage-1)*pageSize;
        int length = pageSize;
        paramList.add(start);
        paramList.add(length);
        Object[] params = paramList.toArray();
        return  jdbcTemplate.query(sqlBuilder.toString(), new BeanPropertyRowMapper<Route>(Route.class),params);
    }

}
