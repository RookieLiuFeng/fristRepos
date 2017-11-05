var $menu_tree;
$(function() {
    initMenuTree();
});

function initMenuTree(){
    //组织机构树
    $menu_tree = $("#menu_tree").tree({
        url : ctxAdmin+"/index/navTree",
        method:'get',
        animate:true,
        lines:true,
        onClick:function(node){
            var url = node.attributes.url;
            if(url){

                if(url.substring(0,4) == "http"){

                }else if(url.substring(0,1) == "/" ){
                    url = ctx + url;
                }else{
                    url = ctxAdmin+'/' + url;
                }
                eu.addTab($layout_center_tabs,node.text,url,true,node.iconCls);
            }
        }
    });
}