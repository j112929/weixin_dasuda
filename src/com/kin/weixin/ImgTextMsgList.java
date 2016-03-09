package com.kin.weixin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kone
 * Date: 13-5-22
 * Time: 下午11:05
 * To change this template use File | Settings | File Templates.
 */
public class ImgTextMsgList {
    private int count;
    private int totalCount;
    private String type;
    private String subtype;
    private List<ImgTextMsg> list = new ArrayList<ImgTextMsg>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public List<ImgTextMsg> getList() {
        return list;
    }

    public void setList(List<ImgTextMsg> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ImgTextMsgList{" +
                "count=" + count +
                ", totalCount=" + totalCount +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                ", list=" + list +
                '}';
    }

    public static class ImgTextMsg {
        private String appId;
        private int count;
        private String time;
        private List<ImgText> appmsgList = new ArrayList<ImgText>();

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ImgText> getAppmsgList() {
            return appmsgList;
        }

        public void setAppmsgList(List<ImgText> appmsgList) {
            this.appmsgList = appmsgList;
        }

        @Override
        public String toString() {
            return "ImgTextMsg{" +
                    "appId='" + appId + '\'' +
                    ", count=" + count +
                    ", time='" + time + '\'' +
                    ", appmsgList=" + appmsgList +
                    '}';
        }

        public static class ImgText {
            private String imgURL;
            private String title;
            private String desc;
            private String url;
            private int ClickCount;
            private int ClickMemberCount;

            public String getImgURL() {
                return imgURL;
            }

            public void setImgURL(String imgURL) {
                this.imgURL = imgURL;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getClickCount() {
                return ClickCount;
            }

            public void setClickCount(int clickCount) {
                ClickCount = clickCount;
            }

            public int getClickMemberCount() {
                return ClickMemberCount;
            }

            public void setClickMemberCount(int clickMemberCount) {
                ClickMemberCount = clickMemberCount;
            }

            @Override
            public String toString() {
                return "ImgText{" +
                        "imgURL='" + imgURL + '\'' +
                        ", title='" + title + '\'' +
                        ", desc='" + desc + '\'' +
                        ", url='" + url + '\'' +
                        ", ClickCount=" + ClickCount +
                        ", ClickMemberCount=" + ClickMemberCount +
                        '}';
            }
        }
    }


}
