package cn.xzh.travel.web.servlet;

import cn.xzh.travel.expection.PasswordErrorException;
import cn.xzh.travel.expection.UserNameExistsException;
import cn.xzh.travel.expection.UserNameNotExistsException;
import cn.xzh.travel.expection.UserNameNotNullException;
import cn.xzh.travel.pojo.ResultInfo;
import cn.xzh.travel.pojo.User;
import cn.xzh.travel.service.UserService;
import cn.xzh.travel.utils.Md5Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends BaseServlet {
    /**
     * 实例用户业务类
     */
    private UserService userService = new UserService();

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

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            password = Md5Util.encodeByMd5(password);
            String sysCheckCode = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
            String userCheckCode = request.getParameter("check");
            if (sysCheckCode != null && !userCheckCode.equalsIgnoreCase(sysCheckCode)) {
                resultInfo = new ResultInfo(false, "验证码错误");
            } else {
                User dbUser = userService.login(username, password);
                //判断登录是否成功
                if (dbUser != null) {
                    //登录成功，将用户信息写入session
                    request.getSession().setAttribute("loginUser", dbUser);
                    //实例返回数据对象
                    resultInfo = new ResultInfo(true);
                }
            }
       } catch (UserNameNotNullException e) {
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (UserNameNotExistsException e) {
            //用户名不存在或错误
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (PasswordErrorException e) {
            //密码错误
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);//系统异常，抛到友好页面
        }
        String jsonData =  new ObjectMapper().writeValueAsString(resultInfo);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(jsonData);
    }

    private void getLoginUserData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = null;
        User user =(User) request.getSession().getAttribute("loginUser");
        if(user==null){
            resultInfo = new ResultInfo(false);
        }else{
            resultInfo = new ResultInfo(true,user,null);
        }
        String jsonData =  new ObjectMapper().writeValueAsString(resultInfo);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(jsonData);
    }

    private void loginOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath()+"/login.html");
    }
}
