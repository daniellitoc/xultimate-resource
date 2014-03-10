package org.danielli.xultimate.upload.service;

import org.danielli.xultimate.context.image.model.Gravity;
import org.danielli.xultimate.context.image.model.ImageGeometry;

public interface ImageService {

	byte[] resizeImage(String imageFilename, byte[] data, ImageGeometry imageGeometry, Gravity gravity);
}
