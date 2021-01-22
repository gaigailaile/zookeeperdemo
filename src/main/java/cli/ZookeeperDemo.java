package cli;

import javafx.event.EventType;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZookeeperDemo {
    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181",2000,new Watcher(){
            public void process(WatchedEvent event) {
//                // 获取时间的状态
//                KeeperState keeperState = event.getState();
//                EventType tventType = event.getType();
//                // 如果是建立连接
//                if (KeeperState.SyncConnected == keeperState) {
//                    if (EventType.None == tventType) {
//                        // 如果建立连接成功,则发送信号量,让后阻塞程序向下执行
//                        countDownLatch.countDown();
//                        System.out.println("zk 建立连接");
//                    }
//                }
            }
        });
    }
}
