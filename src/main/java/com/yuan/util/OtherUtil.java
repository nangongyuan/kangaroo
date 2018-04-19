package com.yuan.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.xml.transform.sax.SAXTransformerFactory;

public class OtherUtil {

    private static String randSouce = "poiuytrewqasdfghjklmnbvcxzQWERTYUIOPLKJHGFDSAZXCVBNM";

    public static Date string2Date(String time) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); //加上时间
        Date date = null;
        try {
            date = sDateFormat.parse(time);
            System.out.println(date);
        } catch (ParseException px) {
            return null;
        }
        return date;
    }

    public static String getSecretkey() {
        int len = 4;
        char[] temp = randSouce.toCharArray();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int rnd = (int) (Math.random() * randSouce.length());
            str.append(temp[rnd]);
        }
        return str.toString();
    }

    public static String sendPost(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(500);
            conn.setReadTimeout(1000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("charset", "UTF-8");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");
            out = new OutputStreamWriter(conn
                    .getOutputStream(), "UTF-8");
            out.write(param);


            out.flush();

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

}
