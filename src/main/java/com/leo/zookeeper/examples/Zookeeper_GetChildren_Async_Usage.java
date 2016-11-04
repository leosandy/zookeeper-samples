package com.leo.zookeeper.examples;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Zookeeper_GetChildren_Async_Usage implements Watcher {

	private static CountDownLatch connectionedSamephore = new CountDownLatch(1);
	
	private static ZooKeeper zookeeper = null;
	
	public static void main(String[] args) {
		try {
			String path = "/zk-async-book-1";
			zookeeper = new ZooKeeper("localhost:2181,localhost:2182,localhost:2183", 5000, new Zookeeper_GetChildren_Async_Usage());
			zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			zookeeper.create(path+"/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			zookeeper.getChildren(path, true, new Children2CallBack(), "children async call back !");
			zookeeper.create(path+"/c2", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			zookeeper.create(path+"/c3", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			zookeeper.getChildren(path, true, new Children2CallBack(), "children async call back two!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void process(WatchedEvent event) {
		if(KeeperState.SyncConnected == event.getState()){
			try {
				if(EventType.NodeChildrenChanged == event.getType()){
					System.out.println("Node changed :" + zookeeper.getChildren(event.getPath(), false));
				}else if(EventType.NodeCreated == event.getType()){
					System.out.println("node created " + zookeeper.getChildren(event.getPath(), true));
				}else if(EventType.NodeDataChanged == event.getType()){
					System.out.println("node data changed : " + zookeeper.getChildren(event.getPath(), true));
				}else if(EventType.NodeDeleted == event.getType()){
					System.out.println("node deleted :" + event.getType());
				}else if(Event.EventType.None == event.getType() && null == event.getPath()){
					connectionedSamephore.countDown();
				}
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static class Children2CallBack implements AsyncCallback.Children2Callback{

		@Override
		public void processResult(int rc, String path, Object ctx,List<String> children, Stat stat) {
			System.out.printf("rc:%d,path:%s,context:%s,children:%s,stat:%s\n",rc,path,ctx,children,stat);
		}
		
	}

}
