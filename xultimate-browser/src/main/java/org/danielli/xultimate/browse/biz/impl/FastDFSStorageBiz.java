package org.danielli.xultimate.browse.biz.impl;

import javax.annotation.Resource;

import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.danielli.xultimate.browse.biz.StorageBiz;
import org.danielli.xultimate.context.dfs.DistributedFileSystemException;
import org.danielli.xultimate.context.dfs.fastdfs.AbstractStorageClientReturnedCallback;
import org.danielli.xultimate.context.dfs.fastdfs.support.StorageClientTemplate;
import org.danielli.xultimate.context.dfs.fastdfs.util.FastDFSUtils;
import org.springframework.stereotype.Service;

@Service("fastDFSStorageBiz")
public class FastDFSStorageBiz implements StorageBiz {

	@Resource(name = "storageClientTemplate")
	private StorageClientTemplate storageClientTemplate;
	
	@Override
	public byte[] getResourceByPath(final String resourcePath) throws DistributedFileSystemException {
		return storageClientTemplate.execute(new AbstractStorageClientReturnedCallback<byte[]>() {
			
			@Override
			public byte[] doInStorageClient(TrackerClient trackerClient, TrackerServer trackerServer, StorageServer storageServer)
					throws Exception {
				StorageClient1 storageClient = FastDFSUtils.newStorageClient1(trackerServer, storageServer);
				return storageClient.download_file1(resourcePath);
			}
			
			@Override
			public StorageServer getStorageServer(TrackerClient trackerClient, TrackerServer trackerServer) throws Exception {
				return trackerClient.getFetchStorage1(trackerServer, resourcePath);
			}
		});
	}
}
