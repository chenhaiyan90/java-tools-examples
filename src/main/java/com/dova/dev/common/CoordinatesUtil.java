package com.dova.dev.common;

public class CoordinatesUtil {

    private final static double EARTH_RADIUS = 6378137.0f;

    /**
     * 计算两个经纬度坐标之间的直线距离
     *
     * @param srcLongitude 经度1
     * @param srcLatitude  纬度1
     * @param dstLongitude 经度2
     * @param dstLatitude  纬度2
     * @return 距离(单位米)
     */
    public static int getDistance(double srcLongitude, double srcLatitude,
                                  double dstLongitude, double dstLatitude) {
        double radLatThis = Math.toRadians(srcLatitude);
        double radLogThat = Math.toRadians(dstLatitude);
        double a = radLatThis - radLogThat;
        double b = Math.toRadians(srcLongitude) - Math.toRadians(dstLongitude);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLatThis) * Math.cos(radLogThat)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return Double.valueOf(s).intValue();
    }


    /**
     * 获取经度偏移
     *
     * @param distance
     * @param lat
     * @return
     */
    public static double getLngOffset(double distance, double lat) {
        double dLng = 2 * Math.asin(Math.sin(distance / (2 * EARTH_RADIUS))
                / Math.cos(Math.toRadians(lat)));
        return Math.toDegrees(dLng);
    }

    /**
     * 获取纬度偏移
     *
     * @param distance
     * @param lat
     * @return
     */
    public static double getLatOffset(double distance, double lat) {
        double dLat = distance / EARTH_RADIUS;
        return Math.toDegrees(dLat);
    }

    public static void main(String[] args) {
        int dis = getDistance(116.414645,40.067637,116.406273,40.074825);
        System.out.println(dis);
    }
}


