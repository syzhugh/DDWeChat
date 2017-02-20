'''
1.Login
'''
import os
import http.cookiejar
import urllib.request, urllib.parse, urllib.error
import re
import time
import xml.sax
import json
import sys
import threading

# for pic upload
import hashlib
import mimetypes
import requests
from requests_toolbelt import MultipartEncoder

from UserGroup import UserGroup
from UserNormal import UserNormal

'''variables'''

_cookie = None

_uuid = ''

_QRImagePath = os.getcwd() + '/qrcode.jpg'

_tip = -1
_redirect_uri = ''
_base_uri = ''

_skey = ''
_wxsid = ''
_wxuin = ''
_pass_ticket = ''
_BaseRequest = {}
_currentuser = ''
_synckey = {}


_mtarget = ''

_webwx_data_ticket = None
deviceId = "e000000000000000"

_normal = []
_group = []

'''class'''
# class Me(object):

def getUUID():
	global _uuid
	
	response = None
	url = "https://login.wx.qq.com/jslogin"
	params = urllib.parse.urlencode({
		"appid":"wx782c26e4c19acffb",
		"redirect_uri":"https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage",
		"fun":"new",
		"lang":"zh_CN",
		"_":int(time.time())
	}).encode("utf-8")
	req = urllib.request.Request(url,data = params,method = "get")
	with urllib.request.urlopen(req) as f:
		response = f.read()
	
	if response!=None:
		regx = r'window.QRLogin.code = (\d+); window.QRLogin.uuid = "(\S+?)"'
		pm = re.search(regx, str(response))
		code = int(pm.group(1))
		uuid = str(pm.group(2))
		if code == 200:
			_uuid = uuid
			return True
		else:
			return False
	else:
		return False

def getQRImg():
	global _tip
	
	url = "https://login.weixin.qq.com/qrcode/" + _uuid
	req = urllib.request.Request(url,method = "get")
	
	with urllib.request.urlopen(req) as data:
		with open(_QRImagePath,"wb") as imgfile:
			imgfile.write(data.read())
			os.startfile(_QRImagePath)
	_tip = 1
	
def waitForLogin():
	global _tip,_redirect_uri,_base_uri
	url = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login"
	params = urllib.parse.urlencode({
		"loginicon":"true",
		"uuid":_uuid,
		"tip":_tip,
		"_":int(time.time())
	}).encode("utf-8")
	
	response = None
	req = urllib.request.Request(url,data = params,method = "get")
	with urllib.request.urlopen(req) as data:
		response = data.read()
	
	if response==None:
		return -1
		
	regx = r'window.code=(\d+);'
	pm = re.search(regx, str(response))
	code = int(pm.group(1))
	
	if code == 201:  # 已扫描
		print('成功扫描,请在手机上点击确认以登录')
		_tip = 0
	elif code == 200:  # 已登录
		print('正在登录...')
		# window.code=200;
		# window.redirect_uri="https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?
		# ticket=AyaFkr_Zh3BvQA8Uw2rCy4bD@qrticket_0
		# &uuid=QaaSZUIG3A==
		# &lang=zh_CN
		# &scan=1483586519";
		regx = r'window.redirect_uri="(\S+?)";'
		pm = re.search(regx, str(response))
		_redirect_uri = pm.group(1) + '&fun=new'
		_base_uri = _redirect_uri[:_redirect_uri.rfind('/')]
		
		# 测试：uri是否正确赋值
		# print("_redirect_uri:",_redirect_uri,'\n')
		# print("_base_uri:",_base_uri,'\n')
		
		pass
	elif code == 408:  # 超时
		print("---------超时----------")
		_tip = 0
		pass
	return code
	
