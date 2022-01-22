package app.be.response;

import java.util.Map;

public class PosterListResponse {

    final int count;

    final Map<String, String> names;

    public PosterListResponse(int count, Map<String, String> names) {
        this.count = count;
        this.names = names;
    }
}
