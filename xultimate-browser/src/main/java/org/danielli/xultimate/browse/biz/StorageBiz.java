package org.danielli.xultimate.browse.biz;

import org.danielli.xultimate.context.dfs.DistributedFileSystemException;

public interface StorageBiz {
	
	byte[] getResourceByPath(String resourcePath) throws DistributedFileSystemException;
	
}
