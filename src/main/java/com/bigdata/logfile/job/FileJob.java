package com.bigdata.logfile.job;


import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.net.NetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;

public class FileJob {
    private static  Log log = LogFactory.get();

    public String uploadPath="D:/log/api/";
    //发送请求地址
    public String url="http://10.204.106.180:18443/api/auth/create";
    //发送token请求地址
    public String getTokenUrl="http://10.204.106.180:18443/api/auth/login";

    //上传日志文件请求地址
    public String uploadLogFileUrl="http://10.204.106.180:18443/storage/upload/file";
    //上传日志摘要请求地址
    public String uploadFileDigestUrl="http://10.204.106.180:18443/storage/input";

    public String account="fzdsjwcdhxm";
    public String password="dsj123456";

    public static Cache<String,String> fifoCache = CacheUtil.newFIFOCache(3);
    public void execute(){
        //先注册

//        log.info("注册");


//        String authcreate =authCreate(account,password,url);
//        log.info("注册返回结果:"+authcreate);
//        JSONObject authcreateResult =JSONUtil.parseObj(authcreate);
//        if("00000".equals(authcreateResult.get("code"))){
//            log.info("注册成功");
//        }else{
//            log.info("注册失败:"+authcreateResult.get("code"));
//        }






        //然后获取token，有效时间4个小时
        log.info("获取token");
//        if(fifoCache.get("token")==null){
            String getTokenResult =getToken(account,password,getTokenUrl);
            log.info("获取到的token返回结果:"+getTokenResult);
            JSONObject getTokenResultObj =JSONUtil.parseObj(getTokenResult);
            if("00000".equals(getTokenResultObj.get("code"))){
                log.info("获取token成功");
                log.info("获取到的token:"+getTokenResultObj.get("data"));
                //保存到缓存中

//                fifoCache.put("token",getTokenResultObj.get("data").toString());
                ArrayList<File> FileNames =new ArrayList<>();
                String today= DateUtil.today();
                System.out.println("today:"+today);
                File[] ls = FileUtil.ls(uploadPath);
                for (int i =0;i<ls.length;i++){
                    File f =ls[i];
                    String fileName = f.getName();
                    if("api.log".equals(fileName)){
                        continue;
                    }
                    String []fileDate=fileName.split("\\.");
                    if(fileDate[1].equals(today)){
                        FileNames.add(f);
                    }
                }

                if(FileNames.size()>0){

                    FileNames.forEach((e) -> {
                        String uploadLogFileResult = uploadLogFile(account, uploadLogFileUrl, e, getTokenResultObj.get("data").toString());
                        log.info(e.getName()+" 上传文件返回结果"+uploadLogFileResult);
//                        Date date = DateUtil.date(System.currentTimeMillis());
//                        InetAddress localhost = NetUtil.getLocalhost();
//                        JSONObject dataIndex=JSONUtil.createObj().put("host",localhost.getHostAddress()).put("type","访客预约平台").put("timestamp",((DateTime) date).toTimestamp());
//                        JSONObject authCreateObj = JSONUtil.createObj().put("dataUser",account).put("dataHash", SecureUtil.md5(e.getName())).put("dataName","apilog").put("dataIndex",dataIndex);
//                        JSONObject requestObj =JSONUtil.createObj().put("head",authCreateObj);
//
//                        uploadFile(uploadLogFileUrl,getTokenResultObj.get("data").toString(),requestObj.toString(),e);
                    });
                }
            }else{
                log.info("获取token失败:"+getTokenResultObj.get("code"));

            }
//        }





//        ArrayList<File> FileNames =new ArrayList<>();
//        String today= DateUtil.today();
//        System.out.println("today:"+today);
//        File[] ls = FileUtil.ls(uploadPath);
//        for (int i =0;i<ls.length;i++){
//            File f =ls[i];
//         String fileName = f.getName();
//          if("api.log".equals(fileName)){
//             continue;
//          }
//          String []fileDate=fileName.split("\\.");
//          if(fileDate[1].equals(today)){
//              FileNames.add(f);
//          }
//        }
//        StringBuilder sb = new StringBuilder();
//        FileReader fileReader = new FileReader(FileNames.get(0));
//
//
//        List<String> results = fileReader.readLines();

//        for (int i = 0; i <= 3; i++) {
//            System.out.println(results.get(i));
//        }


//        if(FileNames.size()>0){
//
//            FileNames.forEach((e) -> {
                //然后上传所有日志文件
//                uploadLogFile(account,uploadLogFileUrl,e);

                //这个是上传日志摘要方法
//                FileReader fileUploadReader = new FileReader(e);
//                List<String> UploadReaderresults = fileUploadReader.readLines();
//                for (int i = 0; i <= 20; i++) {
//                    sb.append(UploadReaderresults.get(i)+"\\r\\n");
////                  System.out.println(UploadReaderresults.get(i));
//                }
//                uploadLogFileDigest(account,uploadLogFileUrl,e,sb.toString());
//            });

//        }


    }

