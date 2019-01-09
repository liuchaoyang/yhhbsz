package com.yzxt.yh.eif.surezen;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MsgEncoder extends ProtocolEncoderAdapter
{
    private static final AttributeKey ENCODER = new AttributeKey(MsgEncoder.class, "encoder");
    private Charset charset;

    public MsgEncoder(Charset charset)
    {
        this.charset = charset;
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception
    {
        CharsetEncoder encoder = (CharsetEncoder) session.getAttribute(ENCODER);
        if (encoder == null)
        {
            encoder = this.charset.newEncoder();
            session.setAttribute(ENCODER, encoder);
        }
        String value = message == null ? "" : message.toString();
        IoBuffer buf = IoBuffer.allocate(value.length()).setAutoExpand(true);
        if (value != null && value.length() > 0)
        {
            buf.putString("[", encoder);
            buf.putString(value, encoder);
            buf.putString("]\r\n", encoder);
        }
        buf.flip();
        out.write(buf);
    }

}
