package com.leo.zookeeper.examples;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class Zookeeper_Create_Api_Sync_Usage implements Watcher {

	private static CountDownLatch connectedSamephore = new CountDownLatch(1);
	
	@Override
	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()){
			connectedSamephore.countDown();
		}
	}
	
	public static void main(String[] args) {
		String connectString = "localhost:2181,localhost:2182,localhost:2183";
		try {
			ZooKeeper zooKeeper = new ZooKeeper(connectString, 5000, new Zookeeper_Create_Api_Sync_Usage());
			connectedSamephore.await();
			String path1 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			System.out.println("Success create node :" + path1);
			String path2 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			System.out.println("Success create node :" + path2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
