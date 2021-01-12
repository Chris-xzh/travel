package cn.xzh.travel.web.servlet;

import cn.xzh.travel.pojo.ResultInfo;
import cn.xzh.travel.pojo.Route;
import cn.xzh.travel.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.internal.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/route")
public class RouteServlet extends BaseServlet{

    private RouteService routeService = new RouteService();
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
        System.out.println(jsonData);
        response.getWriter().write(jsonData);
    }
}
