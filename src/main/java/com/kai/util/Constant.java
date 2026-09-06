package com.kai.util;

public class Constant {

    //Session & Cookie
    public static final String SESSION_ACCOUNT = "account";
    public static final String COOKIE_REMEMBER = "username";

    // view path
    public static final String DIR_REGISTER = "/views/register.jsp";
    public static final String DIR_LOGIN    = "/views/login.jsp";
    public static final String DIR_VERIFY   = "/views/verify.jsp";
    public static final String DIR_FORGOT   = "/views/forgot-password.jsp";
    public static final String DIR_RESET    = "/views/reset-password.jsp";

    //Role
    public static final int ROLE_USER  = 0;
    public static final int ROLE_ADMIN = 1;

    //acc state
    public static final int STATUS_INACTIVE = 0; 
    public static final int STATUS_ACTIVE   = 1; 

    //Upload
    public static final String DIR = AppConfig.get("upload.dir", "D:\\upload");

    private Constant() {} 
}