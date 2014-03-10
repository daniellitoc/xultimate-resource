package org.danielli.xultimate.upload.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.danielli.xultimate.context.i18n.Message;
import org.danielli.xultimate.context.i18n.MessageUtils;
import org.danielli.xultimate.context.image.model.GeometryOperator;
import org.danielli.xultimate.context.image.model.Gravity;
import org.danielli.xultimate.context.image.model.ImageGeometry;
import org.danielli.xultimate.context.image.model.ImageSize;
import org.danielli.xultimate.upload.service.ImageService;
import org.danielli.xultimate.upload.service.ResourceService;
import org.danielli.xultimate.util.StringUtils;
import org.danielli.xultimate.util.collections.MapUtils;
import org.danielli.xultimate.util.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@Controller
@RequestMapping("/resources")
public class ResourceUploadController {

	@Resource(name = "resourceServiceImpl")
	private ResourceService resourceService;
	
	@Resource(name = "imageServiceImpl")
	private ImageService imageService;
	
	private static String AUTHOR = "author";
	
	private static String FILE_NAME = "fileName";
	
	private final ImageGeometry imageGeometry = new ImageGeometry(new ImageSize(800, 800), GeometryOperator.Maximum);
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUploadController.class);

	@RequestMapping(method = { RequestMethod.POST }, value = { "/image" })
	@ResponseBody
	public Message<String> uploadImage(@RequestParam(required = false) MultipartFile multipartFile, HttpServletRequest request) {
		try {
			if (multipartFile == null) {
				return MessageUtils.warn("upload.resources.empty");
			}
			String username = getLoginUserName(request);
			if (StringUtils.isEmpty(username)) {
				return MessageUtils.warn("upload.resources.post.noSession");
			}
			String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			if (StringUtils.isEmpty(extension)) {
				return MessageUtils.warn("upload.resources.delete.noExtention");
			} 
			Map<String, String> metadata = new HashMap<String, String>();
			metadata.put(AUTHOR, username);
			metadata.put(FILE_NAME, multipartFile.getOriginalFilename());
			byte[] imageData = imageService.resizeImage(multipartFile.getOriginalFilename(), multipartFile.getBytes(), imageGeometry, Gravity.Center);
			String resourcePath = resourceService.uploadResource(extension, imageData, metadata);
			return MessageUtils.success("upload.resources.post.success", resourcePath);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return MessageUtils.error("upload.resources.post.error");
		}
	}
	
	@RequestMapping(method = { RequestMethod.POST } )
	@ResponseBody
	public Message<String> uploadResource(@RequestParam(required = false) MultipartFile multipartFile, HttpServletRequest request) {
		try {
			if (multipartFile == null) {
				return MessageUtils.warn("upload.resources.empty");
			}
			String username = getLoginUserName(request);
			if (StringUtils.isEmpty(username)) {
				return MessageUtils.warn("upload.resources.post.noSession");
			}
			String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			if (StringUtils.isEmpty(extension)) {
				return MessageUtils.warn("upload.resources.delete.noExtention");
			} 
			Map<String, String> metadata = new HashMap<String, String>();
			metadata.put(AUTHOR, username);
			metadata.put(FILE_NAME, multipartFile.getOriginalFilename());
			String resourcePath = resourceService.uploadResource(extension, multipartFile.getBytes(), metadata);
			return MessageUtils.success("upload.resources.post.success", resourcePath);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return MessageUtils.error("upload.resources.post.error");
		}
	}
	
	@RequestMapping(method = { RequestMethod.DELETE }, value = { "/{groupId:g\\d+}/{f1:M\\d+}/{f2:\\w{2}}/{f3:\\w{2}}/{f4:.*}" })
	@ResponseBody
	public Message<String> deleteResource(@PathVariable("groupId") String groupId, @PathVariable("f1") String f1, @PathVariable("f2") String f2, @PathVariable("f3") String f3, @PathVariable("f4") String f4,  HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		try {
			String resourcePath = new StringBuilder().append(groupId).append("/").append(f1).append("/").append(f2).append("/").append(f3).append("/").append(f4).toString();
			Map<String, String> metadata = resourceService.getMetadataByPath(resourcePath);
			if (MapUtils.isEmpty(metadata)) {
				throw new NoSuchRequestHandlingMethodException(request);
			}
			String author = metadata.get(AUTHOR);
			String username = getLoginUserName(request);
			if (StringUtils.isEmpty(username)) {
				return MessageUtils.warn("upload.resources.delete.noSession");
			}
			if (!StringUtils.equals(username, author)) {
				return MessageUtils.warn("upload.resources.delete.noPermission");
			}
			resourceService.deleteResourceByPath(resourcePath);
			return MessageUtils.success("upload.resources.delete.success");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return MessageUtils.error("upload.resources.delete.error");
		}
	}
	
	private String getLoginUserName(HttpServletRequest request) {
		return "daniellitoc";
	}
}
