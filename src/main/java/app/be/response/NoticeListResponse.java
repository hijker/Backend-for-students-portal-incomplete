package app.be.response;

import java.util.Map;

public class NoticeListResponse {

    final int count;

    final Map<String, String> names;

    public NoticeListResponse(int count, Map<String, String> names) {
        this.count = count;
        this.names = names;
    }
}