def login():
	global _skey,_wxsid,_wxuin,_pass_ticket,_BaseRequest
	url = _redirect_uri + "&fun=new" + "&version=v2"
	info = None
	with urllib.request.urlopen(url) as data:
		info = data.read()
		
	# <error>
		# <ret>0</ret>
		# <message/>
		# <skey>@crypt_22a14847_1b793dbe2ec293c4208b7be506b80a84</skey>
		# <wxsid>bfKh7JbkG94pDqkx</wxsid>
		# <wxuin>763395811</wxuin>
		# <pass_ticket>i%2Fqqb8i3ox4jRVl%2Bzi4Bg%2BtjRBqeooh7JeY87v0EMixbc%2Bzx8spe%2FuzPL8O9CGxV</pass_ticket>
		# <isgrayscale>1</isgrayscale>
	# </error>

	class KeysHandler(xml.sax.ContentHandler):
		def __init__(self):
			self.currenttag = ""
			self.skey = ""
			self.wxsid = ""
			self.wxuin = ""
			self.pass_ticket = ""
		
		def startElement(self,tag,attributes):
			self.currenttag = tag
		def characters(self,content):
			if	self.currenttag == "skey":
				self.skey = content
			if	self.currenttag == "wxsid":
				self.wxsid = content
			if  self.currenttag == "wxuin":
				self.wxuin = content
			if  self.currenttag == "pass_ticket":
				self.pass_ticket = content
		def endElement(self,tag):	
			pass
	
	if info == None:
		return False
	else:
		mhandler = KeysHandler()
		xml.sax.parseString(info,mhandler)
		_skey = mhandler.skey
		_wxsid = mhandler.wxsid
		_wxuin = mhandler.wxuin
		_pass_ticket = mhandler.pass_ticket

		# 测试：keys是否正确赋值
		# print("_skey:",_skey,'\n')
		# print("_wxsid:",_wxsid,'\n')
		# print("_wxuin:",_wxuin,'\n')
		# print("_pass_ticket:",_pass_ticket,'\n')
		
		print(_skey,_wxsid,_wxuin,_pass_ticket)
		if _skey == '' or _wxsid == '' or _wxuin == '' or _pass_ticket == '':
			return False
		
		# {"BaseRequest":
			# {"Uin":"763395811",
			# "Sid":"PuISCXjhirV/hLZk",
			# "Skey":"@crypt_22a14847_a5f099a1b340c34b416093d7f0b8f866",
			# "DeviceID":"e977402533652388"
			# }
		# }
		
		_BaseRequest = {
			'Uin': _wxuin,
			'Sid': _wxsid,
			'Skey': _skey,
			'DeviceID': deviceId,
		}
		return True

# 这个好像没有什么卵用，只是初始聊天面板的信息
def init_wechat():
	global _currentuser,_synckey
	# https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxinit?
	# r=-1834588146
	# &lang=zh_CN
	# &pass_ticket=YHcD9wXUpoN4ecurTjweTFFd9OzzakFl3jnzsWw2axr423R5xTXpkh0qmEjYjBdb

	url = _base_uri+"/webwxinit?lang=%s&pass_ticket=%s" %("zh_CN",_pass_ticket)
	params = {
		'BaseRequest': _BaseRequest
	}
	print(json.dumps(params))
	req = urllib.request.Request(url=url, data=json.dumps(params).encode('utf-8'),method = "post")
	req.add_header('ContentType', 'application/json; charset=UTF-8')
	response = None
	with urllib.request.urlopen(req) as data:
		response = data.read()
		debug_writeinfo("init_info.txt",response)
		
	_currentuser = json.loads(response.decode())['User']['UserName']
	_synckey = json.loads(response.decode())['SyncKey']
	
	
# 这个是关键的	
def getContacts():
	global _normal,_group
	
	# notify
	# url = _base_uri+"/webwxstatusnotify?lang=%s&pass_ticket=%s" %("zh_CN",_pass_ticket)
	# params = {
		# 'BaseRequest': _BaseRequest,
		# 'ClientMsgId':int(time.time()),
		# 'Code':3,
		# 'FromUserName':_currentuser,
		# 'ToUserName':_currentuser
	# }
	# req = urllib.request.Request(url=url, data=json.dumps(params).encode('utf-8'),method = "post")
	# req.add_header('ContentType', 'application/json; charset=UTF-8')
	# with urllib.request.urlopen(req) as data:
		# with open("C:/Sun/WeChat/contacts_temp.txt","wb") as f:
			# response = data.read()
			# f.write(response)
			
	# getcontact
	url = _base_uri+"/webwxgetcontact?lang=zh_CN&pass_ticket=%s&r=%s&seq=%s&skey=%s" % (_pass_ticket,int(time.time()),0,_skey)
	
	req = urllib.request.Request(url)
	mcontacts = None
	with urllib.request.urlopen(req) as data:
		print(data.status,data.reason)
		mcontacts = data.read()
		debug_writeinfo("info_contacts.txt",mcontacts)
	
	if not mcontacts == None:
		mcontacts = mcontacts.decode('utf-8', 'replace')
	mcontacts_dict = json.loads(mcontacts)
	mcontacts_list = mcontacts_dict['MemberList']
	
	# 公众号：	VerifyFlag >0 8的倍数		x
	# 特殊账号：username --> xxx			x
	# 群聊：	username --> @@xxx			x
	# 普通：	username --> @xxx			o
	# 自己：	username --> _currentuser	x
	print("contacts count:",len(mcontacts_list))
	
	
	for index in range(len(mcontacts_list)-1,-1,-1):
		user = mcontacts_list[index]
		if (user["VerifyFlag"]>0 or user["UserName"][0] != '@' or user["UserName"] == _currentuser):
			del mcontacts_list[index]
		elif user["UserName"][0:2] == '@@':
			_group.append(user)
		elif user["UserName"][0] == '@':
			_normal.append(user)
			
	print("group count:",len(_group))
	print("normal count:",len(_normal))
	
	size = 0
	for item in _normal:
		if item["Alias"] == "":
			print(item["NickName"],item["RemarkName"])
			size = size+1
	print("没有微信号的人:",size)
	
	

