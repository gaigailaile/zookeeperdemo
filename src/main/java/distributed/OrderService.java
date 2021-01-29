package distributed;

public class OrderService implements Runnable {

    private OrderNumGenerator orderNumGenerator = new OrderNumGenerator();

    private static Object obj = new Object();

    private Lock lock = new ZookeeperDistrbuteLock();

    @Override
    public void run() {
        lock.getLock();
        String orderNumber = orderNumGenerator.getOrderNumber();
        System.out.println("获取订单号:" + orderNumber);
        lock.unLock();
    }
}
