package org.danielli.xultimate.upload.service;

import java.util.Map;

import org.danielli.xultimate.context.image.model.Gravity;
import org.danielli.xultimate.context.image.model.ImageGeometry;

public interface ResourceService {

	Map<String, String> getMetadataByPath(String resourcePath);
	
	Integer deleteFileByPath(String resourcePath);
	
	String uploadResource(String extension, byte[] resourceData, Map<String, String> metadata);
	
	byte[] resizeImage(byte[] data, String imageFilename, ImageGeometry imageGeometry, Gravity gravity);
}
