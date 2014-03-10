package org.danielli.xultimate.browse.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.danielli.xultimate.browse.service.ImageService;
import org.danielli.xultimate.browse.service.ResourceService;
import org.danielli.xultimate.context.image.model.GeometryOperator;
import org.danielli.xultimate.context.image.model.ImageGeometry;
import org.danielli.xultimate.context.image.model.ImageSize;
import org.danielli.xultimate.util.ArrayUtils;
import org.danielli.xultimate.web.util.BrowserCacheGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@Controller
@RequestMapping("/resources")
public class ResourceBrowseController {

	@Resource(name = "resourceServiceImpl")
	private ResourceService resourceService;
	
	@Resource(name = "imageServiceImpl")
	private ImageService imageService;
	
	@Resource(name = "browserCacheGenerator")
	private BrowserCacheGenerator browserCacheGenerator;
	
	@Value("${browser.cache.seconds}")
	private Integer expireSeconds;
	
	@RequestMapping(method = { RequestMethod.GET }, value = { "/generic/{groupId:g\\d+}/{f1:M\\d+}/{f2:\\w{2}}/{f3:\\w{2}}/{f4:.*}" })
	@ResponseBody
	public byte[] showGenericResource(@PathVariable("groupId") String groupId, @PathVariable("f1") String f1, @PathVariable("f2") String f2, @PathVariable("f3") String f3, @PathVariable("f4") String f4, HttpServletRequest request, HttpServletResponse response) throws NoSuchRequestHandlingMethodException {
		try {
			String resourcePath = new StringBuilder().append(groupId).append("/").append(f1).append("/").append(f2).append("/").append(f3).append("/").append(f4).toString();
			byte[] result = resourceService.getResourceByPath(resourcePath);
			if (ArrayUtils.isEmpty(result)) {
				throw new NoSuchRequestHandlingMethodException(request);
			}
			browserCacheGenerator.checkAndPrepare(request, response, expireSeconds, true);
			return result;
		} catch (Exception e) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
	}
	
	private ImageGeometry imageGeometry350x350 = new ImageGeometry(new ImageSize(350, 350), GeometryOperator.Emphasize);
	
	@RequestMapping(method = { RequestMethod.GET }, value = { "/350x350/{groupId:g\\d+}/{f1:M\\d+}/{f2:\\w{2}}/{f3:\\w{2}}/{f4:.*}" } )
	@ResponseBody
	public byte[] show350x350Image(@PathVariable("groupId") String groupId, @PathVariable("f1") String f1, @PathVariable("f2") String f2, @PathVariable("f3") String f3, @PathVariable("f4") String f4, HttpServletRequest request, HttpServletResponse response) throws NoSuchRequestHandlingMethodException {
		try {
			String resourcePath = new StringBuilder().append(groupId).append("/").append(f1).append("/").append(f2).append("/").append(f3).append("/").append(f4).toString();
			byte[] result = resourceService.getResourceByPath(resourcePath);
			if (ArrayUtils.isEmpty(result)) {
				throw new NoSuchRequestHandlingMethodException(request);
			}
			result = imageService.resizeImage(resourcePath, result, imageGeometry350x350);
			browserCacheGenerator.checkAndPrepare(request, response, expireSeconds, true);
			return result;
		} catch (Exception e) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
	}
	
	private ImageGeometry imageGeometry160x160 = new ImageGeometry(new ImageSize(160, 160), GeometryOperator.Emphasize);
	
	@RequestMapping(method = { RequestMethod.GET }, value = { "/160x160/{groupId:g\\d+}/{f1:M\\d+}/{f2:\\w{2}}/{f3:\\w{2}}/{f4:.*}" } )
	@ResponseBody
	public byte[] show160x160Image(@PathVariable("groupId") String groupId, @PathVariable("f1") String f1, @PathVariable("f2") String f2, @PathVariable("f3") String f3, @PathVariable("f4") String f4, HttpServletRequest request, HttpServletResponse response) throws NoSuchRequestHandlingMethodException {
		try {
			String resourcePath = new StringBuilder().append(groupId).append("/").append(f1).append("/").append(f2).append("/").append(f3).append("/").append(f4).toString();
			byte[] result = resourceService.getResourceByPath(resourcePath);
			if (ArrayUtils.isEmpty(result)) {
				throw new NoSuchRequestHandlingMethodException(request);
			}
			result = imageService.resizeImage(resourcePath, result, imageGeometry160x160);
			browserCacheGenerator.checkAndPrepare(request, response, expireSeconds, true);
			return result;
		} catch (Exception e) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
	}
}
