package omc.spop.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
public class SFtpTool {
    private Session session = null;    
    private Channel channel = null; 
    private ChannelSftp channelSftp = null;
    
    public void init(String host, String userName, String password, int port) {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(userName, host, port);
            session.setPassword(password);
 
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
 
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
 
        channelSftp = (ChannelSftp) channel; 
    }    
    
    public boolean upload(String dir, String filePath) {
        boolean result = true;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            String fileName = file.getName();

            in = new FileInputStream(file);
            channelSftp.cd(dir);
            channelSftp.put(in, fileName);
            
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }    
}