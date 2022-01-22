package app.be.response;

import java.util.List;

public class PosterListResponse {

    final int count;

    final List<String> names;

    public PosterListResponse(int count, List<String> names) {
        this.count = count;
        this.names = names;
    }
}
