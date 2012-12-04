package edu.ibs.webui.client;

/**
 * Created by IntelliJ IDEA.
 * User: Максим
 * Date: 29.10.12
 * Time: 23:04
 * To change this template use File | Settings | File Templates.
 */
public class AccountData {
    private static AccountRecord[] records;

    public static AccountRecord[] getRecords() {
        if (records == null) {
            records = getNewRecords();
        }
        return records;
    }


    public static AccountRecord[] getNewRecords() {

        return new AccountRecord[]{

                new AccountRecord("sales", "VISA", "**** **** **** 1231", "USD",
                        "-300", "Операции за рубежом: <b>вкл.</b><br/>Операции в интернете: <b>вкл.</b>"),

                new AccountRecord("sales", "VISA", "**** **** **** 4545", "EUR",
                        "2000", "Операции в интернете: <b>вкл.</b><br/>Операции в интернете: <b>вкл.</b>"),

                new AccountRecord("sales", "Cirrus/Maestro", "**** **** **** 9713", "BYR",
                        "6000000", "Операции в интернете: <b>откл.</b><br/>Операции в интернете: <b>откл.</b>"),
        };

    }

}
