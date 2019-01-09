package com.yzxt.yh.eif.surezen;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MsgCodecFactory implements ProtocolCodecFactory
{
    private MsgEncoder msgEncoder;
    private MsgDecoder msgDecoder;

    public MsgCodecFactory(Charset charset)
    {
        msgEncoder = new MsgEncoder(charset);
        msgDecoder = new MsgDecoder(charset);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession paramIoSession) throws Exception
    {
        return msgEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession paramIoSession) throws Exception
    {
        return msgDecoder;
    }

}