    //用户注册用户请求
    public static String authCreate(String account,String password,String url){
        JSONObject authCreateObj = JSONUtil.createObj().put("account",account).put("password",password);
        Map<String, String > heads = new HashMap<>();
        heads.put("Content-Type", "application/json;charset=UTF-8");
//        JSONObject param = new JSONObject();
//        param.put("account", account);
//        param.put("password", password);
        String result = HttpRequest.post(url)
                .header(Header.USER_AGENT, "http")//头信息，多个头信息多次调用此方法即可
                .header(Header.CONTENT_TYPE,"application/json;charset=UTF-8")
                .body(authCreateObj.toString())
                .timeout(20000)//超时，毫秒
                .execute().body();
        return  result;
    }


    //获取token请求
    public static String getToken(String account,String password,String url){
        JSONObject authCreateObj = JSONUtil.createObj().put("user",account).put("password",password);
        String result = HttpRequest.get(url+"?user="+account+"&password="+password)
                .header(Header.USER_AGENT, "Hutool http")//头信息，多个头信息多次调用此方法即可
//                .form(authCreateObj.toString())//表单内容
                .header(Header.USER_AGENT, "Hutool http")
                .header("user", account)
                .header("password",password)
//                .body(authCreateObj.toString())
                .timeout(20000)//超时，毫秒
                .execute().body();
        return  result;
    }

    //上传文件请求
    public static String uploadLogFile(String account,String url,File file,String token){
        String now = DateUtil.now();
        Date date = DateUtil.date(System.currentTimeMillis());
        InetAddress localhost = NetUtil.getLocalhost();
//        JSONObject fileObj = JSONUtil.createObj().put("file",file);
        JSONObject dataIndex=JSONUtil.createObj().put("host",localhost.getHostAddress()).put("type","访客预约平台").put("timestamp",now);
        JSONObject authCreateObj = JSONUtil.createObj().put("dataUser",account).put("dataHash", SecureUtil.md5(file.getName())).put("dataName","apilog").put("dataIndex",dataIndex);
        JSONObject requestObj =JSONUtil.createObj().put("head",authCreateObj);
        String result = HttpRequest.post(url)
                .header(Header.USER_AGENT, "http")//头信息，多个头信息多次调用此方法即可
//                .bearerAuth(token)
                .header(Header.AUTHORIZATION,"Bearer"+token)
                .header(Header.CONTENT_TYPE,"multipart/form-data;")
                .form("input",requestObj.toString())//表单内容
                .form("file",file)
                .timeout(20000)//超时，毫秒
                .execute().body();
        return  result;
    }

    //上传日志摘要
    public static String uploadLogFileDigest(String account,String url,File file,String digest){
        Date date = DateUtil.date(System.currentTimeMillis());
        InetAddress localhost = NetUtil.getLocalhost();
//        JSONObject fileObj = JSONUtil.createObj().put("file",file);
        JSONObject dataIndex=JSONUtil.createObj().put("host",localhost.getHostAddress()).put("type","访客预约平台").put("timestamp",((DateTime) date).toTimestamp());
        JSONObject authCreateObj = JSONUtil.createObj().put("dataUser",account).put("dataHash", SecureUtil.md5(file.getName())).put("dataName","apilog").put("dataIndex",dataIndex);
        JSONObject requestObj =JSONUtil.createObj().put("head",authCreateObj).put("body",digest);
        String result = HttpRequest.get(url)
                .header(Header.USER_AGENT, "Authorization:"+fifoCache.get("token"))//头信息，多个头信息多次调用此方法即可
                .form(requestObj.toString())//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        return  result;
    }




    public static void uploadFile(String host, String auth, String  input,File file) {

        HttpURLConnection httpURLConnection = null;

        try {
            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器的域名
            URL url = new URL(host);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            httpURLConnection.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            // 设置请求头参数
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charsert", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
            if(auth != null ) {
                httpURLConnection.addRequestProperty("Authorization", "Bearer " + auth);
            }
            OutputStream out = httpURLConnection.getOutputStream();
            // 上传文件
//            File file = new File(fileName);
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改

            String content = "--" + BOUNDARY + "\r\n";
            content       += "Content-Disposition: form-data; name=\"input\"" + "\r\n\r\n";
            content       += input;
            content       += "\r\n--" + BOUNDARY + "\r\n";
            content       += "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n";
            content       += "Content-Type: image/jpeg\r\n\r\n";

            sb.append(content);
//            sb.append("Content-Disposition: form-data;name=\""+input+"\";filename=\""+ fileName + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            //定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
        } catch (Exception e) {
            log.info("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
    }
}
