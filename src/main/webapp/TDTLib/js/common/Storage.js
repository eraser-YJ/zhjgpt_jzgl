/**
 * �ｷ��筝�筝�localStorage����sessionStorage�� 絲剛��ie7篁ュ����筝�����storage��羌頵����菴���筝�筝�yl_Storage絲壕院��
 * 
 * @param {String}
 *            type: "local" or "session"��"local"���莅ゅ�若��
 * @return {Storage} localStorage or sesssionStorage
 */
var getStorage = function(type) {
	if (!window.localStorage) {
		if (window.globalStorage) {
			// try/catch for file protocol in Firefox
			try {
				window.localStorage = window.globalStorage;
			} catch (e) {
				console.err("��羈���綮�globalStorage");
			}
		} else {
			var storage = new IeStorage();
			window.localStorage = storage;
		}
	} else if (type === "session"){
		return window.sessionStorage;
	}
	return window.localStorage;
}

/*
 * File: IeStorage.js Author: 罌���藝� Modify: 罌���藝� 2014.12.10
 */

// 罩ょ瓜������綮阪��膊∞��膽�絖�鐚�yl.tool.IeStorage絲壕院����絲�ie7篁ヤ�筝�����window.localStorage��罔≧����絲壕院��
// 篏睡���区�莪��� yl_sys.getStorage()��
IeStorage = function() {
	var CLASS = IeStorage, thi$ = CLASS.prototype;
	if (CLASS.__defined__) {
		this.init.apply(this, arguments);
		return;
	}
	CLASS.__defined__ = true;

	var attrKey = "localStorage";

	this.cleanKey = function(key) {
		return key
				.replace(
						/[^-._0-9A-Za-z\xb7\xc0-\xd6\xd8-\xf6\xf8-\u037d\u37f-\u1fff\u200c-\u200d\u203f\u2040\u2070-\u218f]/g,
						"-");
	};

	this.setItem = function(key, value) {
		this.userData.load(attrKey);
		key = this.cleanKey(key);

		if (!this.userData.getAttribute(key)) {
			this.length++;
		}
		this.userData.setAttribute(key, value);

		this.userData.save(attrKey);
	};

	this.getItem = function(key) {
		this.userData.load(attrKey);
		key = this.cleanKey(key);
		return this.userData.getAttribute(key);
	};

	this.removeItem = function(key) {
		this.userData.load(attrKey);
		key = this.cleanKey(key);
		this.userData.removeAttribute(key);

		this.userData.save(ttrKey);
		this.length--;
		if (this.length < 0) {
			this.length = 0;
		}
	};

	this.clear = function() {
		this.userData.load(attrKey);
		var i = 0;
		while (attr = this.userData.XMLDocument.documentElement.attributes[i++]) {
			this.userData.removeAttribute(attr.name);
		}
		this.userData.save(attrKey);
		this.length = 0;
	};

	this.key = function(key) {
		this.userData.load(attrKey);
		return this.userData.XMLDocument.documentElement.attributes[key];
	};

	this.init = function() {
		var userData = this.userData = document.createElement("input");

		userData.style.display = "none";
		document.getElementsByTagName("head")[0].appendChild(userData);
		userData.addBehavior("#default#userdata");

		userData.load(attrKey);
		this.length = userData.XMLDocument.documentElement.attributes.length;
	};
	this.init.apply(this, arguments);
};