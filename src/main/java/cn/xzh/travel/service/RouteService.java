package cn.xzh.travel.service;

import cn.xzh.travel.dao.RouteDao;
import cn.xzh.travel.pojo.Route;

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
}
