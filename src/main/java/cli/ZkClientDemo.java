//package cli;
//
//import org.I0Itec.zkclient.IZkChildListener;
//import org.I0Itec.zkclient.IZkDataListener;
//import org.I0Itec.zkclient.ZkClient;
//import org.apache.zookeeper.CreateMode;
//
//import java.util.List;
//
//public class ZkClientDemo {
//    public static void main(String[] args) throws InterruptedException {
//        String connStr = "127.0.0.1:2181";
//        ZkClient zkClient = new ZkClient(connStr);
//
//        // 注册【数据】事件
//        zkClient.subscribeDataChanges("/gai", new IZkDataListener() {
//            public void handleDataChange(String s, Object o) throws Exception {
//                System.out.println("数据修改:" + s + "-------" + o);
//            }
//
//            public void handleDataDeleted(String s) throws Exception {
//                System.out.println("数据删除:" + s);
//            }
//        });
//
//        zkClient.subscribeChildChanges("/", new IZkChildListener() {
//            public void handleChildChange(String s, List<String> list) throws Exception {
//                System.out.println("子节点发生变化：" + s);
//                list.forEach(f -> {
//                    System.out.println("content：" + f);
//                });
//            }
//        });
//
//        //遍历根节点
//        List<String> list = zkClient.getChildren("/");
//        list.forEach(e -> {
//            System.out.println(e);
//        });
//
//        String res = zkClient.create("/gai", "I love you", CreateMode.PERSISTENT);
//        System.out.println("创建节点/gai 成功:" + res);
//
//        zkClient.writeData("/gai", "我也爱你");
//        System.out.println("修改节点/gai 数据成功");
//
//        res = zkClient.readData("/gai");
//        System.out.println("节点数据:" + res);
//
//        Thread.sleep(1000);
//
//        zkClient.delete("/gai");
//        System.out.println("删除节点/gai 成功");
//
//    }
//}
