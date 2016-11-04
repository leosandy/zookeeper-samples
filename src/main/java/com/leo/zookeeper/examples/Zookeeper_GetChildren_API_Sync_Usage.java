package com.leo.zookeeper.examples;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class Zookeeper_GetChildren_API_Sync_Usage implements Watcher {

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	private static ZooKeeper zookeeper = null;
	
	public static void main(String[] args) {
		try {
			String path = "/zk-book-temp-1";
			zookeeper = new ZooKeeper("localhost:2181,localhost:2182,localhost:2183", 50000,//
					 new Zookeeper_GetChildren_API_Sync_Usage());
			connectedSemaphore.await();
//			String result = zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//			System.out.println(result);
//			result = zookeeper.create(path+"/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//			System.out.println(result);
//			System.out.println(zookeeper.getChildren(path, true));
//			zookeeper.create(path + "/c3", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//			zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//			zookeeper.create(path + "/B4", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			System.out.println(zookeeper.getChildren(path, true));
//			zookeeper.create(path + "/B5", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			zookeeper.create(path + "/B6", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			zookeeper.create(path + "/B7", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()){
			if(EventType.None == event.getType() && null == event.getPath()){
				connectedSemaphore.countDown();
			}else if(EventType.NodeChildrenChanged == event.getType()){
				try {
					List<String> list = zookeeper.getChildren(event.getPath(), true);
					System.out.println("Reget child:"+list.toString());
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
