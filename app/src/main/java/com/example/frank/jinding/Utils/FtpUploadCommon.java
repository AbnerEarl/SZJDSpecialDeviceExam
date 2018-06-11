package com.example.frank.jinding.Utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Frank on 2017/11/29.
 */

public class FtpUploadCommon {


    /**
     2.     * ftp上传
     3.     * @param url   ftp地址
     4.     * @param port  ftp连接端口号
     5.     * @param username  登录用户名
     6.     * @param password  登录密码
     7.     * @param fileNamePath  本地文件保存路径
     8.     * @param fileName  本地文件名
     9.     * @return
     10.     */
    public static String ftpUpload(String url, int port, String username,String password,String fileName){
        FTPClient ftpClient = new FTPClient();
            FileInputStream fis = null;
                String returnMessage = "0";
               try {
                       ftpClient.connect(url,port);
                      boolean loginResult = ftpClient.login(username, password);
                        int returnCode = ftpClient.getReplyCode();
                        if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
                             ftpClient.setBufferSize(1024);
                            ftpClient.setControlEncoding("UTF-8");
                         ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            ftpClient.enterLocalPassiveMode();
                               fis = new FileInputStream(fileName);
                             ftpClient.storeFile("test", fis);

                               returnMessage = "1";   //上传成功
                          } else {// 如果登录失败
                            returnMessage = "0";
                       }

               } catch (IOException e) {
                       e.printStackTrace();
          //           throw new RuntimeException("FTP客户端出错！", e);
                     returnMessage = "-1";
               } finally {
                         //IOUtils.closeQuietly(fis);
                        try {
                               ftpClient.disconnect();
                      } catch (IOException e) {
                              e.printStackTrace();
                             throw new RuntimeException("关闭FTP连接发生异常！", e);
                    }
                 }
          return returnMessage;
         }

}
