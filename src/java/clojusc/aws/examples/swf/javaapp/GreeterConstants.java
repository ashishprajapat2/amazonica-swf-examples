package clojusc.aws.examples.swf.javaapp;

public class GreeterConstants {

    public static final String appApiVersion = "2.0";
    public static final String accessKey = "AWS_ACCESS_KEY_ID";
    public static final String secretKey = "AWS_SECRET_KEY";
    public static final String endpoint = "https://swf.us-west-2.amazonaws.com";
    public static final String domain = "clojusc.hello-world-workflow";
    public static final String clientId = "greeter-client-id";
    public static final String taskListToPoll = "hw-wf-tl";
    public static final int clientTimeout = 70 * 1000;

}
