seajs.config({
    alias: {
        'jquery': 'https://cdn.bootcss.com/jquery/1.9.1/jquery.min.js',
        '$': 'https://cdn.bootcss.com/jquery/1.9.1/jquery.min.js',
        'jquery.migrate': 'https://cdn.bootcss.com/jquery-migrate/1.2.1/jquery-migrate.min.js',
        'plugins': 'http://www.noneone.cn:8081/noneoneblog/assets/js/plugins.js',

            /* modules */
        'main': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/main.js',
        'authc': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/authc.js',
        'sidebox': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/sidebox.js',
        'post': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/post.js',
        'comment': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/comment.js',
        'phiz': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/phiz.js',
        'avatar': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/avatar.js',
        'editor': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/editor.js',
        'view': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/view.js',
        'webuploader': 'http://www.noneone.cn:8081/noneoneblog/assets/js/modules/webuploader.js',

            /* vendors */
        'bootstrap': 'https://cdn.bootcss.com/bootstrap/3.0.3/js/bootstrap.js',
        'baguetteBox': 'https://cdn.bootcss.com/baguettebox.js/1.4.0/baguetteBox.min',
        'layer': 'https://cdn.bootcss.com/layer/2.1/layer.js',
        'pace': 'https://cdn.bootcss.com/pace/1.0.2/pace',
        'pjax': 'https://cdn.bootcss.com/jquery.pjax/1.7.0/jquery.pjax',
        'jcrop': 'https://cdn.bootcss.com/jquery-jcrop/0.9.12/js/jquery.Jcrop.min',
        'validate': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/validate/jquery-validate.js',
        'lazyload': 'https://cdn.bootcss.com/jquery_lazyload/1.9.3/jquery.lazyload.js',

        'ueditor': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/ueditor/ueditor.all.min.js',
        'ueditor.config': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/ueditor/ueditor.config.js',
        'ueditor.parse': 'http://www.noneone.cn:8081/noneoneblog/assets/vendors/ueditor/ueditor.parse.min.js'
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