package com.kai.controller.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;

import com.kai.util.Constant;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/image")
public class ImageController extends HttpServlet {

    private static final Path UPLOAD_ROOT =
            Paths.get(Constant.DIR).toAbsolutePath().normalize();

    private static final Map<String, String> ALLOWED = Map.of(
            "jpg",  "image/jpeg",
            "jpeg", "image/jpeg",
            "png",  "image/png",
            "gif",  "image/gif",
            "webp", "image/webp");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String fname = req.getParameter("fname");
        if (fname == null || fname.isBlank()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String safeName = Paths.get(fname).getFileName().toString();

        if (!safeName.matches("[A-Za-z0-9._-]{1,100}")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int dot = safeName.lastIndexOf('.');
        String ext = (dot < 0) ? "" : safeName.substring(dot + 1).toLowerCase(Locale.ROOT);
        String contentType = ALLOWED.get(ext);
        if (contentType == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path file = UPLOAD_ROOT.resolve(safeName).normalize();
        if (!file.startsWith(UPLOAD_ROOT) || !Files.isRegularFile(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType(contentType);
        resp.setContentLengthLong(Files.size(file));
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setHeader("Content-Disposition", "inline; filename=\"" + safeName + "\"");

        Files.copy(file, resp.getOutputStream());
    }
}