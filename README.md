# xultimate-resource #

采用Spring MVC，用于在FastDFS分布式文件系统中完成资源管理(上传、删除、下载)的脚手架。图片类资源的缩放、剪裁、水印等功能通过已封装好AWT和im4java进行处理。


#### 图片读取架构LVS(Keepalived) + HAProxy + Varnish + Tengine + FastDFS + GD ####

* LVS作为第一层负载均衡，通过Keepalived实现高可用，采用"DR"模式，"最小连接"调度算法。可水平扩展。
* HAProxy作为第二层负载均衡，不需要实现高可用，采用"一致性Hash"算法。可水平扩展。
* Varnish作为反向代理服务器，主要用于缓存服务器，多台部署，采用"URL Hash"算法，不实现高可用。支持水平扩展，根据上层负载均衡算法衡量数据丢失。
* Tengine作为Web服务器，可添加浏览器缓存，不实现高可用。水平扩展程度视FastDFS的存储节点而定。
* FastDFS作为资源的存储中心，支持水平扩展、高可用。通过fastdfs-nginx-module和Tengine配合使用。
* GD实现实时缩略图功能。通过ngx_http_image_filter_module模块和Tengine配合使用。
* 通过Tengine的rewrite指令实现访问同一资源的不同地址(/350x350/g1/M01...a.jpg、/160x160/g1/M01...a.jpg)完成不同缩略图展现功能(参考京东图片展示)。
	
	
#### 图片读取架构LVS(Keepalived) + HAProxy + Varnish + Tomcat(AIO/NIO) + FastDFS + GraphicsMagick/ImageMagick/AWT ####

* LVS作为第一层负载均衡，通过Keepalived实现高可用，采用"DR"模式，"最小连接"调度算法。可水平扩展。
* HAProxy作为第二层负载均衡，不需要实现高可用，采用"一致性Hash"算法。可水平扩展。
* Varnish作为反向代理服务器，主要用于缓存服务器，多台部署，采用"URL Hash"算法，不实现高可用。支持水平扩展，根据上层负载均衡衡量数据丢失。
* Tomcat作为Java应用服务器，可添加浏览器缓存，水平扩展程度视FastDFS的存储节点而定。
* FastDFS作为资源的存储节点，支持水平扩展、高可用。通过JAVA API调用获取图片。
* 默认使用GraphicsMagick实现实时缩略图功能。可选择ImageMagick或AWT。GraphicsMagick/ImageMagick的调用通过im4java完成。
* 通过Spring MVC支持访问同一资源的不同地址(/350x350/g1/M01...a.jpg、/160x160/g1/M01...a.jpg)完成不同缩略图展现功能(参考京东图片展示)。

	
## xultimate-browse ##

* 该项目提供的功能用于替代Tengine、fastdfs-nginx-module、ngx_http_image_filter_module模块，前者模块可实现资源浏览，后者模块用于实时缩略图，但是没有使用GraphicsMagick。
* 提供资源下载和图片下载功能，采用浏览器缓存。图片下载功能支持原图和实时缩略图，图片处理功能默认使用im4java + GraphicsMagick实现，支持AWT或ImageMagick。
* 项目Web层采用Spring MVC。
* 浏览器缓存功能通过xultimate-web工程的BrowserCacheGenerator完成。
* 图片处理(缩放等)功能采用xultimate-context的image模块处理。
* 资源下载功能采用xultimate-context-support的dfs/fastdfs模块处理。


## xultimate-upload ##

* 提供资源上传和图片上传，以及资源删除功能。
* 项目Web层采用Spring MVC。
* 图片处理(缩放等)功能采用xultimate-context的image模块处理。
* 资源上传、删除功能采用xultimate-context-support的dfs/fastdfs模块处理。