package NYTimes;

import java.util.ArrayList;

public class Contract {
    private String status;
    private String copyright;
    Response response;

    public String getStatus() {
        return status;
    }

    public String getCopyright() {
        return copyright;
    }

    public Response getResponse() {
        return response;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setResponse(Response responseObject) {
        this.response = responseObject;
    }
}

