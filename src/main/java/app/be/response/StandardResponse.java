package app.be.response;

public class StandardResponse {

    final String message;

    public StandardResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
