package com.dova.dev.zkutils;

import org.I0Itec.zkclient.*;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.commons.codec.Charsets;
import org.apache.zookeeper.Watcher;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liuzhendong on 16/10/30.
 */
public class SubcribeDataChange {
    public static String zkConnect = "182.92.183.219:2181";
    public static void main(String[] args) throws Exception{
        ZkConnection zkConnection = new ZkConnection(zkConnect,3000);
        ZkClient zkClient = new ZkClient(zkConnection);
        zkClient.setZkSerializer(new ZkStringSerializer());
        zkClient.createEphemeral("/tmp");
        zkClient.subscribeDataChanges("/tmp",new LeaderChangeListener());
        zkClient.subscribeChildChanges("/childtest",new ChildListener());
        zkClient.subscribeStateChanges(new SessionExpiredListener());
        System.out.println("SessionId:" + zkConnection.getZookeeper().getSessionId());
        new CountDownLatch(1).await();
    }
}

class LeaderChangeListener implements IZkDataListener{

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception{
        System.out.println(String.format("DATA_CHANGE path:%s newData:%s", dataPath, data.toString()));
    }

    @Override
    public void handleDataDeleted(String dataPath) {
        System.out.println(String.format("DATA_DELETE path:%s", dataPath));
    }
}
class ZkStringSerializer implements ZkSerializer{
    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError
    {
        return new String(bytes, Charsets.UTF_8);
    }

    @Override
    public byte[] serialize(Object obj) throws ZkMarshallingError
    {
        return String.valueOf(obj).getBytes(Charsets.UTF_8);
    }
}

class SessionExpiredListener implements IZkStateListener{
    public void handleStateChanged(Watcher.Event.KeeperState var1) throws Exception{
        System.out.println("state change:" + var1);
    }

    public void handleNewSession() throws Exception{
        System.out.println("new Session established");
    }

    public void handleSessionEstablishmentError(Throwable var1) throws Exception{
        var1.printStackTrace();
    }
}
class  ChildListener implements IZkChildListener{
    public void handleChildChange(String var1, List<String> var2) throws Exception{
        System.out.println("child change " + var1 + var2);
    }
}