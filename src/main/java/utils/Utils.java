package utils;

public class Utils {

    private static final String ALTERNATE_EXCHANGE_NAME_POSTFIX = "ae";
    private static final String MASTER_EXCHANGE_NAME_POSTFIX = "me";

    public static String getAlternateExchangeName(String exchangeName) {
        return exchangeName + "-" + ALTERNATE_EXCHANGE_NAME_POSTFIX;
    }

    public static String getVersionedQueueName(String queueName, String version) {
        return queueName + "-" + version;
    }

    public static String getMasterExchangeName(String appName) {
        return appName  + "-" + MASTER_EXCHANGE_NAME_POSTFIX;
    }

    public static String getVersionedExchangeName(String exchangeName, String version) {
        return exchangeName + "-" + version;
    }
}
