package cli;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Executors;

public class CuratorDemo {
    public static void main(String[] args) throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
//                .authorization() //创建客户端的时候授权
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

        /*创建会话*/
        curator.start();

        /**
         * 创建节点
         * 注意:
         * 1 除非指明创建节点的类型,默认是持久节点
         * 2 ZooKeeper规定:所有非叶子节点都是持久节点,所以递归创建出来的节点,只有最后的数据节点才是指定类型的节点,其父节点是持久节点
         */
        curator.create().forPath("/China");//创建一个初始内容为空的节点
        curator.create().forPath("/America", "zhangsan".getBytes());
        curator.create().withMode(CreateMode.EPHEMERAL).forPath("/France");//创建一个初始内容为空的临时节点
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/Russia/car", "haha".getBytes());//递归创建,/Russia是持久节点

        /**
         * 异步创建节点
         * 注意:如果自己指定了线程池,那么相应的操作就会在线程池中执行,如果没有指定,那么就会使用Zookeeper的EventThread线程对事件进行串行处理
         */
        curator.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("当前线程:" + Thread.currentThread().getName() + ",code:" + event.getResultCode()
                        + ",type:" + event.getType());
            }
        }, Executors.newFixedThreadPool(10)).forPath("/async-curator-my");

        curator.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("当前线程:" + Thread.currentThread().getName() + ",code:" + event.getResultCode()
                        + ",type:" + event.getType());
            }
        }).forPath("/async-curator-zookeeper");

        //获取节点
        Stat stat = new Stat();
        byte[] bytes = curator.getData().storingStatIn(stat).forPath("/America");
        System.out.println("路径/America的数据是："+new String(bytes));
        System.out.println("路径/America的数据的版本是:"+stat.getVersion());

        //更新节点
        curator.setData().withVersion(0).forPath("/America", "美国".getBytes());

        Stat stat1 = new Stat();
        byte[] bytes1 = curator.getData().storingStatIn(stat).forPath("/America");
        System.out.println("路径/America的数据是："+new String(bytes1));
        System.out.println("路径/America的数据的版本是:"+stat.getVersion());

        /**
         * 删除节点
         */
        curator.delete().forPath("/China"); //只能删除叶子节点
        curator.delete().deletingChildrenIfNeeded().forPath("/Russia"); //删除一个节点,并递归删除其所有子节点
        curator.delete().withVersion(1).forPath("/America");    //强制指定版本进行删除
//        curator.delete().guaranteed().forPath("/America");  //注意:由于一些网络原因,上述的删除操作有可能失败,使用guaranteed(),如果删除失败,会记录下来,只要会话有效,就会不断的重试,直到删除成功为止

        curator.close();

        NodeCache nodeCache = new NodeCache(curator,"/lzh");
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("新的节点数据:" + new String(nodeCache.getCurrentData().getData()));
            }
        });
        nodeCache.start(true);

        /**
         * 监听子节点变化情况
         * 1 新增子节点
         * 2 删除子节点
         * 3 子节点数据变更
         */
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curator,"/lzh",true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()){
                    case CHILD_ADDED:
                        System.out.println("新增子节点:" + event.getData().getPath());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("子节点数据变化:" + event.getData().getPath());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("删除子节点:" + event.getData().getPath());
                        break;
                    default:
                        break;
                }
            }
        });
        pathChildrenCache.start();

    }
}
