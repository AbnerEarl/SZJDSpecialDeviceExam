package com.example.frank.jinding.Upload;

import android.app.Activity;

import com.example.frank.jinding.Conf.URLConfig;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File description.
 *
 * @author Frank
 * @date 2018/1/28
 * @emial 1320259466@qq.com
 * @description (about file's use)
 */

public class FtpClientUpload {


    public static void UploadFile(final String filePath, final String remotepaht, final Activity activity, final String filename){

        //final String company="/SZJD";
        //final String rootPath = "/usr/local/tomcat8/webapps/ExamSpotRecordStorage/SZJD/";//阿里云目录
        //final String rootPath = "/opt/tomcat/webapps/ExamSpotRecordStorage/SZJD/";//腾讯云目录
        final String rootPath = "/SZJD/";//腾讯云windows服务器目录
        //final String rootPath = File.separator+"usr"+File.separator+"local"+File.separator+"tomcat8"+File.separator+"webapps"+File.separator+"ExamSpotRecordStorage"+File.separator+"SZJD"+File.separator;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FTPClient ftpClient = new FTPClient();
                    FileInputStream fis = null;
                    int returnMessage = 0;
                    String url = URLConfig.FtpURL;
                    int port = 21;
                    String username = "myftp";
                    String password = "myftp";
                    //String remotePath = rootPath; //改动的地方，不直接使用根目录
                    String remotePath = rootPath;
                    String remoteFileName = filename;
                    try {
                        ftpClient.connect(url, port);
                        boolean loginResult = ftpClient.login(username, password);
                        int returnCode = ftpClient.getReplyCode();

                        if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功
                            ftpClient.changeWorkingDirectory(rootPath);
                            System.out.println("==============登录成功");
                            String pathss[]=remotepaht.split("/");
                            for (int i=0;i<pathss.length;i++){
                                remotePath=remotePath+pathss[i]+"/";
                                if (ftpClient.changeWorkingDirectory(remotePath)){
                                    continue;
                                }else {
                                    Boolean dd=ftpClient.makeDirectory(remotePath);
                                    System.out.println("==============创建目录情况"+dd);
                                }
                            }

                            // 设置上传目录
                            ftpClient.changeWorkingDirectory(remotePath);
                            ftpClient.setBufferSize(10*1024*1024);
                            ftpClient.setControlEncoding("UTF-8");
                            ftpClient.enterLocalPassiveMode();         // 被动模式

                            fis = new FileInputStream(filePath);
                            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                            boolean isSuccess = ftpClient.storeFile(remoteFileName, fis);
                            returnMessage = 1; // 上传成功


                            fis.close();
                        }
                        else {
                            // 如果登录失败
                            returnMessage = 0;
                            System.out.println("==============登录失败！！！");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("FTP客户端出错！", e);
                    } finally {
                        // IOUtils.closeQuietly(fis);
                        try {
                            ftpClient.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("关闭FTP连接发生异常！", e);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }





    public static void DeleteFile( final String remotepaht, final String filename){

        //final String company="/SZJD";
        //final String rootPath = "/usr/local/tomcat8/webapps/ExamSpotRecordStorage/SZJD/";
        //final String rootPath = "/opt/tomcat/webapps/ExamSpotRecordStorage/SZJD/";//腾讯云服务器
        final String rootPath = "/SZJD/";//腾讯云windows服务器
        //final String rootPath = File.separator+"usr"+File.separator+"local"+File.separator+"tomcat8"+File.separator+"webapps"+File.separator+"ExamSpotRecordStorage"+File.separator+"SZJD"+File.separator;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FTPClient ftpClient = new FTPClient();
                    int returnMessage = 0;
                    String url = URLConfig.FtpURL;
                    int port = 21;
                    String username = "myftp";
                    String password = "myftp";
                    String remotePath = rootPath;
                    String remoteFileName = filename;
                    try {
                        ftpClient.connect(url, port);
                        boolean loginResult = ftpClient.login(username, password);
                        int returnCode = ftpClient.getReplyCode();
                        if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {// 如果登录成功

                            ftpClient.changeWorkingDirectory(remotePath+remotepaht);
                            ftpClient.setBufferSize(10*1024*1024);
                            ftpClient.setControlEncoding("UTF-8");
                            ftpClient.enterLocalPassiveMode();         // 被动模式
                            ftpClient.deleteFile(filename);

                            returnMessage = 1;

                        }
                        else {
                            // 如果登录失败
                            returnMessage = 0;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException("FTP客户端出错！", e);
                    } finally {
                        // IOUtils.closeQuietly(fis);
                        try {
                            ftpClient.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("关闭FTP连接发生异常！", e);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



}
