# xultimate-image #

提供用于在FastDFS分布式文件系统中完成图片读取（实时缩略图生成）和图片上传功能。
	
	
## xultimate-browser ##

* 提供了图片读取功能。
* 图片读取架构1：LVS(Keepalived) + HAProxy + Varnish + Nginx + FastDFS + GD。其中Nginx通过fastdfs-nginx-module和ngx_http_image_filter_module集成FastDFS和GD，GD实现实时缩略图功能，各个层次都可以水平扩展，不需要该工程完成读取和实时缩略图生成。
* 图片读取架构1：LVS(Keepalived) + HAProxy + Varnish + Tomcat + FastDFS + GraphicsMagick/AWT。图片读取功能由Java负责，缩略图实时生成通过awt或im4java和GraphicsMagick完成，各个层次都可以水平扩展，需要该工程完成。
* 图片读取包含：1.原图或大图展示； 2.缩略图展示。
* 浏览器缓存功能通过xultimate-web工程的BrowserCacheGenerator完成。
* 图片缩放由xultimate-context的image模块处理。
* 图片读取由xultimate-context-support的dfs模块处理。


## xultimate-upload ##

* 提供了图片上传功能。
* 图片上传包括：1.原图上传；2.大图上传。
* 图片处理由xultimate-context的image模块处理。
* 图片上传由xultimate-context-support的dfs模块处理。
