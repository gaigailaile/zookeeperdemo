package watcher;

public class Demo {
    public static void main(String[] args) {
        ZkClientWatcher zk = new ZkClientWatcher();
        //建立连接
        zk.createConnection("127.0.0.1:2181",3000);
        //创建节点
        zk.createPath("/gai","i love u");
        //查询节点
        String data = zk.getData("/gai");
        System.out.println("数据为:" + data);
        //修改节点
        zk.editPath("/gai","我爱你");
        //查询节点
        String dataEdit = zk.getData("/gai");
        System.out.println("数据为:" + dataEdit);
        //删除节点
        zk.deletePath("/gai");
    }
}
