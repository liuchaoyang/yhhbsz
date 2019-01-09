package com.yzxt.yh.eif.surezen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.BeanFactory;

import com.yzxt.fw.server.BeanFactoryHelper;
import com.yzxt.fw.server.Config;

public class SurezenServer extends Thread
{
    private Logger logger = Logger.getLogger(SurezenServer.class);
    private static IoAcceptor acceptor;

    /**
     * 河北循证腕表服务
     * @throws IOException
     */
    public void begin() throws Exception
    {
        try
        {
            BeanFactory beanFactory = BeanFactoryHelper.getBeanfactory();
            if (beanFactory == null)
            {
                logger.error("BeanFactory不存在，忽略河北循证腕表服务。");
                return;
            }
            SurezenHandleProxy surezenHandleProxy = (SurezenHandleProxy) beanFactory.getBean("surezenHandleProxy");
            Config config = Config.getInstance();
            Integer port = config.getInteger("surezen.server.port");
            if (port == null || port.intValue() == 0)
            {
                logger.debug("未配置河北循证腕表服务端口号，取消启动河北循证腕表服务。");
                return;
            }
            String encoding = config.getString("surezen.server.encoding", "UTF-8");
            // 打开通道
            acceptor = new NioSocketAcceptor();
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
            acceptor.getFilterChain().addLast("codec",
                    new ProtocolCodecFilter(new MsgCodecFactory(Charset.forName(encoding))));
            acceptor.setHandler(surezenHandleProxy);
            acceptor.getSessionConfig().setReadBufferSize(1024);
            // 空闲时间（秒）
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30 * 60);
            acceptor.bind(new InetSocketAddress(port));
        }
        catch (Exception e)
        {
            logger.error("启动河北循证腕表服务出错。", e);
            throw e;
        }
    }

    /**
     * 关闭河北循证腕表服务
     */
    public void shutdown()
    {
        if (acceptor != null && !acceptor.isDisposed())
        {
            acceptor.dispose(true);
        }
    }

}
