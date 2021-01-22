package cn.xzh.travel.service;

import cn.xzh.travel.dao.FavoriteDao;
import cn.xzh.travel.pojo.Favorite;
import cn.xzh.travel.pojo.PageBean;
import cn.xzh.travel.pojo.Route;
import cn.xzh.travel.pojo.User;
import cn.xzh.travel.utils.JdbcUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDao();

    public boolean isFavoriteByRidAndUserId(String rid, int uid) throws Exception {
        Favorite favorite =  favoriteDao.findFavoriteByRidAndUserId(rid,uid);
        if(favorite!=null){
            return true;
        }else{
            return false;
        }
    }
    public void addFavorite(Route route, User user) throws Exception {
        Favorite favorite = new Favorite();
        favorite.setRoute(route);
        favorite.setUser(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        favorite.setDate(sdf.format(new Date()));
        DataSource dataSource = JdbcUtils.getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        TransactionSynchronizationManager.initSynchronization();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            connection.setAutoCommit(false);
            favoriteDao.addFavorite(jdbcTemplate,favorite);
            favoriteDao.updateRouteFavoriteNum(jdbcTemplate,route.getRid());
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            System.out.println("回滾了");
            throw e;//抛出异常，说明执行失败
        } finally {
            try {
                TransactionSynchronizationManager.clearSynchronization();
                connection.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public PageBean<Favorite> getPageBean(int uid, String curPageStr) throws Exception {
        PageBean<Favorite> pageBean = new PageBean<Favorite>();
        int curPage = 1;
        if(curPageStr!=null && !curPageStr.trim().equals("")){
            curPage = Integer.parseInt(curPageStr);
        }
        pageBean.setCurPage(curPage);
        int pageSize= 4;
        pageBean.setPageSize(pageSize);
        int count= favoriteDao.getCount(uid);
        pageBean.setCount(count);
        List<Map<String,Object>> mapList = favoriteDao.findFavoriteListByPage(uid,curPage,pageSize);
        List<Favorite> favoriteList = convertMapListToList(mapList);
        pageBean.setData(favoriteList);
        return pageBean;
    }
    private List<Favorite> convertMapListToList(List<Map<String,Object>> mapList)throws Exception{
        List<Favorite> favoriteList = null;
        if(mapList!=null && mapList.size()>0){
            favoriteList = new ArrayList<Favorite>();
            for (Map<String,Object> map:mapList) {
                Favorite favorite = new Favorite();
                Route route = new Route();
                BeanUtils.populate(favorite,map);
                BeanUtils.populate(route,map);
                favorite.setRoute(route);
                favoriteList.add(favorite);
            }
        }
        return favoriteList;
    }

}
