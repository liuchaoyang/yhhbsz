package com.yzxt.fw.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.yzxt.fw.server.Config;
import com.yzxt.yh.util.StringUtil;

public class EmailUtil
{
    private static Logger logger = Logger.getLogger(EmailUtil.class);
    private static EmailUtil instance;
    private static Object lock = new Object();

    private boolean emailEnable;
    private String from;
    private String account;
    private String pwd;
    private Properties props = new Properties();

    /**
     * 初始化邮件参数
     */
    private EmailUtil()
    {
        Config config = Config.getInstance();
        emailEnable = config.getBoolean("email.enable", Boolean.FALSE);
        from = config.getString("email.from");
        account = config.getString("email.account");
        pwd = config.getString("email.pwd");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", config.getString("email.smtp.host"));
        props.setProperty("mail.smtp.port", config.getString("email.smtp.port", "25"));
    }

    /**
     * 获取工具类实例
     * @return
     */
    public static EmailUtil getInstance()
    {
        if (instance == null)
        {
            synchronized (lock)
            {
                if (instance == null)
                {
                    instance = new EmailUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 发送邮件
     * @param to 收件人
     * @param subject 标题
     * @param content 内容
     * @return
     * @throws Exception
     */
    public boolean send(String to, String subject, String content) throws Exception
    {
        return send(to, null, null, subject, content, null);
    }

    /**
     * 发送邮件
     * @param to 收件人
     * @param cc 抄送人
     * @param bcc 密送人
     * @param subject 标题
     * @param content 内容
     * @return
     * @throws Exception
     */
    public boolean send(String to, String cc, String bcc, String subject, String content) throws Exception
    {
        return send(to, cc, bcc, subject, content, null);
    }

    /**
     * 发送邮件
     * @param to 收件人
     * @param cc 抄送人
     * @param bcc 密送人
     * @param subject 标题
     * @param content 内容
     * @param attachs 附件
     * @return
     */
    public boolean send(String to, String cc, String bcc, String subject, String content, List<EmailAttach> attachs)
    {
        if (emailEnable)
        {
            try
            {
                Authenticator authenticator = new CustAuthenticator(account, pwd);
                Session session = Session.getDefaultInstance(props, authenticator);
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                if (!StringUtil.isEmpty(cc))
                {
                    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
                }
                if (!StringUtil.isEmpty(bcc))
                {
                    message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
                }
                message.setSubject(subject);
                message.setSentDate(new Date());
                Multipart multipart = new MimeMultipart();
                BodyPart bodyPart = new MimeBodyPart();
                bodyPart.setContent(content, "text/html;charset=utf-8");
                multipart.addBodyPart(bodyPart);
                // 附件暂不处理
                message.setContent(multipart);
                message.saveChanges();
                Transport.send(message);
                return true;
            }
            catch (Exception e)
            {
                logger.error("发送邮件出错", e);
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * 帐号认证类
     */
    private class CustAuthenticator extends Authenticator
    {
        private PasswordAuthentication passwordAuthentication;

        public CustAuthenticator(String userName, String password)
        {
            passwordAuthentication = new PasswordAuthentication(userName, password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication()
        {
            return passwordAuthentication;
        }
    }

}
