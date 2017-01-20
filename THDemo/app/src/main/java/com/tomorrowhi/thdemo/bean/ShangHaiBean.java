package com.tomorrowhi.thdemo.bean;

import java.util.List;

/**
 * Created by zhaotaotao on 19/01/2017.
 * beanDemo
 */
public class ShangHaiBean {
    /**
     * idx : 1437
     * aqi : 162
     * time : {"v":1484830800,"s":"2017-01-19 13:00:00","tz":"+08:00"}
     * city : {"name":"Shanghai","url":"http://aqicn.org/city/shanghai/","geo":["31.2047372","121.4489017"]}
     * attributions : [{"name":"Shanghai Environment Monitoring Center(上海市环境监测中心)","url":"http://www.semc.gov.cn/"},{"name":"China National Urban air quality real-time publishing platform (全国城市空气质量实时发布平台)","url":"http://113.108.142.147:20035/emcpublish/"},{"name":"U.S. Consulate Shanghai Air Quality Monitor","url":"http://shanghai.usembassy-china.org.cn/airmonitor.html"},{"name":"World Air Quality Index Project","url":"http://waqi.info/"}]
     * iaqi : {"pm25":{"v":162},"pm10":{"v":63},"o3":{"v":28},"no2":{"v":28},"so2":{"v":12},"co":{"v":9},"t":{"v":8},"d":{"v":4},"p":{"v":1025},"h":{"v":76},"w":{"v":3}}
     */

    private int idx;
    private int aqi;
    private TimeBean time;
    private CityBean city;
    private IaqiBean iaqi;
    private List<AttributionsBean> attributions;

    @Override
    public String toString() {
        return "ShangHaiBean{" +
                "idx=" + idx +
                ", aqi=" + aqi +
                ", time=" + time +
                ", city=" + city +
                ", iaqi=" + iaqi +
                ", attributions=" + attributions +
                '}';
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public TimeBean getTime() {
        return time;
    }

    public void setTime(TimeBean time) {
        this.time = time;
    }

    public CityBean getCity() {
        return city;
    }

    public void setCity(CityBean city) {
        this.city = city;
    }

    public IaqiBean getIaqi() {
        return iaqi;
    }

    public void setIaqi(IaqiBean iaqi) {
        this.iaqi = iaqi;
    }

    public List<AttributionsBean> getAttributions() {
        return attributions;
    }

    public void setAttributions(List<AttributionsBean> attributions) {
        this.attributions = attributions;
    }

    public static class TimeBean {

        @Override
        public String toString() {
            return "TimeBean{" +
                    "v=" + v +
                    ", s='" + s + '\'' +
                    ", tz='" + tz + '\'' +
                    '}';
        }

        /**
         * v : 1484830800
         * s : 2017-01-19 13:00:00
         * tz : +08:00
         */

        private int v;
        private String s;
        private String tz;

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public String getTz() {
            return tz;
        }

        public void setTz(String tz) {
            this.tz = tz;
        }
    }

    public static class CityBean {

        @Override
        public String toString() {
            return "CityBean{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", geo=" + geo +
                    '}';
        }

        /**
         * name : Shanghai
         * url : http://aqicn.org/city/shanghai/
         * geo : ["31.2047372","121.4489017"]
         */

        private String name;
        private String url;
        private List<String> geo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<String> getGeo() {
            return geo;
        }

        public void setGeo(List<String> geo) {
            this.geo = geo;
        }
    }

    public static class IaqiBean {
        @Override
        public String toString() {
            return "IaqiBean{" +
                    "pm25=" + pm25 +
                    ", pm10=" + pm10 +
                    ", o3=" + o3 +
                    ", no2=" + no2 +
                    ", so2=" + so2 +
                    ", co=" + co +
                    ", t=" + t +
                    ", d=" + d +
                    ", p=" + p +
                    ", h=" + h +
                    ", w=" + w +
                    '}';
        }

        /**
         * pm25 : {"v":162}
         * pm10 : {"v":63}
         * o3 : {"v":28}
         * no2 : {"v":28}
         * so2 : {"v":12}
         * co : {"v":9}
         * t : {"v":8}
         * d : {"v":4}
         * p : {"v":1025}
         * h : {"v":76}
         * w : {"v":3}
         */

        private IaqiBean.Pm25Bean pm25;
        private IaqiBean.Pm10Bean pm10;
        private IaqiBean.O3Bean o3;
        private IaqiBean.No2Bean no2;
        private IaqiBean.So2Bean so2;
        private IaqiBean.CoBean co;
        private IaqiBean.TBean t;
        private IaqiBean.DBean d;
        private IaqiBean.PBean p;
        private IaqiBean.HBean h;
        private IaqiBean.WBean w;

        public IaqiBean.Pm25Bean getPm25() {
            return pm25;
        }

        public void setPm25(IaqiBean.Pm25Bean pm25) {
            this.pm25 = pm25;
        }

        public IaqiBean.Pm10Bean getPm10() {
            return pm10;
        }

        public void setPm10(IaqiBean.Pm10Bean pm10) {
            this.pm10 = pm10;
        }

        public IaqiBean.O3Bean getO3() {
            return o3;
        }

        public void setO3(IaqiBean.O3Bean o3) {
            this.o3 = o3;
        }

        public IaqiBean.No2Bean getNo2() {
            return no2;
        }

        public void setNo2(IaqiBean.No2Bean no2) {
            this.no2 = no2;
        }

        public IaqiBean.So2Bean getSo2() {
            return so2;
        }

        public void setSo2(IaqiBean.So2Bean so2) {
            this.so2 = so2;
        }

        public IaqiBean.CoBean getCo() {
            return co;
        }

        public void setCo(IaqiBean.CoBean co) {
            this.co = co;
        }

        public IaqiBean.TBean getT() {
            return t;
        }

        public void setT(IaqiBean.TBean t) {
            this.t = t;
        }

        public IaqiBean.DBean getD() {
            return d;
        }

        public void setD(IaqiBean.DBean d) {
            this.d = d;
        }

        public IaqiBean.PBean getP() {
            return p;
        }

        public void setP(IaqiBean.PBean p) {
            this.p = p;
        }

        public IaqiBean.HBean getH() {
            return h;
        }

        public void setH(IaqiBean.HBean h) {
            this.h = h;
        }

        public IaqiBean.WBean getW() {
            return w;
        }

        public void setW(IaqiBean.WBean w) {
            this.w = w;
        }

        public static class Pm25Bean {
            /**
             * v : 162
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class Pm10Bean {
            /**
             * v : 63
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class O3Bean {
            /**
             * v : 28
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class No2Bean {
            /**
             * v : 28
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class So2Bean {
            /**
             * v : 12
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class CoBean {
            /**
             * v : 9
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class TBean {
            /**
             * v : 8
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class DBean {
            /**
             * v : 4
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class PBean {
            /**
             * v : 1025
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class HBean {
            /**
             * v : 76
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }

        public static class WBean {
            /**
             * v : 3
             */

            private int v;

            public int getV() {
                return v;
            }

            public void setV(int v) {
                this.v = v;
            }
        }
    }

    public static class AttributionsBean {

        @Override
        public String toString() {
            return "AttributionsBean{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        /**
         * name : Shanghai Environment Monitoring Center(上海市环境监测中心)
         * url : http://www.semc.gov.cn/
         */

        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

