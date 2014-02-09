package org.danielli.xultimate.browser.service.impl;

import java.io.File;
import java.util.UUID;

import javax.annotation.Resource;

import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.danielli.xultimate.browser.service.ResourceBrowserService;
import org.danielli.xultimate.context.dfs.DistributedFileSystemException;
import org.danielli.xultimate.context.dfs.fastdfs.StorageClientReturnedCallback;
import org.danielli.xultimate.context.dfs.fastdfs.support.StorageClientTemplate;
import org.danielli.xultimate.context.dfs.fastdfs.util.FastDFSUtils;
import org.danielli.xultimate.context.image.ImageInfoException;
import org.danielli.xultimate.context.image.ImageResizeTemplate;
import org.danielli.xultimate.context.image.awt.ImageUtils;
import org.danielli.xultimate.context.image.model.ImageFormat;
import org.danielli.xultimate.context.image.model.ImageGeometry;
import org.danielli.xultimate.util.io.FileUtils;
import org.danielli.xultimate.util.io.ResourceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("resourceBrowserServiceImpl")
public class ResourceBrowserServiceImpl implements ResourceBrowserService {

	@Resource(name = "resourcePath")
	private StorageClientTemplate storageClientTemplate;
	
	@Resource(name = "imageResizeTemplate")
	private ImageResizeTemplate imageResizeTemplate;
	
	@Value("${resource.temp.directory}")
	private String resourceTempDirectory;
	
	@Override
	public byte[] getResourceByPath(final String resourcePath) throws DistributedFileSystemException {
		return storageClientTemplate.execute(new StorageClientReturnedCallback<byte[]>() {
			
			@Override
			public byte[] doInStorageClient(TrackerServer trackerServer, StorageServer storageServer) throws Exception {
				StorageClient1 storageClient = FastDFSUtils.newStorageClient1(trackerServer, storageServer);
				return storageClient.download_file1(resourcePath);
			}
			
			@Override
			public StorageServer getStorageServer(TrackerClient trackerClient, TrackerServer trackerServer) throws Exception {
				return null;
			}
			
			@Override
			public TrackerServer getTrackerServer(TrackerClient trackerClient) throws Exception {
				return trackerClient.getConnection();
			}
			
		});
	}
	
	private String getRandomFilePath(ImageFormat imageFormat) {
		return resourceTempDirectory + File.separator + UUID.randomUUID().toString() + "." + imageFormat.name();
	}
	
	@Override
	public byte[] getImageByPath(String resourcePath, ImageGeometry imageGeometry) throws DistributedFileSystemException, ImageInfoException {
		ImageFormat imageFormat = ImageUtils.getExtension(resourcePath);
		File sourceImageFile = ResourceUtils.getFileSystemResource(getRandomFilePath(imageFormat)).getFile();
		File destImageFile = ResourceUtils.getFileSystemResource(getRandomFilePath(imageFormat)).getFile();
		byte[] imageData = getResourceByPath(resourcePath);
		try {
			FileUtils.writeByteArrayToFile(sourceImageFile, imageData);
			imageResizeTemplate.resizeImage(sourceImageFile, destImageFile, imageGeometry);
			return FileUtils.readFileToByteArray(destImageFile);
		} catch (Exception e) {
			throw new ImageInfoException(e.getMessage(), e);
		} finally {
			// TODO: 可异步
			FileUtils.deleteQuietly(sourceImageFile);
			FileUtils.deleteQuietly(destImageFile);
		}
	}

}
