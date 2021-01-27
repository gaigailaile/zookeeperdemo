package cli;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperDemo {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
//                // 获取时间的状态
                Watcher.Event.KeeperState keeperState = event.getState();
                Event.EventType tventType = event.getType();
                // 如果是建立连接
                if (Event.KeeperState.SyncConnected == keeperState) {
                    if (Event.EventType.None == tventType) {
                        // 如果建立连接成功,则发送信号量,让后阻塞程序向下执行
                        System.out.println("zk 建立连接");
                        countDownLatch.countDown();
                    }else if(event.getType() == Event.EventType.NodeCreated){
                        System.out.println("listen:节点创建");
                    }else if(event.getType() == Event.EventType.NodeChildrenChanged){
                        System.out.println("listen:子节点修改");
                    }
                }
            }
        };

        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",2000,watcher);
        countDownLatch.await();

        zooKeeper.exists("/gai",watcher);

        //创建节点
        String result = zooKeeper.create("/gai", "i love u".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 获取节点
        byte[] bs = zooKeeper.getData("/gai", true, null);
        result = new String(bs);
        System.out.println("创建节点后的数据是:" + result);

        // 修改节点
        zooKeeper.setData("/gai", "我爱你".getBytes(), -1);

        Thread.sleep(10);

        bs = zooKeeper.getData("/gai", true, null);
        result = new String(bs);
        System.out.println("修改节点后的数据是:" + result);

        // 删除节点
        zooKeeper.delete("/gai", -1);

        zooKeeper.close();
    }
}
