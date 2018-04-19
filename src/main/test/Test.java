import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.net.HttpResponse;
import com.yuan.dto.RobotMsgDTO;
import com.yuan.dto.SocketDTO;
import com.yuan.entity.LotteryDO;
import com.yuan.entity.RedPacketDO;
import com.yuan.entity.VoteDO;
import com.yuan.entity.VoteGroup;
import com.yuan.util.OtherUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;


import static com.yuan.constant.SocketMsgConstant.ROOM_SPONSOR_RED_PACKET;

/**
 * @program: kangaroo
 * @description: 测试类
 * @author: yuan
 * @create: 2018-03-30 21:47
 **/

public class Test {

    @org.junit.Test
    public void testObj2Json(){
        System.out.println(OtherUtil.string2Date("2018-04-08 21:42:53").getTime());

    }

    @org.junit.Test
    public void testJson2Obj(){
        //  {"type":1,"sender":2,"receiver":3,"data":[1,5,7]}           用户投票的格式
        //  {"type":6,"sender":null,"receiver":5,"data":{"rptitle":"生日快乐","money":88.88,"number":10,"random":1}} 发起红包
        String json = "{\"type\":6,\"sender\":null,\"receiver\":5,\"data\":{\"rptitle\":\"生日快乐\",\"money\":88.88,\"number\":10,\"random\":1}}";
        try {
            SocketDTO socketDTO = new ObjectMapper().readValue(json, SocketDTO.class);
            Map<String, Object> map = (Map<String, Object>) socketDTO.getData();
            System.out.println(map.get("rptitle"));
            System.out.println(map.get("money"));
            System.out.println(map.get("number"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Float> rndRedPacket(float money,int number){
        List<Float> list = new ArrayList<Float>();
        int s = (int) (money*100);
        int t=s;
        s = s - number;
        int sum =0;
        for (int i=0; i<number-1; i++){
            int r = 1+(int) (Math.random()*s);
            s-=r;
            sum +=r;
            list.add(r/100F);
        }
        list.add((t-sum)/100F);
        return list;
    }

    @org.junit.Test
    public  void test3(){
        List<Float> list = rndRedPacket(88.88F,4);
        System.out.println(Arrays.toString(list.toArray()));
    }
    @org.junit.Test
    public void test4(){
        try {
            String pstData = "{\"reqType\":0,\"perception\":{\"inputText\":{\"text\":\"66666666\"},\"inputImage\":{\"url\":\"imageUrl\"},\"selfInfo\":{\"location\":{\"city\":\"北京\",\"province\":\"北京\",\"street\":\"信息路\"}}},\"userInfo\":{\"apiKey\":\"1724285dbd794f1eb20787cd275e7305\",\"userId\":\"2\"}}";
            pstData = new String(pstData.getBytes(),"utf-8");
            //发送POST请求
            URL url = new URL("http://openapi.tuling123.com/openapi/api/v2");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Length", "" + pstData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(pstData);
            out.flush();
            out.close();
            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            System.out.println(result);
            in.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }


    private static String getEncoding(String str){
        String encoding = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(),encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        encoding = "GBK";
        try {
            if (str.equals(new String(str.getBytes(),encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        encoding = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(),encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        encoding = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(),encoding))) {
                return encoding;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @org.junit.Test
    public void test5(){

        String msg = "傻逼";
        System.out.println(OtherUtil.sendPost("http://www.tuling123.com/openapi/api","key=1724285dbd794f1eb20787cd275e7305&info="+msg));


    }
}