package trs.com.tang.appconfig;

public class AppAPI {
    private static String IP = "134.175.20.81";
    //地址
    private static String Address = "http://"+IP+"/Tang/";
    //登录接口
    public static String LoginAddress = Address + "LoginServlet.do";
    //网易云http翻译接口
    public static String WangYi = Address + "TranslateServlet.do";

    //网易翻译
    public static String WangYiHttp = "http://openapi.youdao.com/api";


}
