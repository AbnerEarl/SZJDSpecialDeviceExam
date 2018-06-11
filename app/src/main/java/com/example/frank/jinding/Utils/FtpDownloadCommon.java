package com.example.frank.jinding.Utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Frank on 2017/11/29.
 */

public class FtpDownloadCommon {

    /**
     2.     * ftp下载
     3.     * @param url
     4.     * @param port
     5.     * @param username
     6.     * @param password
     7.     * @param filePath 存放文件的路径
     8.     * @param FTP_file 要下载的文件名
     9.     * @param SD_file 本地文件名
     10.     */
    public static String ftpDown(String url, int port, String username, String password, String filePath, String FTP_file, String SD_file) {
        BufferedOutputStream buffOut = null;

        FTPClient ftpClient = new FTPClient();
        String returnMessage = "0";
        try {
            ftpClient.connect(url, port);
            boolean loginResult = ftpClient.login(username, password);
            int returnCode = ftpClient.getReplyCode();
            if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();

                buffOut = new BufferedOutputStream(new FileOutputStream(filePath + SD_file), 8 * 1024);
                ftpClient.retrieveFile(FTP_file, buffOut);
                buffOut.flush();
                buffOut.close();

                ftpClient.logout();
                ftpClient.disconnect();

                returnMessage = "1";   //上传成功
            } else {// 如果登录失败
                returnMessage = "0";
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
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
