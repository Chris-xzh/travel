package cn.xzh.travel.web.servlet;

import cn.xzh.travel.pojo.PageBean;
import cn.xzh.travel.pojo.ResultInfo;
import cn.xzh.travel.pojo.Route;
import cn.xzh.travel.pojo.User;
import cn.xzh.travel.service.FavoriteService;
import cn.xzh.travel.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/route")
public class RouteServlet extends BaseServlet{

    private RouteService routeService = new RouteService();
    private FavoriteService favoriteService = new FavoriteService();


    private static Logger log = Logger.getLogger(RouteServlet.class.getClass());

    private void routeCareChoose(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            Map<String, List<Route>> map = routeService.routeCareChoose();
            resultInfo = new ResultInfo(true, map, null);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo = new ResultInfo(false);
        }
        String jsonData = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(jsonData);
    }

    private void findRouteListByCid(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            int curPage = 1;
            String curPageStr = request.getParameter("curPage");
            String cid = request.getParameter("cid");
            String rname = request.getParameter("rname");
            if (curPageStr != null && !curPageStr.equals("")) {
                curPage = Integer.parseInt(curPageStr);
            }
            PageBean pageBean = routeService.getPageBean(cid,curPage,rname);
            resultInfo = new ResultInfo(true,pageBean,null);
        }catch (Exception e){
            e.printStackTrace();
            resultInfo = new ResultInfo(false);
        }
        String jsonData = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(jsonData);
    }

    private void findRouteByRid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try{
            String rid = request.getParameter("rid");
            Route route = routeService.findRouteByRid(rid);
            resultInfo = new ResultInfo(true,route,null);
        }catch (Exception e){
            e.printStackTrace();
            resultInfo = new ResultInfo(false);
        }
        String jsonData =  new ObjectMapper().writeValueAsString(resultInfo);
        System.out.println(jsonData);
        response.getWriter().write(jsonData);
    }

    private void isFavoriteByRid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            User user = (User) request.getSession().getAttribute("loginUser");
            if(user==null){
                resultInfo = new ResultInfo(true,false,null);
            }else{
                String rid = request.getParameter("rid");
                boolean isFavorite =favoriteService.isFavoriteByRidAndUserId(rid,user.getUid());
                resultInfo = new ResultInfo(true,isFavorite,null);
            }
        }catch (Exception e){
            e.printStackTrace();
            resultInfo = new ResultInfo(false);//false代表发送了错误
        }
        String jsonData =  new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(jsonData);
    }

    private void findRoutesFavoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            int curPage = 1;//默认第1页
            String curPageStr = request.getParameter("curPage");
            if(curPageStr!=null && !curPageStr.trim().equals("")){
                curPage = Integer.parseInt(curPageStr);
            }
            Map<String,Object> conditionMap = new HashMap<String,Object>();
            conditionMap.put("rname",request.getParameter("rname"));//封装旅游线路名称搜索条件
            conditionMap.put("startPrice",request.getParameter("startPrice"));//封装最小金额搜索条件
            conditionMap.put("endPrice",request.getParameter("endPrice"));//封装最大金额搜索条件
            PageBean<Route> pageBean = routeService.getPageBeanByFavoriteRank(curPage,conditionMap);
            resultInfo = new ResultInfo(true,pageBean,null);
        }catch (Exception e){
            e.printStackTrace();
            resultInfo = new ResultInfo(false);
        }
        String jsonData =  new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(jsonData);
    }
}
