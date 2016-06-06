package com.dova.dev.common;

/**
 * Created by liuzhendong on 16/5/25.
 */

import org.apache.commons.mail.*;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.util.List;

public class EmailUtil {
    public final static String SMTP     = "XXX";
    public final static String USERNAME = "XXX";
    public final static String PASSWORD = "XXX";
    public final static String FROM     = "XXX";

    public static void send(String subject, String content, List<String> to) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(SMTP);
        email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
        email.setSSLOnConnect(true);

        email.setSubject(subject);
        email.setFrom(FROM);
        email.setMsg(content);

        email.addCc("lzz25yi@126.com");

        for (String t : to) {
            email.addTo(t);
        }
        email.send();
    }


    public static void sendAttach(String subject, String content, String attachContent, List<String> to) throws Exception {
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(SMTP);
        email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
        email.setSSLOnConnect(true);

        email.addCc("lzz25yi@126.com");

        email.setSubject(subject);
        email.setFrom(FROM);
        email.setMsg(content);

        for (String t : to) {
            email.addTo(t);
        }

        DataSource source = new ByteArrayDataSource(attachContent.getBytes("utf-8"),"application/octet-stream");
        email.attach(source, subject+".txt", "the detailed data");
        email.send();
    }
}
