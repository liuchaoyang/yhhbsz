package com.yzxt.yh.eif.mp;

import java.util.Date;

public class MpSender
{

    /**
     * 发送GPS数据
     * "*IMEI,GPS,checkTime,Longitude,Latitude,speed,direction,altitude#"
     * @param extDeviceNo 扩展设备编号，设备类型设备编号
     * @param longitude
     * @param latitude
     * @return
     */
    public static int sendGps(String extDeviceNo, Date checkTime, Double longitude, Double latitude)
    {
        MpProxy mpServer = MpProxy.getInstance();
        if (mpServer == null)
        {
            return MpConst.OPER_STATE_SERVER_NOT_RUNNING;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(extDeviceNo).append(",GPS,")
                .append(MpConst.daf.format(checkTime != null ? checkTime : new Date()));
        sb.append(",").append(MpConst.def.format(longitude)).append(",").append(MpConst.def.format(latitude))
                .append(",").append("0,0,0").append("#");
        int r = mpServer.send(sb.toString());
        return r;
    }

    /**
     * 发送LBS数据
     * "*IMEI,LBS,checkTime,Mnc,Lac,Cellid#"
     * @param extDeviceNo 扩展设备编号，设备类型设备编号
     * @param checkTime
     * @param mnc
     * @param lac
     * @param cellid
     * @return
     */
    public static int sendLbs(String extDeviceNo, Date checkTime, String mnc, String lac, String cellid)
    {
        MpProxy mpServer = MpProxy.getInstance();
        if (mpServer == null)
        {
            return MpConst.OPER_STATE_SERVER_NOT_RUNNING;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(extDeviceNo).append(",LBS,")
                .append(MpConst.daf.format(checkTime != null ? checkTime : new Date()));
        sb.append(",").append(mnc).append(",").append(lac).append(",").append(cellid).append("#");
        int r = mpServer.send(sb.toString());
        return r;
    }

    /**
     * 发送电子围栏信息
     * *IMEI,AlarmSet, Longitude,Latitude, distance #
     * @param extDeviceNo
     * @param longitude
     * @param latitude
     * @param distance 离开设置点的距离
     * @return
     */
    public static int sendFenceSet(String extDeviceNo, Double longitude, Double latitude, int distance)
    {
        MpProxy mpServer = MpProxy.getInstance();
        if (mpServer == null)
        {
            return MpConst.OPER_STATE_SERVER_NOT_RUNNING;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(extDeviceNo).append(",AlarmSet");
        sb.append(",").append(MpConst.def.format(longitude)).append(",").append(MpConst.def.format(latitude))
                .append(",").append(distance).append("#");
        int r = mpServer.send(sb.toString());
        return r;
    }

}
