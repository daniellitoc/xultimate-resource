package org.danielli.xultimate.browse.service;

import org.danielli.xultimate.context.image.ImageInfoException;
import org.danielli.xultimate.context.image.model.ImageGeometry;

public interface ImageService {
	
	byte[] resizeImage(String imageFilename, byte[] data, ImageGeometry imageGeometry) throws ImageInfoException;
}
