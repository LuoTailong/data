package com.itheima;

import org.apache.zookeeper.*;

public class ZKClient {
    static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception {
        // 初始化 ZooKeeper 实例(zk 地址、会话超时时间，与系统默认一致、watcher)
        zk = new ZooKeeper("node1:2181,node2:2181", 30000, new Watcher() {
            //该方法就是监听出发客户端的回调方法
            public void process(WatchedEvent event) {
                System.out.println("事件类型为：" + event.getType());
                System.out.println("事件发生的路径：" + event.getPath());
                System.out.println("通知状态为：" + event.getState());

                //如果要实现永久监听 可以在上次监听结束前再次设置同类型监听
                try {
                    zk.getData("/myGirls", true, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 构造节点
        zk.create("/myGirls", "性感的".getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        //1 设置监听
        zk.getData("/myGirls", true, null);

        //2 修改数据 出发监听 注意数据版本号 -1表示用户不维护
        zk.setData("/myGirls", "美丽的".getBytes(), -1);
        zk.setData("/myGirls", "善良的".getBytes(), -1);

        zk.delete("/myGirls",-1);
        zk.close();
    }
}
