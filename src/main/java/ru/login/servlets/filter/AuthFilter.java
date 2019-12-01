package ru.login.servlets.filter;

import ru.login.dao.UserDAO;
import ru.login.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

import static ru.login.model.User.*;

@WebFilter(urlPatterns = {"/"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String username = request.getParameter("username");
        String password = request.getParameter("password");


        HttpSession session = request.getSession();

        if (Objects.nonNull(session) &&
                Objects.nonNull(session.getAttribute("username")) &&
                Objects.nonNull(session.getAttribute("password")) &&
                Objects.nonNull(session.getAttribute("role"))) {

            Role role = (Role) session.getAttribute("role");
            returnRelevantView(request, response, role);

        } else if (UserDAO.usernameAndPassValidation(username, password)) {
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("password", password);
            Role role = UserDAO.getRole(username);
            request.getSession().setAttribute("role", role);

            returnRelevantView(request, response, role);

        } else {
            request.getRequestDispatcher("/WEB-INF/view/login.html").forward(request, response);
        }
    }

    private void returnRelevantView(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Role role) throws ServletException, IOException {
        if (role.equals(Role.ADMIN)) {
            //request.getRequestDispatcher("/WEB-INF/view/admin.html").forward(request, response);

            String contextPath = request.getContextPath();

            response.sendRedirect("admin");
        } else if (role.equals(Role.USER)) {
            //request.getRequestDispatcher("/WEB-INF/view/user.html").forward(request, response);
            response.sendRedirect("user");
        }
    }
}
