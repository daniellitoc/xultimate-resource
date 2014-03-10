package org.danielli.xultimate.browse.service.impl;

import javax.annotation.Resource;

import org.danielli.xultimate.browse.biz.StorageBiz;
import org.danielli.xultimate.browse.service.ResourceService;
import org.danielli.xultimate.context.dfs.DistributedFileSystemException;
import org.springframework.stereotype.Service;

@Service("resourceServiceImpl")
public class ResourceServiceImpl implements ResourceService {

	@Resource(name = "fastDFSStorageBiz")
	private StorageBiz storageBiz;
	
	@Override
	public byte[] getResourceByPath(String resourcePath) throws DistributedFileSystemException {
		return storageBiz.getResourceByPath(resourcePath);
	}
}
