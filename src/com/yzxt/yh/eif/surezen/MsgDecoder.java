package com.yzxt.yh.eif.surezen;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.textline.TextLineDecoder;

public class MsgDecoder extends TextLineDecoder
{

    public MsgDecoder(Charset charset)
    {
        super(charset, "]");
    }

    @Override
    protected void writeText(IoSession session, String text, ProtocolDecoderOutput out)
    {
        int sPos = text != null ? text.indexOf("[") : -1;
        if (sPos > -1)
        {
            text = text.substring(sPos + 1);
        }
        else
        {
            text = "";
        }
        super.writeText(session, text, out);
    }

}
