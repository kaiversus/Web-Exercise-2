package com.kai.controller.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import com.kai.dao.impl.UserDao;
import com.kai.entity.User;

@WebServlet(urlPatterns = { "/profile" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DEBUG: ProfileController doGet called");
        try {
            User user = userDao.findById(1);
            System.out.println("DEBUG: user found = " + (user != null));
            
            if (user == null) {
                user = new User("admin", "Nguyễn Văn A", "0909123456", "");
                userDao.insert(user);
                System.out.println("DEBUG: user inserted");
            }

            req.setAttribute("user", user);
            resp.setContentType("text/html;charset=UTF-8");
            req.getRequestDispatcher("/views/profile.jsp").include(req, resp);
            System.out.println("DEBUG: forwarded to profile.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        
        User user = userDao.findById(1);
        user.setFullname(fullname);
        user.setPhone(phone);

        try {
            Part part = req.getPart("images"); 
            if (part != null && part.getSize() > 0) {
                String uploadPath = req.getServletContext().getRealPath("/uploads");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                
                String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String filePath = uploadPath + File.separator + fileName;
                
                part.write(filePath); 
                user.setImages(fileName); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        userDao.update(user);
        
        req.setAttribute("message", "Cập nhật Profile thành công!");
        req.setAttribute("user", user);
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/views/profile.jsp").include(req, resp);
    }
}