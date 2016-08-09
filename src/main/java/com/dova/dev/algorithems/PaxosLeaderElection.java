package com.dova.dev.algorithems;


/**
 * Created by liuzhendong on 16/4/4.
 * zookeeper中的领导选取算法模拟实现
 * 基于paxos
 */

public class PaxosLeaderElection {

    public static void main(String[] args)throws  Exception{
        int num = 40;
        ServiceNode[] serviceNodes = new ServiceNode[num];
        Accepter[] accepters = new Accepter[num];
        for (int i = 0; i < num; i++) {
            serviceNodes[i] = new ServiceNode(i+1,num);
            serviceNodes[i].proposer.qurum = accepters;
            accepters[i] = serviceNodes[i].accepter;
        }
        for (int i = 0; i < num; i++) {
            serviceNodes[i].start();
        }
        for (int i = 0; i < num; i++) {
            serviceNodes[i].join();
        }
    }
}



class  ServiceNode extends Thread{

    public  final  int ID;
    public  final  int NODE_NUM;
    public  Accepter accepter;
    public  Proposer proposer;
    public  ServiceNode leader;

    public  ServiceNode(int id, int node_num){
        this.ID = id;
        this.NODE_NUM = node_num;
        this.accepter = new Accepter(this);
        this.proposer = new Proposer(NODE_NUM/2 + 1, this);
    }
    public void findLeader(){
        System.out.println(String.format("Service-%d, finding leader",ID));
        leader = proposer.proposeLeader();
        System.out.println(String.format("Service-%d, finds the leader %d",ID,leader.ID));
    }

    @Override
    public void run(){
         while (true){
            if(checkLeader()){
                System.out.println(String.format("Service-%d, the leader is %d",ID, leader.ID));
            }else {
                //没有领导或领导已失效, 重置自己的accepter的领导值并开始寻找领导
                accepter.clear();
                findLeader();
            }
             try {
                 Thread.sleep(1000);
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
    }

    public  boolean checkLeader(){
        if(leader == null){
            //System.out.println(String.format("Service-%d, there is no leader",ID));
            return  false;
        }
        if(leader.getState() == State.TERMINATED){
            //System.out.println(String.format("Servive-%d, the leader is terminated",ID));
            return  false;
        }
        return  true;
    }
}



class  Result{
    ServiceNode lastVoteLeader;
    boolean ok;
    String epoth;
    public Result(boolean ok, ServiceNode lastVoteLeader, String epoth){
        this.lastVoteLeader = lastVoteLeader;
        this.ok = ok;
        this.epoth = epoth;
    }
}

class Accepter{
    public ServiceNode lastLeader; //上一次接收的值
    public String acceptEpoth; //上一次接收值的epoth
    public String epoch; //上一次prepare的epoth
    public  ServiceNode myServiceNode;
    public  Accepter(ServiceNode myServiceNode){
        this.myServiceNode = myServiceNode;
        this.epoch = "";
        this.acceptEpoth = "";
        this.lastLeader = null;
    }
    public  synchronized  void clear(){
        this.lastLeader = null;
        this.acceptEpoth = "";
    }
    public  synchronized  boolean accept(String epoch,ServiceNode leader){
        if(!checkMyServiceNode()){
            //模拟消息延迟或断网
            return  false;
        }
        if(epoch.compareTo(this.epoch) == 0){
            /*
            try{
                Thread.sleep(100 + (int)(100*Math.random()));
            }catch (Exception e){
                e.printStackTrace();
            }
            */
            this.acceptEpoth = epoch;
            this.lastLeader = leader;
            return  true;
        }else {
            return  false;
        }
    }
    public  synchronized  Result prepare(String epoch){
        if(!checkMyServiceNode()){
            //模拟消息延迟或断网
            return  new Result(false, null, null);
        }
        if(epoch.compareTo(this.epoch) > 0){
            /*
            try{
                Thread.sleep(100 + (int)(100*Math.random()));
            }catch (Exception e){
                e.printStackTrace();
            }
            */
            this.epoch = epoch;
            return new Result(true, this.lastLeader,acceptEpoth);
        }else {
            return  new Result(false, this.lastLeader, acceptEpoth);
        }
    }
    public  boolean checkMyServiceNode(){
        if(this.myServiceNode.getState() == Thread.State.TERMINATED){
            return  false;
        }
        return  true;
    }
}



class  Proposer{
    public  final  int QUORUM_NUM;
    public  Accepter[] qurum;
    public  ServiceNode myServiveNode;
    public Proposer(int quorum_num, ServiceNode myServiceNode){
        this.myServiveNode = myServiceNode;
        this.QUORUM_NUM = quorum_num;
    }

    public  ServiceNode proposeLeader(){
        while (true){
            Result result = prepare();
            if(result.ok){
                if(accept(result)){
                    return  result.lastVoteLeader;
                }
            }
            //随机sleep,避免活锁
            try{
                System.out.println(String.format("Proposer-%d sleep %s",myServiveNode.ID,result.epoth));
                Thread.sleep((int)(1000 * Math.random()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public Result prepare(){
        String epoth = String.format("%d-%d", System.currentTimeMillis(), myServiveNode.ID);
        int num = 0;
        String maxEpoch = "";
        ServiceNode maxEpochServiceNode = null;
        for (Accepter accepter: qurum) {
            Result result = accepter.prepare(epoth);
            if(result.ok){
                num++;
            }
            if(result.epoth.compareTo(maxEpoch) > 0){
                maxEpoch = result.epoth;
                maxEpochServiceNode = result.lastVoteLeader;
            }
        }

        if(num >= QUORUM_NUM){

            System.out.println(String.format("Proposer-%d lastvote:%d lastepoth:%s",myServiveNode.ID,
                    maxEpochServiceNode == null ? 0 : maxEpochServiceNode.ID,maxEpoch));
            if(maxEpochServiceNode == null){
                maxEpochServiceNode = myServiveNode;
            }
            return new Result(true, maxEpochServiceNode, epoth);
        }else {
            return  new Result(false, maxEpochServiceNode, epoth);
        }
    }
    public  boolean accept(Result prepare){
        int num = 0;
        for (Accepter accepter: qurum) {
            boolean result = accepter.accept(prepare.epoth, prepare.lastVoteLeader);
            if (result) {
                num++;
            }
        }
        if(num >= QUORUM_NUM){
            return  true;
        }
        return  false;
    }

}