def getHeadImg():
	for item in _normal:
		
		url = "https://wx2.qq.com" + item["HeadImgUrl"]
		req = urllib.request.Request(url,method = "get")
		
		with urllib.request.urlopen(req) as data:
			with open("C:/Sun/WeChat/icon/" + item["UserName"]+".jpg","wb") as file:
				file.write(data.read())
			
def sendMsg(content):
	mtarget = None
	for temp in _normal:
		if temp["NickName"].find("房车")>-1:
			mtarget = temp["UserName"]
			break

	if mtarget == None:
		return

	url = _base_uri + "/webwxsendmsg?lang=zh_CN&pass_ticket=%s"%(_pass_ticket)
	print(url)
	mtime = str(int(time.time()))

	params = {
		"BaseRequest": _BaseRequest,
		"Msg": {
			"ClientMsgId": mtime,
			"Content": content,
			"FromUserName": _currentuser,
			"LocalID": mtime,
			"ToUserName": mtarget,
			"Type": 1
		},
		"Scene": 0
	}

	req = urllib.request.Request(url=url, data=json.dumps(params,ensure_ascii=False).encode("utf-8"),method = "post")
	req.add_header('ContentType', 'application/json; charset=UTF-8')
	with urllib.request.urlopen(req) as data:
		print(data.read())
		

def sendImg1():
	global _webwx_data_ticket
	
	print("/////use requests////////////////////")
	url = 'https://file.wx2.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json'
	imgPath = "C:/Users/ZS27/Desktop/init.png"
	
	file_name = "init.png"
	file_size = os.path.getsize(imgPath)
	file_md5 = md5_file(imgPath)
	file_modifiedtime = time.strftime("%a %b %d %Y %H:%M:%S GMT%z", time.localtime())+' (中国标准时间)'
	
	
	
	for item in _cookie:
		if item.name == "webwx_data_ticket":
			_webwx_data_ticket = item.value
			break
	
	uploadmediarequest = json.dumps({
            "BaseRequest": _BaseRequest,
            "ClientMediaId": int(time.time()),
            "TotalLen": file_size,
            "StartPos": 0,
            "DataLen": file_size,
            "MediaType": 4
        }, ensure_ascii=False ).encode('utf8')
	
	multipart_encoder = MultipartEncoder(
		fields={
			'id': 'WU_FILE_0',
			'name': file_name,
			'type': "image/png",
			'lastModifieDate': file_modifiedtime,
			'size': str(file_size),
			'mediatype': "pic",
			'uploadmediarequest': uploadmediarequest,
			'webwx_data_ticket': _webwx_data_ticket,
			'pass_ticket': _pass_ticket,
			'filename': (file_name, open(imgPath, 'rb'), "image/png")
		},
		boundary='----WebKitFormBoundaryepUtwaH1zH2RLvos'
	)
	
	headers = {
		'Host': 'file.wx2.qq.com',
		'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36',
		'Accept': '*/*',
		'Accept-Language': 'zh-CN,zh;q=0.8',
		'Accept-Encoding': 'gzip, deflate, br',
		'Content-Type': multipart_encoder.content_type,
		'Content-Length':str(file_size),
		'Referer': 'https://wx2.qq.com/',
		'Origin': 'https://wx2.qq.com',
		'Connection': 'keep-alive',
	}
	
	mresponse = requests.post(url, data=multipart_encoder, headers=headers)
	
	req1_dic = json.loads(mresponse.text)
	mediaid = req1_dic['MediaId']
	
	print('--------------getMediaid------------------')
	
	url2 = _base_uri + "/webwxsendmsgimg?fun=async&f=json"
	mtarget = None
	for temp in _normal:
		if temp["NickName"].find("房车")>-1:
			mtarget = temp["UserName"]
			break
	
	if mtarget == None:
		return
	mtime = str(int(time.time()))
	params = {
		"BaseRequest": _BaseRequest,
		"Msg": {
			"ClientMsgId": mtime,
			"FromUserName": _currentuser,
			"LocalID": mtime,
			"MediaId": mediaid,
			"ToUserName": mtarget,
			"Type": 3
		},
		"Scene": 0
	}
	
	print('--------------starttosend------------------')
	
	req = urllib.request.Request(url=url2, data=json.dumps(params,ensure_ascii=False).encode("utf-8"),method = "post")
	req.add_header('ContentType', 'application/json; charset=UTF-8')
	
	with urllib.request.urlopen(req) as data:
		temp = data.read()
		print(temp)
		
