package org.danielli.xultimate.browse.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;

import org.danielli.xultimate.browse.service.ImageService;
import org.danielli.xultimate.context.image.ImageInfoException;
import org.danielli.xultimate.context.image.ImageResizeTemplate;
import org.danielli.xultimate.context.image.awt.ImageUtils;
import org.danielli.xultimate.context.image.model.ImageFormat;
import org.danielli.xultimate.context.image.model.ImageGeometry;
import org.danielli.xultimate.util.io.FileUtils;
import org.danielli.xultimate.util.io.ResourceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("imageServiceImpl")
public class ImageServiceImpl implements ImageService {

	@Resource(name = "imageResizeTemplate")
	private ImageResizeTemplate imageResizeTemplate;
	
	@Value("${resource.temp.directory}")
	private String resourceTempDirectory;
	
	private String getRandomFilePath(ImageFormat imageFormat) {
		return resourceTempDirectory + File.separator + UUID.randomUUID().toString() + "." + imageFormat.name();
	}
	
	@Override
	public byte[] resizeImage(String imageFilename, byte[] data, ImageGeometry imageGeometry) throws ImageInfoException {
		ImageFormat imageFormat = ImageUtils.getExtension(imageFilename);
		File sourceImageFile = ResourceUtils.getFileSystemResource(getRandomFilePath(imageFormat)).getFile();
		File destImageFile = ResourceUtils.getFileSystemResource(getRandomFilePath(imageFormat)).getFile();
		try {
			FileUtils.writeByteArrayToFile(sourceImageFile, data);
			imageResizeTemplate.resizeImage(sourceImageFile, destImageFile, imageGeometry);
			return FileUtils.readFileToByteArray(destImageFile);
		} catch (IOException e) {
			throw new ImageInfoException(e.getMessage(), e);
		} finally {
			// TODO: 可异步
			FileUtils.deleteQuietly(sourceImageFile);
			FileUtils.deleteQuietly(destImageFile);
		} 
	}
	
}
