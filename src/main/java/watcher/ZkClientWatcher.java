package watcher;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkClientWatcher implements Watcher {

    private static final String CONNECT_ADDRESS = "127.0.0.1:2181";

    private static final int SESSION_TIME = 3000;

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZooKeeper zk;

    @Override
    public void process(WatchedEvent watchedEvent) {
        // 获取事件状态
        Event.KeeperState keeperState = watchedEvent.getState();
        // 获取事件类型
        Event.EventType eventType = watchedEvent.getType();
        // zk 路径
        String path = watchedEvent.getPath();
        System.out.println("进入到 process() keeperState:" + keeperState + ", eventType:" + eventType + ", path:" + path);
        // 判断是否建立连接
        if (Event.KeeperState.SyncConnected == keeperState) {
            if (Event.EventType.None == eventType) {
                // 如果建立建立成功,让后程序往下走
                System.out.println("zk 建立连接成功!");
                countDownLatch.countDown();
            } else if (Event.EventType.NodeCreated == eventType) {
                System.out.println("事件通知,新增node节点" + path);
            } else if (Event.EventType.NodeDataChanged == eventType) {
                System.out.println("事件通知,当前node节点" + path + "被修改....");
            } else if (Event.EventType.NodeDeleted == eventType) {
                System.out.println("事件通知,当前node节点" + path + "被删除....");
            }
        }
        System.out.println("--------------------------------------------------------");
    }

    public void createConnection(String connStr,int sessionTime){
        try {
            zk = new ZooKeeper(connStr,sessionTime,this);
            System.out.println("zk 开始启动连接服务器....");
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean createPath(String path,String data){
        try {
            this.exists(path,true);
            this.zk.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            System.out.println("创建节点成功 path:" + path + ",data:" + data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Stat exists(String path,boolean watch){
        try {
            return this.zk.exists(path,watch);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean editPath(String path,String data){
        try {
            this.exists(path,true);
            this.zk.setData(path,data.getBytes(),-1);
            System.out.println("修改节点成功 path:" + path + ",data:" + data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean deletePath(String path){
        try {
            this.exists(path,true);
            this.zk.delete(path,-1);
            System.out.println("删除节点成功 path:" + path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
