var operatorTreeModule = {};

/**
 * 添加节点
 * @param treeObj：树对象
 * @param id：节点id
 * @param parentId： 父节点id
 * @param text： 显示内容
 * @param queue： 排序
 */
operatorTreeModule.addNode = function(treeObj, id, parentId, text, queue) {
    var nodes = treeObj.getSelectedNodes();
    var newNode = {
        id: id, pId: parentId, name: text, deptType: 0, checkState: false, iconSkin: "fa-office",
        leaderName: '', isChecked: '1', userType: '', queue: queue
    };
    treeObj.addNodes(nodes[0], newNode);
    if (nodes[0].children != null && nodes[0].children.length > 0) {
        var children = nodes[0].children;
        var nNode = children[children.length - 1];
        for (var i = children.length - 1; i >= 0; i--) {
            if (nNode.queue < children[i].queue) {
                treeObj.moveNode(children[i], nNode, "prev");
            }
        }
    }
    treeObj.setting.callback.onClick(null, newNode.id, newNode);
};

/**
 * 修改节点
 * @param treeObj: 树对象
 * @param id: 节点id
 * @param name: 节点名
 * @param queue: 排序
 */
operatorTreeModule.editNode = function(treeObj, id, name, queue) {
    var node = treeObj.getNodeByParam("id", id, null);
    node.name = name;
    node.queue = queue;
    node.leaderName = '';
    treeObj.updateNode(node);
    while(!node.isFirstNode){
        var pNode = node.getPreNode();
        treeObj.moveNode(pNode, node, "prev");
    }
    while(true){
        var nNode = node.getNextNode();
        if(nNode != null){
            if(node.queue > nNode.queue){
                treeObj.moveNode(nNode, node, "next");
            }else{
                break;
            }
        }else{
            break;
        }
    }
};

/**
 * 删除节点
 * @param treeObj：树对象
 * @param id： 节点id
 */
operatorTreeModule.deleteNode = function (treeObj, id) {
    var node = treeObj.getNodeByParam("id", id, null);
    var pNode = node.getParentNode();
    treeObj.removeNode(node, true);
    treeObj.selectNode(pNode, false);
    treeObj.setting.callback.onClick(null, pNode.id, pNode);
};

/**
 * 搜索树初始化状态
 * @type {boolean}
 */
operatorTreeModule.searchInitData = null;
operatorTreeModule.rootNode = null;
operatorTreeModule.searchTreeInit = function(config) {
    if (operatorTreeModule.searchInitData == null) {
        operatorTreeModule.searchInitData = config.data;
        $('#ztreeSearchKeyword').keydown(function(event) {
            if (event.keyCode != 13) {
                return;
            }
            operatorTreeModule.filter(config, this.getAttribute("containerId"));
        });

        $('#ztreeSearchBtn').click(function() {
            operatorTreeModule.filter(config, document.getElementById("ztreeSearchKeyword").getAttribute("containerId"));
        });
    }
};

operatorTreeModule.filter = function(config, containerId) {
    console.log(containerId);
    var zTree = $.fn.zTree.getZTreeObj(containerId);
    config.treeObj.initZtree($('#' + containerId), operatorTreeModule.searchInitData);
    zTree.setting.callback.onClick(null, operatorTreeModule.searchInitData[0].id, operatorTreeModule.searchInitData[0]);
    var keyword = $('#ztreeSearchKeyword').val();
    var nodeList = [operatorTreeModule.rootNode];
    if (keyword == '') {
        nodeList = operatorTreeModule.searchInitData;
    } else {
        nodeList = zTree.getNodesByParamFuzzy("name", keyword);
    }
    config.treeObj.initZtree($('#' + containerId), nodeList);
};

/** 待根节点的
operatorTreeModule.searchTreeInit = function(config) {
    if (operatorTreeModule.searchInitData == null) {
        operatorTreeModule.searchInitData = config.data;
        operatorTreeModule.rootNode = config.treeObj.getRootNode();
        operatorTreeModule.rootNode['children'] = [];
        $('#ztreeSearchKeyword').keydown(function(event) {
            if (event.keyCode != 13) {
                return;
            }
            var containerId = this.getAttribute("containerId");
            var zTree = $.fn.zTree.getZTreeObj(containerId);
            config.treeObj.initZtree($('#' + containerId), operatorTreeModule.searchInitData);
            zTree.setting.callback.onClick(null, operatorTreeModule.searchInitData[0].id, operatorTreeModule.searchInitData[0]);
            var keyword = $('#ztreeSearchKeyword').val();
            var nodeList = [operatorTreeModule.rootNode];;
            console.log(nodeList);
            if (keyword == '') {
                nodeList = operatorTreeModule.searchInitData;
            } else {
                var searchNode = zTree.getNodesByParamFuzzy("name", keyword);
                for (var i = 0; i < searchNode.length; i++) {
                    nodeList.push(searchNode[i]);
                }
            }
            config.treeObj.initZtree($('#' + containerId), nodeList);
            //zTree.setting.callback.onClick(null, nodeList[0].id, nodeList[0]);
        });
    }
};
 */

/*cmProjectWeeklyShowListPanel.test = function() {
    $('#ztreeSearchKeyword').keyup(function() {
        var containerId = this.getAttribute("containerId");
        console.log(containerId);
        var zTree = $.fn.zTree.getZTreeObj(config.container);
        if (operatorTreeModule.searchInitData == null) {
            var node = zTree.getNodes();
            operatorTreeModule.searchInitData = zTree.transformToArray(node);
        }
        var keyword = $('#ztreeSearchKeyword').val();
        var nodeList = zTree.getNodesByParamFuzzy("name", keyword);
        if (keyword == '') {
            nodeList = operatorTreeModule.searchInitData;
        }
        config.treeObj.initZtree($('#' + config.container), nodeList);
        zTree.setting.callback.onClick(null, nodeList[0].id, nodeList[0]);
    });
};*/
