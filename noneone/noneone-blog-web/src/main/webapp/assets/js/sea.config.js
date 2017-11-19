seajs.config({
    alias: {
        'jquery': 'https://cdn.bootcss.com/jquery/1.9.1/jquery.min',
        '$': 'https://cdn.bootcss.com/jquery/1.9.1/jquery.min',
        'jquery.migrate': 'https://cdn.bootcss.com/jquery-migrate/1.2.1/jquery-migrate.min',
        'plugins': 'http://www.noneone.cn:8081/noneoneblog/assets/js/plugins',

            /* modules */
        'main': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/main',
        'authc': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/authc',
        'sidebox': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/sidebox',
        'post': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/post',
        'comment': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/comment',
        'phiz': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/phiz',
        'avatar': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/avatar',
        'editor': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/editor',
        'view': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/view',
        'webuploader': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/webuploader',

            /* vendors */
        'bootstrap': 'https://cdn.bootcss.com/bootstrap/3.0.3/js/bootstrap',
        'baguetteBox': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/baguette/baguetteBox.min',
        'layer': 'https://cdn.bootcss.com/layer/2.1/layer.js',
        'pace': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/pace/pace.min',
        'pjax': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/pjax/jquery.pjax',
        'dmuploader': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/uploader/dmuploader',
        'webuploader.min': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/webuploader/webuploader.min',
        'webuploader.css': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/webuploader/webuploader.css',
        'jcrop': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/jcrop/jquery.jcrop.min',
        'validate': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/validate/jquery-validate',
        'lazyload': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/lazyload/jquery.lazyload',

        'ueditor': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/ueditor/ueditor.all.min',
        'ueditor.config': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/ueditor/ueditor.config',
        'ueditor.parse': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/ueditor/ueditor.parse.min'
    },

    // 预加载项
    preload: [this.JSON ? '' : 'json', 'jquery'],

        // 路径配置
//    paths: {
//        'vendors': '../../vendors',
//    },

    // 变量配置
    vars: {
        'locale': 'zh-cn'
    },

    charset: 'utf-8',

    debug: false
});

var __SEAJS_FILE_VERSION = '?v=1.3';

//seajs.on('fetch', function(data) {
//	if (!data.uri) {
//		return ;
//	}
//
//	if (data.uri.indexOf(app.mainScript) > 0) {
//		return ;
//	}
//
//    if (/\:\/\/.*?\/assets\/libs\/[^(common)]/.test(data.uri)) {
//        return ;
//    }
//
//    data.requestUri = data.uri + __SEAJS_FILE_VERSION;
//
//});
//
//seajs.on('define', function(data) {
//	if (data.uri.lastIndexOf(__SEAJS_FILE_VERSION) > 0) {
//	    data.uri = data.uri.replace(__SEAJS_FILE_VERSION, '');
//	}
//});