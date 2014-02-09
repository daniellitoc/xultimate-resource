package org.danielli.xultimate.browser.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.danielli.xultimate.browser.service.ResourceBrowserService;
import org.danielli.xultimate.context.image.model.GeometryOperator;
import org.danielli.xultimate.context.image.model.ImageGeometry;
import org.danielli.xultimate.context.image.model.ImageSize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@Controller
public class ResourceBrowserController {

	@Resource(name = "resourceBrowserServiceImpl")
	private ResourceBrowserService resourceBrowserService;
	
	@RequestMapping(method = { RequestMethod.GET }, 
			value = { "/generic/{resourcePath:g\\d+/M\\d+/.*}", "/800x800/{resourcePath:g\\d+/M\\d+/.*}" }
	)
	@ResponseBody
	public byte[] showGenericResource(@PathVariable("resourcePath") String resourcePath, HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		try {
			return resourceBrowserService.getResourceByPath(resourcePath); 
		} catch (Exception e) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
	}
	
	private ImageGeometry imageGeometry350x350 = new ImageGeometry(new ImageSize(350, 350), GeometryOperator.Emphasize);
	
	@RequestMapping(method = { RequestMethod.GET }, 
			value = { "/350x350/{imagePath:g\\d+/M\\d+/.*}" }
	)
	@ResponseBody
	public byte[] show350x350Image(@PathVariable("imagePath") String imagePath, HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		try {
			return resourceBrowserService.getImageByPath(imagePath, imageGeometry350x350); 
		} catch (Exception e) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
	}
	
	private ImageGeometry imageGeometry160x160 = new ImageGeometry(new ImageSize(160, 160), GeometryOperator.Emphasize);
	
	@RequestMapping(method = { RequestMethod.GET }, 
			value = { "/160x160/{imagePath:g\\d+/M\\d+/.*}" }
	)
	@ResponseBody
	public byte[] show160x160Image(@PathVariable("imagePath") String imagePath, HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
		try {
			return resourceBrowserService.getImageByPath(imagePath, imageGeometry160x160); 
		} catch (Exception e) {
			throw new NoSuchRequestHandlingMethodException(request);
		}
	}
}
