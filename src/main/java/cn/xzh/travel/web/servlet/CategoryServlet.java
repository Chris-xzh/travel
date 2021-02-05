package cn.xzh.travel.web.servlet;

import cn.xzh.travel.pojo.ResultInfo;
import cn.xzh.travel.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="CategoryServlet",urlPatterns = "/category")
public class CategoryServlet extends BaseServlet{

    private CategoryService categoryService = new CategoryService();

    private void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            String jsonData = categoryService.findAllCategory();
            resultInfo = new ResultInfo(true,jsonData,null);
        }catch (Exception e){
            e.printStackTrace();
            resultInfo = new ResultInfo(false);
        }
        String jsonData = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(jsonData);
    }
}