def sendLargeImg():
	pass
		
		
def addUserInGroup(item):
	
	# target_group = None
	# for item in _group:
		# if item["NickName"].find("微信辅助测试")>-1:
			# target_group = item
			# break
		# pass
	print(item["NickName"])
	
	username = item["UserName"]
	
	url = _base_uri + "/webwxbatchgetcontact?type=ex&r=%s&lang=zh_CN&pass_ticket=%s"%(int(time.time()),_pass_ticket)
	
	# https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxbatchgetcontact?
	# type=ex&r=1484995527676&pass_ticket=HZqmS6beL8qoml2iiXwkkL%252Br6y%252Bt%252BLqlSHzfxV2NhLfwkT0L0%252FhSNCJ%252B2aPApThL
	
	params = {
		"BaseRequest": _BaseRequest,
		"Count": 1,
		"List": [
			{
			"EncryChatRoomId": "",
			"UserName": username
			}
		]
	}
	req = urllib.request.Request(url, data=json.dumps(params,ensure_ascii=False).encode("utf-8"),method = "post")
	req.add_header('ContentType', 'application/json; charset=UTF-8')
	
	print("----获取群聊信息-----")
	temp = None
	with urllib.request.urlopen(req) as data:
		temp = data.read()
		debug_writeinfo("target_group.txt",temp)
		
	if temp != None:
		group_memeber = json.loads(temp.decode("utf-8","replace"))["ContactList"][0]["MemberList"]
		print ("group_memeber num:",len(group_memeber))
		
		unadded = []
		
		for item in group_memeber:
			exists = False
			for friend in _normal:
				if item["UserName"] == friend["UserName"]:
					exists = True
					break
			if not exists:
				unadded.append(item)
				
		print ("unadded num:",len(unadded))
		
		# tick = 0
		# num = 0
		# for item in unadded:
			# adduser(item)
			# num += 1
			# print("已经添加好友：" , num)
			# tick += 1
			# time.sleep(20)
			# if tick == 10:
				# time.sleep(60*60)
				# tick = 0

			
def adduser(item):
	url = _base_uri + "/webwxverifyuser?lang=zh_CN&r=%s&pass_ticket=%s"%(int(time.time()),_pass_ticket)
	
	print(item["NickName"])
	
	params = {
		"BaseRequest": _BaseRequest,
		"Opcode": 2,
		"SceneList": [33],
		"SceneListCount": 1,
		"VerifyContent": " ",
		"VerifyUserList": [
			{
				"Value": item["UserName"],
				"VerifyUserTicket": ""
			}
		],
		"VerifyUserListSize": 1,
		"skey": _skey
	}
	
	req = urllib.request.Request(url, data=json.dumps(params,ensure_ascii=False).encode("utf-8"),method = "post")
	req.add_header('ContentType', 'application/json; charset=UTF-8')
	
	temp = None
	with urllib.request.urlopen(req) as data:
		temp = data.read()
		print(temp)

