# -*- coding: utf-8 -*-

import datetime
import re
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
from scrapy.selector import Selector
from scrapy.http import Request
from scrapy.spiders import CrawlSpider
from spider.items import CompanyNameVerify

class saic_gov_cn_Spider(CrawlSpider):
	name = 'saic_gov_cn1'

	allowed_domains=['home.saic.gov.cn']
	start_urls = [
        'http://home.saic.gov.cn/qyj/ggxx'
	]

	def parse(self,response):
		yield Request('http://home.saic.gov.cn/qyj/ggxx',callback=self.parse_second)
		i=1
		while (i<25):
			url1 = self.start_urls[0]+'/index_'+str(i)+'.html'
			i+=1
			yield Request(url1,callback=self.parse_second)

	def parse_second(self, response):
		urllist2 = response.xpath('//div[@class="gts_contentLeftList gts_contentLeftListbox"]//@href').extract()
		for url2 in urllist2:
			yield Request(self.start_urls[0]+url2,callback=self.parse_item)
	
	def parse_item(self, response):
		response_url = response.url
		items = []

		text= response.xpath('//div[@class="TRS_Editor"]/text()').extract()
		counter=1
		for t in text:
			item = CompanyNameVerify()
			if t == u'\n':

				continue
			item['pub_time'] = t.split()[0]
			item['register_num'] = t.split()[1]
			item['company_name'] = t.split()[2]
			item['crawltime'] = datetime.datetime.now().strftime('%Y-%m-%d %H:%M')
			item['source_domain'] = 'http://home.saic.gov.cn^国家工商总局'
			item['url'] = response_url
			item['spider_name']=self.name
			insurance = re.compile(u'\u4fdd\u9669')
			invest = re.compile(u'\u6295\u8d44')
			trust = re.compile(u'\u4fe1\u6258')
			asset_mana = re.compile(u'\u8d44\u4ea7\u7ba1\u7406')
			finance = re.compile(u'\u91d1\u878d')
			holding = re.compile(u'\u63a7\u80a1')
			capital = re.compile(u'\u8d44\u672c')
			consumer_finance=re.compile(u'\u6d88\u8d39\u91d1\u878d')

			search1 = insurance.search(t.split()[2])
			search2 = invest.search(t.split()[2])
			search3 = trust.search(t.split()[2])
			search4 = asset_mana.search(t.split()[2])
			search5 = finance.search(t.split()[2])
			search6 = holding.search(t.split()[2])
			search7 = capital.search(t.split()[2])
			search8 = consumer_finance.search(t.split()[2])
			item['trade'] = ''
			
			if search8:
				item['trade'] = '008'
			if search7:
				item['trade'] = '007'
			if search6:
				item['trade'] = '006'
			if search5:
				item['trade'] = '005'
			if search4:
				item['trade'] = '004'
			if search3:
				item['trade'] = '003'
			if search2:
				item['trade'] = '002'
			if search1:
				item['trade'] = '001'
			if item['trade'] == '':
				item['trade'] = '009'
			items.append(item)
		return items
	
		
        
