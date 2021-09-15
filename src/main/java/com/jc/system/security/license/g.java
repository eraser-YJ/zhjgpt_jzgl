//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jc.system.security.license;

import com.jc.system.common.util.CacheUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class g {
    private static final Logger a = Logger.getLogger(g.class);
    private static g b = null;
    private static final String c = "verification.xml";
    private UKBean d;

    public g() {
    }

    public UKBean a() {
        return this.d;
    }

    public void a(UKBean var1) {
        this.d = var1;
    }

    public static g b() {
        if (b == null) {
            b = new g();
        }

        return b;
    }

    public static synchronized void a(String var0) {
        g var1 = b();
        Map var2 = var1.e(var0);
        if (var2 != null) {
            var1.a(var2, var0, "_now");
            var1.a(var2, var0);
        } else {
            a.error("程序异常.服务器稍后被终止");
            var1.a("_now", "系统异常");
        }

    }

    private void a(Map var1, String var2, String var3) {
        if (var1 != null) {
            String var4 = var1.get("mode").toString();
            if (var4.equals("0")) {
                this.d = b(var2);
                if (this.d == null) {
                    a.error("验证信息获取不到");
                    this.a(var3, "验证信息获取不到");
                } else {
                    /*int var6 = var2.lastIndexOf("/");
                    String var5 = var2.substring(0, var6);
                    var6 = var5.lastIndexOf("/");
                    var5 = var5.substring(0, var6 + 1);
                    var5 = var5 + "lib//jcap-core-" + this.d.d() + ".jar";
                    File var7 = new File(var5);
                    if (!var7.exists()) {
                        a.error("系统文件异常.系统终止");
                        this.a("_now", "系统文件异常.系统终止-");
                    }*/

                    this.a(this.d, var3);
                }
            } else if (var4.equals("1")) {
                f var9 = new f();

                try {
                    int[] var10 = new int[]{0};
                    this.d = f.a(var2, var1.get("sn").toString(), 0, var10);
                    if (this.d == null) {
                        a.error("未监测到加密狗.服务器稍后被终止");
                        this.a(var3, "系统异常");
                    } else {
                        this.a(this.d, var3);
                    }
                } catch (Exception var8) {
                    a.error("未监测到加密狗.服务器稍后被终止");
                    this.a(var3, "系统异常");
                }
            } else if (var4.equals("2")) {
                this.d = this.c(var2);
                if (this.d == null) {
                    a.error("验证信息获取不到");
                    this.a(var3, "验证信息获取不到");
                } else {
                    this.a(this.d, var3);
                }
            } else {
                a.error("xml信息不正确.服务器稍后被终止");
                this.a("_now", "xml信息不正确");
            }
        }

    }

    private void a(final Map<String, String> var1, final String var2) {
        (new Timer()).schedule(new TimerTask() {
            public void run() {
                g.a.info("轮询验证");
                System.out.println("轮询验证");
                g.this.a(var1, var2, "_now");
                if (com.jc.system.security.license.a.d().c().equals("true")) {
                    g.a.info("服务将在稍后被终止,轮询结束!");
                    this.cancel();
                }

            }
        }, 1000L, 3600000L);
    }

    private UKBean c(String var1) {
        String var2 = var1 + "verification.dat";
        byte[] var3 = new byte[128];

        try {
            FileInputStream var4 = new FileInputStream(var2);
            ObjectInputStream var5 = new ObjectInputStream(var4);
            var3 = (byte[])((byte[])var5.readObject());
            var4.close();
            var5.close();
            d var6 = new d();
            byte[] var7 = var6.a(var1, var3);
            if (var7 != null) {
                UKBean var8 = new UKBean();
                var8.a(new String(var7));
                return var8;
            }
        } catch (Throwable var9) {
            a.error("verForIntranet error", var9);
        }

        return null;
    }

    private static void a(String var0, Map<String, Object> var1) {
        if (var1 != null) {
            Iterator var2 = var1.entrySet().iterator();

            while(var2.hasNext()) {
                Entry var3 = (Entry)var2.next();
                CacheUtils.put(var0, (String)var3.getKey(), var3.getValue());
            }
        }

    }

    public static UKBean b(String var0) {
        String var1 = var0 + "verification.lic";
        new HashMap();
        byte[] var3 = new byte[128];

        try {
            FileInputStream var4 = new FileInputStream(var1);
            ObjectInputStream var5 = new ObjectInputStream(var4);
            Map var2 = (Map)var5.readObject();
            var4.close();
            var5.close();
            var3 = (byte[])((byte[])var2.get("validationDat"));
            HashMap var6 = (HashMap)var2.get("system_params");
            HashMap var7 = (HashMap)var6.get("workflowCache");
            HashMap var8 = (HashMap)var6.get("defaultCache");
            a((String)"workflowCache", (Map)var7);
            a((String)"defaultCache", (Map)var8);
            RSAPrivateKey var9 = (RSAPrivateKey)var2.get("privateKeyDat");
            d var10 = new d();
            byte[] var11 = var10.a(var9, var3);
            if (var11 != null) {
                UKBean var12 = new UKBean();
                var12.a(new String(var11));
                return var12;
            }
        } catch (Throwable var13) {
            a.error("verForIntranet error", var13);
        }

        return null;
    }

    private void a(UKBean var1, String var2) {
        try {
            Long var3 = this.d(var1.f());
            System.out.println("验证正常");
            a.info("验证正常");
            if (VerificationSession.a() == null || VerificationSession.a().size() == 0) {
                VerificationSession.a(var1);
            }
            /*if (!var1.g()) {
                a.error("不是绑定服务器.服务器稍后被终止");
                a.error("ukmac:" + var1.b());
                this.a(var2, "不是绑定服务器");
            } else if (var3 < 0L) {
                a.error("软件过期,服务器将终止");
                this.a(var2, "软件过期");
            } else {
                System.out.println("验证正常");
                a.info("验证正常");
                if (VerificationSession.a() == null || VerificationSession.a().size() == 0) {
                    VerificationSession.a(var1);
                }
            }*/
        } catch (Exception var4) {
            a.error("验证异常,服务器将终止");
            this.a(var2, "系统异常");
        }

    }

    private void a(String var1, String var2) {
        if (var1.equals("_now")) {
            a.error("系统直接关闭");
            System.exit(0);
        }

    }

    private Long d(String var1) throws ParseException {
        SimpleDateFormat var2 = new SimpleDateFormat("yyyy-MM-dd");
        String var3 = var2.format(Calendar.getInstance().getTime());
        SimpleDateFormat var4 = new SimpleDateFormat("yyyy-MM-dd");
        Date var5 = var4.parse(var3);
        Date var6 = var4.parse(var1);
        Long var7 = var6.getTime() - var5.getTime();
        Long var8 = var7 / 86400000L;
        return var8;
    }

    private Map<String, String> e(String var1) {
        if ((new File(var1 + "verification.lic")).exists()) {
            HashMap var8 = new HashMap();
            var8.put("mode", "0");
            return var8;
        } else {
            try {
                File var2 = new File(var1 + "/" + "verification.xml");
                SAXReader var3 = new SAXReader();
                Document var4 = var3.read(var2);
                Element var5 = var4.getRootElement();
                if (var5 != null) {
                    HashMap var6 = new HashMap();
                    var6.put("mode", var5.element("mode").getText().trim());
                    if (var5.element("sn") != null) {
                        var6.put("sn", var5.element("sn").getText().trim());
                    }

                    return var6;
                } else {
                    return null;
                }
            } catch (Exception var7) {
                a.error("读取验证XML出错" + var7.toString());
                return null;
            }
        }
    }
}