def check_Remarkname():
	
	
	num = 0
	for item in _normal:
		# if item["Alias"] == "":
		# if item["RemarkName"].find('?')>-1:
		if True:
			print(item["NickName"],item["RemarkName"])
			rename_Remarkname(item)
			# print(_synckey)
			# infoSync()
			num+=1
			print(num)
			time.sleep(15)
			
		# if num==10:
			# time.sleep(60*10)
			# num = 0
			
			
def rename_Remarkname(item):
	url = _base_uri + "/webwxoplog?lang=zh_CN&pass_ticket=%s"%(_pass_ticket)
	params = {
		"BaseRequest": _BaseRequest,
		"CmdId": 2,
		"RemarkName": item["RemarkName"]+"*",
		# "RemarkName": "*",
		"UserName": item["UserName"]
	}
	req = urllib.request.Request(url, data=json.dumps(params,ensure_ascii=False).encode("utf-8"),method = "post")
	req.add_header('ContentType', 'application/json; charset=UTF-8')
	
	temp = None
	with urllib.request.urlopen(req) as data:
		temp = data.read()
		print(temp)
		
			
def formatSyncKey():
	list = []
	for item in _synckey['List']:
		list.append(str(item['Key']) + '_' + str(item['Val']))
		
	return '|'.join(list)
			
def checkSync():
	while True:
		print('-----checkSync')
		
		str_url = 'https://webpush.wx2.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=%s&skey=%s&sid=%s&uin=%s&deviceid=%s&synckey=%s&_=%s' % (int(time.time()),_skey,_wxsid,_wxuin,deviceId,formatSyncKey(),int(time.time()))
		
		# print(str_url)
		req = urllib.request.Request(str_url,method = "get")
		
		temp = None
		with urllib.request.urlopen(req) as data:
			temp = data.read()
			print(temp)
		
		str_find = re.search(r'selector:"(\d+)"',temp.decode('utf-8','ignore')).group(1)
		if int(str_find) > 0:
			infoSync()
			
		print('checkSync-----')

		
def infoSync():
	print('-----infoSync')
	global _synckey
	str_url = _base_uri + '/webwxsync?sid=%s&skey=%s&lang=zh_CN'%(_wxsid,_skey)
	
	params = {
		"BaseRequest": _BaseRequest,
		"SyncKey":_synckey,
		"rr": int(time.time())
	}
	
	req = urllib.request.Request(str_url,data=json.dumps(params,ensure_ascii=False).encode("utf-8"),method = "post")
	
	temp = None
	with urllib.request.urlopen(req) as data:
		temp = data.read()
		with open("C:/Sun/WeChat/sync/" + str(int(time.time())) +".txt","wb") as file:
			file.write(temp)
			
	if temp != None:
		info = json.loads(temp.decode())
		# print(_synckey)
		_synckey = info['SyncKey']
		# print(_synckey)
		print("sync:",info['BaseResponse'])
		
	print('infoSync-----')
	
	
	
	
def debug_writeinfo(name,info):
	parent_path = 'C:/Sun/WeChat/'
	with open(parent_path + name,'wb') as f:
		f.write(info)
	

	
def main_login_test():
	global _cookie
	_cookie = http.cookiejar.CookieJar()
	opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(_cookie))
	urllib.request.install_opener(opener)
	
	# step1	进入界面
	while not getUUID():
		print("----连接失败----")
		
	# step2 获取二维码
	getQRImg()
		
	# step3	等待登陆
	while waitForLogin() != 200:
		pass
	os.remove(_QRImagePath)
	
	# step4 登陆成功，信息初始化
	if not login():
		print("信息获取失败")
		return
	
	# step5 网页微信初始化
	init_wechat()
	
	# step6 获取联系人信息
	getContacts()
	
	# getHeadImg()
	
	# step6.5 开启同步
	# checkThread = threading.Thread(target=checkSync, name='checkSync')
	# checkThread.start()
	
	
	# step7
	# while True:
		# content = input("请输入发送内容\n:")
		# sendMsg(content)
		
	# step8
	# sendImg1()
	# sendLargeImg()
	
	#step9
	for item in _group:
		addUserInGroup(item)
	
	# step10
	# check_Remarkname()
	
	
	# 未完成
	# # 同步
	# # 刷新数据的获取
	


def md5_file(filepath):
	m = hashlib.md5()
	with open(filepath, 'rb') as f:
		m.update(f.read())
	return m.hexdigest()
	
if __name__ == "__main__":
	main_login_test()

	