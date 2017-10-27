package demo.ares.com.a10086msghelper;

import java.io.Serializable;

/**
 * Created on 2017/10/27 01:36.
 *
 * @author ares
 */

public class Message10086 implements Serializable{

    private boolean status ;
    private String content;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
