package cn.xzh.travel.web.servlet;

import cn.xzh.travel.pojo.*;
import cn.xzh.travel.service.FavoriteService;
import cn.xzh.travel.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/favorite")
public class FavoriteServlet extends BaseServlet {

    private FavoriteService favoriteService = new FavoriteService();

    private RouteService routeService = new RouteService();


    private void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            User user = (User) request.getSession().getAttribute("loginUser");
            if (user == null) {
                resultInfo = new ResultInfo(true,false,null);
            }else{
                String rid = request.getParameter("rid");
                Route route = new Route();
                route.setRid(Integer.parseInt(rid));
                favoriteService.addFavorite(route,user);
                route = routeService.findRouteByRid(rid);
                int count = route.getCount();
                resultInfo = new ResultInfo(true,count,null);
            }
        }catch (Exception e){
            e.printStackTrace();
            resultInfo = new ResultInfo(false);
        }
        String jsonData =  new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(jsonData);
    }


    private void findFavoriteByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            User user =(User) request.getSession().getAttribute("loginUser");
            String curPageStr =request.getParameter("curPage");
            PageBean<Favorite> pageBean = favoriteService.getPageBean(user.getUid(),curPageStr);
            resultInfo = new ResultInfo(true,pageBean,null);
        }catch (Exception e){
            e.printStackTrace();
            resultInfo = new ResultInfo(false);
        }
        String jsonData =  new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(jsonData);
    }
}
