package org.danielli.xultimate.browse.service;

import org.danielli.xultimate.context.dfs.DistributedFileSystemException;

public interface ResourceService {
	
	byte[] getResourceByPath(String resourcePath) throws DistributedFileSystemException;
}
