package org.danielli.xultimate.upload.biz;

import java.util.Map;

import org.danielli.xultimate.context.dfs.DistributedFileSystemException;

public interface StorageBiz {

	String uploadResource(String extension, byte[] resourceData, Map<String, String> metadata) throws DistributedFileSystemException;
	
	Map<String, String> getMetadataByPath(String resourcePath) throws DistributedFileSystemException;
	
	void deleteResourceByPath(String resourcePath) throws DistributedFileSystemException;
}
