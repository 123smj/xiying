/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  com.sun.mail.util.MailSSLSocketFactory
 *  javax.mail.Address
 *  javax.mail.Message
 *  javax.mail.MessagingException
 *  javax.mail.Session
 *  javax.mail.Transport
 *  javax.mail.internet.InternetAddress
 *  javax.mail.internet.MimeMessage
 */
package com.gy.util;

import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
    private static final String DEFAUL_FROM = "757174266@qq.com";
    private static final String DEFAUL_FROM_AUTHCODE = "dhfmrruqqhmdbedc";

    public static void sendEmal(String fromAddr, String fromAuthCode, String toAddr, String title, String comtent) throws MessagingException, GeneralSecurityException {
        Properties props = new Properties();
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.host", "smtp.qq.com");
        props.setProperty("mail.transport.protocol", "smtp");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getInstance((Properties) props);
        MimeMessage msg = new MimeMessage(session);
        msg.setSubject(title);
        msg.setText(comtent);
        msg.setFrom((Address) new InternetAddress(fromAddr));
        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", fromAddr, fromAuthCode);
        transport.sendMessage((Message) msg, new Address[]{new InternetAddress(toAddr)});
        transport.close();
    }

    public static void sendEmal(String toAddr, String title, String comtent) throws MessagingException, GeneralSecurityException {
        EmailUtil.sendEmal("757174266@qq.com", "dhfmrruqqhmdbedc", toAddr, title, comtent);
    }

    public static void main(String[] args) {
        try {
            EmailUtil.sendEmal("757174266@qq.com", "CESHI", "\u6d4b\u8bd5\u53d1\u9001\u81ea\u5df1");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
