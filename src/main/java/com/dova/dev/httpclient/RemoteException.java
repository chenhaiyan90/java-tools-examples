package com.dova.dev.httpclient;


public class RemoteException extends RuntimeException {
    public final String      sequence;
    public final String      errorMsg;

    public RemoteException(String sequence, String error) {
        super("Received error from remote service, Sequence = " + sequence + ", ErrorMsg:" + error);
        this.sequence = sequence;
        this.errorMsg = error;
    }
}
