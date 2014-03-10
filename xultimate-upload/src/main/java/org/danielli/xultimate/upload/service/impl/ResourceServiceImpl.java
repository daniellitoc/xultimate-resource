package org.danielli.xultimate.upload.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.danielli.xultimate.upload.biz.StorageBiz;
import org.danielli.xultimate.upload.service.ResourceService;
import org.springframework.stereotype.Service;

@Service("resourceServiceImpl")
public class ResourceServiceImpl implements ResourceService {

	@Resource(name = "fastDFSStorageBiz")
	private StorageBiz storageBiz;
	
	@Override
	public String uploadResource(String extension, byte[] resourceData, Map<String, String> metadata) {
		return storageBiz.uploadResource(extension, resourceData, metadata);
	}
	
	@Override
	public Map<String, String> getMetadataByPath(String resourcePath) {
		return storageBiz.getMetadataByPath(resourcePath);
	}
	
	@Override
	public void deleteResourceByPath(String resourcePath) {
		storageBiz.deleteResourceByPath(resourcePath);
	}
}
