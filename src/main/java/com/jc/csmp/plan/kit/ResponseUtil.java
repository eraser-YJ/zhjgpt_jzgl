package com.jc.csmp.plan.kit;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResponseUtil {

    /**
     * θΏεζε
     *
     * @param resp
     * @param json
     * @throws Exception
     */
    public static void returnJons(HttpServletResponse resp,String json) throws Exception {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter writer = resp.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
