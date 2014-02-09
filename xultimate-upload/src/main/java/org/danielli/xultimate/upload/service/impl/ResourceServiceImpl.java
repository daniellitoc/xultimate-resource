package org.danielli.xultimate.upload.service.impl;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.danielli.xultimate.context.dfs.fastdfs.StorageClientReturnedCallback;
import org.danielli.xultimate.context.dfs.fastdfs.support.StorageClientTemplate;
import org.danielli.xultimate.context.dfs.fastdfs.util.FastDFSUtils;
import org.danielli.xultimate.context.image.ImageInfoException;
import org.danielli.xultimate.context.image.ImageResizeTemplate;
import org.danielli.xultimate.context.image.awt.ImageUtils;
import org.danielli.xultimate.context.image.model.Gravity;
import org.danielli.xultimate.context.image.model.ImageFormat;
import org.danielli.xultimate.context.image.model.ImageGeometry;
import org.danielli.xultimate.upload.service.ResourceService;
import org.danielli.xultimate.util.io.FileUtils;
import org.danielli.xultimate.util.io.ResourceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("resourceServiceImpl")
public class ResourceServiceImpl implements ResourceService {

	@Resource(name = "resourcePath")
	private StorageClientTemplate storageClientTemplate;
	
	@Resource(name = "imageResizeTemplate")
	private ImageResizeTemplate imageResizeTemplate;
	
	@Value("${resource.temp.directory}")
	private String resourceTempDirectory;
	
	private String getRandomFilePath(ImageFormat imageFormat) {
		return resourceTempDirectory + File.separator + UUID.randomUUID().toString() + "." + imageFormat.name();
	}
	
	@Override
	public byte[] resizeImage(byte[] data, String imageFilename, ImageGeometry imageGeometry, Gravity gravity) {
		ImageFormat imageFormat = ImageUtils.getExtension(imageFilename);
		File sourceImageFile = ResourceUtils.getFileSystemResource(getRandomFilePath(imageFormat)).getFile();
		File destImageFile = ResourceUtils.getFileSystemResource(getRandomFilePath(imageFormat)).getFile();
		try {
			FileUtils.writeByteArrayToFile(sourceImageFile, data);
			imageResizeTemplate.resizeImageAsFixed(sourceImageFile, destImageFile, imageGeometry, gravity);
			return FileUtils.readFileToByteArray(destImageFile);
		} catch (Exception e) {
			throw new ImageInfoException(e.getMessage(), e);
		} finally {
			// TODO: 可异步
			FileUtils.deleteQuietly(sourceImageFile);
			FileUtils.deleteQuietly(destImageFile);
		} 

	}
	
	@Override
	public String uploadResource(final String extension, final byte[] resourceData, final Map<String, String> metadata) {
		return storageClientTemplate.execute(new StorageClientReturnedCallback<String>() {
			
			@Override
			public String doInStorageClient(TrackerServer trackerServer, StorageServer storageServer) throws Exception {
				StorageClient1 storageClient = FastDFSUtils.newStorageClient1(trackerServer, storageServer);
				return storageClient.upload_file1(resourceData, extension, FastDFSUtils.newNameValuePairs(metadata));
			}
			
			@Override
			public StorageServer getStorageServer(TrackerClient trackerClient, TrackerServer trackerServer) throws Exception {
				return trackerClient.getStoreStorage(trackerServer);
			}
			
			@Override
			public TrackerServer getTrackerServer(TrackerClient trackerClient) throws Exception {
				return trackerClient.getConnection();
			}
		});
	}
	
	@Override
	public Map<String, String> getMetadataByPath(final String resourcePath) {
		return storageClientTemplate.execute(new StorageClientReturnedCallback<Map<String, String>>() {
			
			@Override
			public Map<String, String> doInStorageClient(TrackerServer trackerServer, StorageServer storageServer) throws Exception {
				StorageClient1 storageClient = FastDFSUtils.newStorageClient1(trackerServer, storageServer);
				NameValuePair[] nameValuePairs = storageClient.get_metadata1(resourcePath);
				return FastDFSUtils.newMapFromNameValuePairs(nameValuePairs);
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
	
	@Override
	public Integer deleteFileByPath(final String resourcePath) {
		return storageClientTemplate.execute(new StorageClientReturnedCallback<Integer>() {
			
			@Override
			public Integer doInStorageClient(TrackerServer trackerServer, StorageServer storageServer) throws Exception {
				StorageClient1 storageClient = FastDFSUtils.newStorageClient1(trackerServer, storageServer);
				return storageClient.delete_file1(resourcePath);
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

}
