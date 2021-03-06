package cn.xzh.travel.service;

import cn.xzh.travel.dao.RouteDao;
import cn.xzh.travel.pojo.*;
import org.apache.commons.beanutils.BeanUtils;

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

    public Route findRouteByRid(String rid)throws Exception {
        Map<String,Object> map =  routeDao.findRouteByRid(rid);
        Route route = new Route();//旅游线路对象
        Category category = new Category();//所属分类对象
        Seller seller = new Seller();//所属商家对象
        BeanUtils.populate(route,map);
        BeanUtils.populate(category,map);
        BeanUtils.populate(seller,map);
        route.setCategory(category);
        route.setSeller(seller);
        List<RouteImg> routeImgs = routeDao.findRouteImgsByRid(rid);
        route.setRouteImgList(routeImgs);
        return route;
    }

    public PageBean<Route> getPageBeanByFavoriteRank(int curPage,Map<String,Object> conditionMap)throws Exception{
        PageBean<Route> pageBean = new PageBean<Route>();
        pageBean.setCurPage(curPage);
        int pageSize= 8;
        pageBean.setPageSize(pageSize);
        int count = routeDao.getCountByFavoriteRank(conditionMap);
        pageBean.setCount(count);
        List<Route> routeList = routeDao.getRoutesFavoriteRankByPage(curPage,pageSize,conditionMap);
        pageBean.setData(routeList);
        return pageBean;
    }

}
