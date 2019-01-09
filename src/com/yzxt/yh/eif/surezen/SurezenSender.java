package com.yzxt.yh.eif.surezen;

import java.util.Date;

import org.apache.mina.core.session.IoSession;

import com.yzxt.yh.util.StringUtil;

public class SurezenSender
{
    /**
     * 发送腕表配置到腕表上，需要补充token和时间
     * 命令类似 V1.0.0,8569853324455,,S81,1,,41,1,170.0,67.0,51
     * @param deviceNo
     * @param configCmd
     * @return
     */
    public static int send(String deviceNo, String configCmd)
    {
        SessionMap sessionMap = SessionMap.getInstance();
        IoSession s = sessionMap.get(deviceNo);
        if (s == null || !s.isConnected())
        {
            return SurezenConst.SEND_STATE_OFFLINE;
        }
        StringBuilder sb = new StringBuilder(configCmd);
        // token位置和时间位置
        int tokenIdx = -1;
        int timeIdx = -1;
        int slitC = 0;
        int len = configCmd != null ? configCmd.length() : 0;
        for (int i = 0; i < len; i++)
        {
            if (configCmd.charAt(i) == ',')
            {
                ++slitC;
                if (slitC == 2)
                {
                    tokenIdx = i + 1;
                }
                else if (slitC == 5)
                {
                    timeIdx = i + 1;
                }
            }
        }
        // 格式正常
        if (timeIdx > -1)
        {
            sb.insert(timeIdx, SurezenHandle.timeF.format(new Date()));
            sb.insert(tokenIdx, StringUtil.ensureStringNotNull((String) s.getAttribute(SurezenConst.SESSION_KEY_TOKEN)));
        }
        s.write(sb);
        return SurezenConst.SEND_STATE_SUCCESS;
    }
}
