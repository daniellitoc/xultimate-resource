package org.danielli.xultimate.upload.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.danielli.xultimate.context.image.model.GeometryOperator;
import org.danielli.xultimate.context.image.model.Gravity;
import org.danielli.xultimate.context.image.model.ImageGeometry;
import org.danielli.xultimate.context.image.model.ImageSize;
import org.danielli.xultimate.upload.service.ResourceService;
import org.danielli.xultimate.util.StringUtils;
import org.danielli.xultimate.util.collections.MapUtils;
import org.danielli.xultimate.util.io.FilenameUtils;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@Controller
public class ResourceUploadController {

	@Resource(name = "resourceServiceImpl")
	private ResourceService resourceService;
	
	private static String AUTHOR = "author";
	
	private static String FILE_NAME = "fileName";

	@RequestMapping(method = { RequestMethod.PUT, RequestMethod.POST }, value = { "/resource" })
	@ResponseBody
	public Map<String, Object> uploadResource(MultipartFile file, HttpServletRequest request) {
		String username = getLoginUserName(request);
		if (StringUtils.isEmpty(username)) {
			throw new HttpMessageNotWritableException(request.getRequestURI());
		}
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isEmpty(extension)) {
			result.put("status", false);
			result.put("errorMessage", file.getOriginalFilename() + " must has extension");
		} else {
			Map<String, String> metadata = new HashMap<String, String>();
			metadata.put(AUTHOR, username);
			metadata.put(FILE_NAME, file.getOriginalFilename());
			try {
				String resourcePath = resourceService.uploadResource(extension, file.getBytes(), metadata);
				result.put("status", true);
				result.put("resourcePath", resourcePath);
			} catch (Exception e) {
				result.put("status", false);
				result.put("errorMessage", e.getMessage());
			}
		}
		return result;
	}
	
	@RequestMapping(method = { RequestMethod.PUT, RequestMethod.POST }, value = { "/image" })
	@ResponseBody
	public Map<String, Object> uploadImage(MultipartFile file, HttpServletRequest request) {
		String username = getLoginUserName(request);
		if (StringUtils.isEmpty(username)) {
			throw new HttpMessageNotWritableException(request.getRequestURI());
		}
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isEmpty(extension)) {
			result.put("status", false);
			result.put("errorMessage", file.getOriginalFilename() + " must has extension");
		} else {
			ImageGeometry imageGeometry = new ImageGeometry(new ImageSize(800, 800), GeometryOperator.Maximum);
			try {
				byte[] imageData = resourceService.resizeImage(file.getBytes(), file.getOriginalFilename(), imageGeometry, Gravity.Center);
				Map<String, String> metadata = new HashMap<String, String>();
				metadata.put(AUTHOR, username);
				metadata.put(FILE_NAME, file.getOriginalFilename());
				String resourcePath = resourceService.uploadResource(extension, imageData, metadata);
				result.put("status", true);
				result.put("resourcePath", resourcePath);
			} catch (Exception e) {
				result.put("status", false);
				result.put("errorMessage", e.getMessage());
			}
		}
		return result;
	}
	
	@RequestMapping(method = { RequestMethod.DELETE }, value = { "/resource/{resourcePath:g\\d+/M\\d+/.*}" })
	@ResponseBody
	public Map<String, Object> deleteResource(@PathVariable("resourcePath") String resourcePath, HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> metadata = resourceService.getMetadataByPath(resourcePath);
		if (MapUtils.isEmpty(metadata)) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
		String author = metadata.get(AUTHOR);
		if (!StringUtils.equals(getLoginUserName(request), author)) {
			throw new HttpMessageNotWritableException(request.getRequestURI());
		}
		Integer deleteStatusCode = resourceService.deleteFileByPath(resourcePath);
		if (deleteStatusCode.equals(0)) {
			result.put("status", true);
		} else {
			result.put("status", false);
			result.put("statusCode", deleteStatusCode);
		}
		return result;
	}
	
	private String getLoginUserName(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) 
			return null;
		String username = (String) session.getAttribute("username");	// TODO: 临时写法
		return username;
	}
}
