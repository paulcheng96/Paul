# -*- coding: utf-8 -*-
import datetime
import re
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
from scrapy.selector import Selector
from scrapy.http import Request
from scrapy.spiders import CrawlSpider
from spider.items import RawInsuranceProduct

class iaChina_cn_ai1_Spider(CrawlSpider):

    name = 'iachina_cn_ai1'
    allowed_domains = ['iachina.cn']
    start_urls = ["http://www.iachina.cn/col/col1817/index.html"]

    def parse(self, response):
        sel = Selector(response)
        urllist = sel.xpath('//ul[@class="xhyw-list"]/li/div/a/@href').extract()
        for url in urllist:
            requesturl = response.urljoin(url)
            yield Request(requesturl, callback=self.parse_second)

    def parse_second(self,response):

        sel1 = Selector(response)
        urllist_second = sel1.xpath('//ul[@class="xhyw-list"]/li/div/a/@href').extract()
        for url in urllist_second:
            requestrul_second = response.urljoin(url)

            yield Request(requestrul_second,callback=self.parse_item)

    def parse_item(self, response):
        items=[]
        sel = Selector(response)
        item = RawInsuranceProduct()
        title = sel.xpath('//h1/text()').extract_first()
        company = sel.xpath('//div[@class="wzzw"]/p[1]//text()').extract()
        if company == [u'\xa0']:
            company = sel.xpath('//div[@class="wzzw"]/h2[1]/span/text()').extract()

        tiaokuan = sel.xpath('//div[@class="wzzw"]//*/text()').extract()
        del(tiaokuan[0])

        for str in tiaokuan:
            if str == u'\xa0':
                tiaokuan.remove(u'\xa0')

        tiaokuan = '\n\n'.join(tiaokuan)
        tiaokuan = tiaokuan.replace(u'\xa0',u'')

        item['url'] = response.url
        item['insurance_product'] = title
        item['insurance_company'] = company[0]
        item['crawltime'] = datetime.datetime.now().strftime('%Y-%m-%d %H:%M')
        item['insurance_type'] = sel.xpath('//td[@class="outstanding"]//a/text()').extract()[-2] + '_' + sel.xpath('//td[@class="outstanding"]//a/text()').extract()[-1]
        item['product_content'] = tiaokuan
        item['spider_name'] = 'iachina_cn_ai1'
        item['source_domain'] = 'iachina.cn^中国保险行业协会'
        items.append(item)
        #print  item['product_content'],'-------------'
        return items

