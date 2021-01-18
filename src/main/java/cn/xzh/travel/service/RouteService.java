package cn.xzh.travel.service;

import cn.xzh.travel.dao.RouteDao;
import cn.xzh.travel.pojo.PageBean;
import cn.xzh.travel.pojo.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteService {
    private RouteDao routeDao = new RouteDao();

    public Map<String, List<Route>> routeCareChoose() {
        Map<String,List<Route>> map = new HashMap<>();
        List<Route> popularityList = routeDao.getPopularityRouteList();
        map.put("popularity",popularityList);
        List<Route> newestList = routeDao.getNewestRouteList();
        map.put("newest",newestList);
        List<Route> themeList = routeDao.getThemeRouteList();
        map.put("theme",themeList);
        return map;
    }
    public PageBean getPageBean(String cid, int curPage,String rname)throws Exception {
        PageBean pageBean = new PageBean();
        pageBean.setCurPage(curPage);
        int pageSize = 3;
        pageBean.setPageSize(pageSize);
        List<Map<String,Object>> mapList = routeDao.getRoutesByPage(cid,curPage,pageSize,rname);
        List<Route> routeList = convertMapListToList(mapList);
//        List<Route> routeList = routeDao.getRoutesByPage(cid,curPage,pageSize,rname);
        pageBean.setData(routeList);
        int count = routeDao.getCountByCid(cid,rname);
        pageBean.setCount(count);
        return pageBean;
    }
    public List<Route> convertMapListToList(List<Map<String,Object>> mapList){
        List<Route> arrayList = new ArrayList<>();
        for(Map<String,Object> map : mapList){
            Route route = new Route();
            route.setCid((Integer) map.get("cid"));
            route.setRname((String) map.get("rname"));
            route.setPrice((Double) map.get("price"));
            route.setRouteIntroduce((String) map.get("routeIntroduce"));
            route.setRflag((String) map.get("rflag"));
            route.setRdate((String) map.get("rdate"));
            route.setRdate((String) map.get("rdate"));
            route.setCount((Integer) map.get("count"));
            route.setRid((Integer) map.get("rid"));
            route.setRimage((String) map.get("rimage"));
            route.setSid((Integer) map.get("sid"));
            route.setSourceId((String) map.get("sourceId"));
            route.setCid((Integer) map.get("cid"));
            arrayList.add(route);
        }
        return arrayList;
    }



}
