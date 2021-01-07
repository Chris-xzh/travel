package cn.xzh.travel.web.servlet;

import cn.xzh.travel.expection.UserNameExistsException;
import cn.xzh.travel.expection.UserNameNotNullException;
import cn.xzh.travel.pojo.ResultInfo;
import cn.xzh.travel.pojo.User;
import cn.xzh.travel.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    /**
     * 实例用户业务类
     */
    private UserService userService = new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if("register".equals(action)){
            register(request,response);
        }else if("active".equals(action)){
            active(request,response);
        }
    }
    private void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo =new ResultInfo();
        String userCheckCode = request.getParameter("check");
        String serverCheckCode= (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        if(serverCheckCode!=null && !serverCheckCode.equalsIgnoreCase(userCheckCode)){
            resultInfo = new ResultInfo(false,"验证码错误");
        }else {
            try {
                User user = new User();
                BeanUtils.populate(user, request.getParameterMap());
                boolean flag = userService.register(user, resultInfo);
                if (flag) {
                    resultInfo.setFlag(true);
                }
            } catch (UserNameNotNullException | UserNameExistsException e) {
                resultInfo = new ResultInfo(false, e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        String date =  new ObjectMapper().writeValueAsString(resultInfo);
        response.setContentType("text/html;charset=utf-8");
        request.getServletContext().setAttribute("code",resultInfo.getUserCode());
        response.getWriter().write(date);
    }

    private void active(HttpServletRequest request, HttpServletResponse response){
        try {
            String code = (String)request.getServletContext().getAttribute("code");

            boolean flag = userService.active(code);

            if(flag){
                response.sendRedirect(request.getContextPath()+"/login.html");
            }else{
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write("激活失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
