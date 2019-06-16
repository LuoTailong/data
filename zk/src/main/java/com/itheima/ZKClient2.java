package com.itheima;

import org.apache.zookeeper.*;

public class ZKClient2 {
    public static void main(String[] args) throws Exception {
        // 初始化 ZooKeeper 实例(zk 地址、会话超时时间，与系统默认一致、watcher)
        ZooKeeper zk = new ZooKeeper("node1:2181,node2:2181", 30000, new Watcher() {
            //该方法就是监听出发客户端的回调方法
            public void process(WatchedEvent event) {
                System.out.println("事件类型为：" + event.getType());
                System.out.println("事件发生的路径：" + event.getPath());
                System.out.println("通知状态为：" + event.getState());
            }
        });

        //创建一个目录节点
        zk.create("/a", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        //创建一个子目录节点
        zk.create("/a/b", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/a", false, null)));

        //取出子目录节点列表
        System.out.println(zk.getChildren("/a/b", true));

        //修改子目录节点数据 -1表示用户不维护
        zk.setData("/a/b", "11111".getBytes(), -1);
        System.out.println("目录节点状态：[" + zk.exists("/a", true) + "]");

        //创建另外一个子目录节点
        zk.create("/a/c", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData("/a/c", true, null)));

        //删除子目录节点
        zk.delete("/a/b", -1);
        zk.delete("/a/c", -1);

        //删除父目录节点
        zk.delete("/a",-1);
        zk.close();
    }
}
