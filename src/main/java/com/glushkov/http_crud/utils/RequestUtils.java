package com.glushkov.http_crud.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class RequestUtils {
    public Long getUserIdFromHeaders(HttpServletRequest req){
        return Optional.ofNullable(req.getHeader("user_id")).map(Long::parseLong).orElse(null);
    }

    public Long getIdFromUrl(HttpServletRequest req){
        String[] urlArray = req.getRequestURI().split("/");
        try {
            return Long.parseLong(urlArray[urlArray.length - 1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public String getNameFromParameters(HttpServletRequest req){
        return req.getParameter("file");
    }
}
