package com.yzxt.yh.util;

/**
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。 
 */
public class GpsCoordUtil
{
    public static final String BAIDU_LBS_TYPE = "bd09ll";
    public static double pi = 3.1415926535897932384626;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    /**
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84ToGcj02(double lon, double lat)
    {
        if (outOfChina(lon, lat))
        {
            return new double[]
            {lon, lat};
        }
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        double mgLon = lon + dLon;
        double mgLat = lat + dLat;
        return new double[]
        {mgLon, mgLat};
    }

    /**
     * 火星坐标系 (GCJ-02) to 84
     * @param lon
     * @param lat
     * @return
     */
    public static double[] gcjToGps84(double lon, double lat)
    {
        double[] gps = transform(lon, lat);
        double lontitude = lon * 2 - gps[0];
        double latitude = lat * 2 - gps[1];
        return new double[]
        {lontitude, latitude};
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     * @param ggLon
     * @param ggLat
     */
    public static double[] gcj02ToBd09(double ggLon, double ggLat)
    {
        double x = ggLon, y = ggLat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new double[]
        {bdLon, bdLat};
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标
     * @param bdLon
     * @param bdLat
     * @return
     */
    public static double[] bd09ToGcj02(double bdLon, double bdLat)
    {
        double x = bdLon - 0.0065, y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double ggLon = z * Math.cos(theta);
        double ggLat = z * Math.sin(theta);
        return new double[]
        {ggLon, ggLat};
    }

    /**
     * (BD-09)-->84
     * @param bdLon
     * @param bdLat
     * @return
     */
    public static double[] bd09ToGps84(double bdLon, double bdLat)
    {
        double[] gcj02 = bd09ToGcj02(bdLon, bdLat);
        double[] map84 = gcjToGps84(gcj02[0], gcj02[1]);
        return map84;
    }

    private final static boolean outOfChina(double lon, double lat)
    {
        if (lon < 72.004 || lon > 137.8347)
        {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271)
        {
            return true;
        }
        return false;
    }

    private final static double[] transform(double lon, double lat)
    {
        if (outOfChina(lon, lat))
        {
            return new double[]
            {lon, lat};
        }
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        double mgLon = lon + dLon;
        double mgLat = lat + dLat;
        return new double[]
        {mgLon, mgLat};
    }

    private final static double transformLon(double x, double y)
    {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }

    private final static double transformLat(double x, double y)
    {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

}