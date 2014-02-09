package org.danielli.xultimate.browser.service;

import org.danielli.xultimate.context.dfs.DistributedFileSystemException;
import org.danielli.xultimate.context.image.ImageInfoException;
import org.danielli.xultimate.context.image.model.ImageGeometry;

public interface ResourceBrowserService {
	
	byte[] getResourceByPath(String resourcePath) throws DistributedFileSystemException;
	
	byte[] getImageByPath(String resourcePath, ImageGeometry imageGeometry) throws DistributedFileSystemException, ImageInfoException;
}	
