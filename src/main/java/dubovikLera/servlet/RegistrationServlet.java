package dubovikLera.servlet;

import dubovikLera.dto.CreateUserDto;
import dubovikLera.entity.Gender;
import dubovikLera.entity.Role;
import dubovikLera.exception.ValidationException;
import dubovikLera.service.UserService;
import dubovikLera.utils.JspHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static dubovikLera.utils.UrlPath.REGISTRATION;

@WebServlet(REGISTRATION)
public class RegistrationServlet extends HttpServlet {
    private final UserService userService = UserService.getINSTANCE();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("roles", Role.values());
        req.setAttribute("genders", Gender.values());
        req.getRequestDispatcher(JspHelper.getPath("registration")).forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       var userDto = CreateUserDto.builder()
               .name(req.getParameter("name"))
               .birthday(req.getParameter("birthday"))
               .email(req.getParameter("email"))
               .password(req.getParameter("pwd"))
               .role(req.getParameter("role"))
               .gender(req.getParameter("gender"))
               .build();

       try{
           userService.create(userDto);
           resp.sendRedirect("/login");
       }catch (ValidationException e) {
           req.setAttribute("errors", e.getErrors());
           doGet(req, resp);
       }


    }
}
