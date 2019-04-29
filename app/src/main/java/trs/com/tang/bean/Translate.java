package trs.com.tang.bean;

import java.util.List;

public class Translate {

    /**
     * tSpeakUrl : http://openapi.youdao.com/ttsapi?q=you&langType=en&sign=38C37BDC96E01439C2C07D2789B6AFD1&salt=1548478295267&voice=4&format=mp3&appKey=2311424c849c3b08
     * web : [{"value":["You","white people","fuck you","Thou"],"key":"你"},{"value":["your","yr year","Freund","Wie geht es Ihrem"],"key":"你的"},{"value":["Invites you over","I nvites you","Invite you","inviting with you"],"key":"邀请你"}]
     * query : 你
     * translation : ["you"]
     * errorCode : 0
     * dict : {"url":"yddict://m.youdao.com/dict?le=eng&q=%E4%BD%A0"}
     * webdict : {"url":"http://m.youdao.com/dict?le=eng&q=%E4%BD%A0"}
     * basic : {"phonetic":"nǐ","explains":["you","thou","thee","ye"]}
     * l : zh-CHS2en
     * speakUrl : http://openapi.youdao.com/ttsapi?q=%E4%BD%A0&langType=zh-CHS&sign=C9EBBA02B2EC0790431912F0A30553DF&salt=1548478295267&voice=4&format=mp3&appKey=2311424c849c3b08
     */

    private String tSpeakUrl;
    private String query;
    private String errorCode;
    private DictBean dict;
    private WebdictBean webdict;
    private BasicBean basic;
    private String l;
    private String speakUrl;
    private List<WebBean> web;
    private List<String> translation;

    public String getTSpeakUrl() {
        return tSpeakUrl;
    }

    public void setTSpeakUrl(String tSpeakUrl) {
        this.tSpeakUrl = tSpeakUrl;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public DictBean getDict() {
        return dict;
    }

    public void setDict(DictBean dict) {
        this.dict = dict;
    }

    public WebdictBean getWebdict() {
        return webdict;
    }

    public void setWebdict(WebdictBean webdict) {
        this.webdict = webdict;
    }

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getSpeakUrl() {
        return speakUrl;
    }

    public void setSpeakUrl(String speakUrl) {
        this.speakUrl = speakUrl;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public static class DictBean {
        /**
         * url : yddict://m.youdao.com/dict?le=eng&q=%E4%BD%A0
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class WebdictBean {
        /**
         * url : http://m.youdao.com/dict?le=eng&q=%E4%BD%A0
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class BasicBean {
        /**
         * phonetic : nǐ
         * explains : ["you","thou","thee","ye"]
         */

        private String phonetic;
        private List<String> explains;

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }
    }

    public static class WebBean {
        /**
         * value : ["You","white people","fuck you","Thou"]
         * key : 你
         */

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }
}
