package distributed;

import org.I0Itec.zkclient.ZkClient;

public abstract class ZookeeperAbstractLock implements Lock {
    private static final String CONNECT_ADDRES = "127.0.0.1:2181";

    protected ZkClient zkClient = new ZkClient(CONNECT_ADDRES);
    protected String PATH = "/lock";

    public void getLock() {
        // 如果当前节点已经存在,则等待
        if (tryLock()) {
            System.out.println("获取到锁 get");
        } else {
            // 等待
            waitLock();
            // 重新获取锁
            getLock();
        }
    }

    protected abstract void waitLock();

    protected abstract boolean tryLock();

    public void unLock() {
        if (zkClient != null) {
            zkClient.close();
        }
        System.out.println("已经释放锁...");
    }
}
