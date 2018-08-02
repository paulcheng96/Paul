# -*- coding: utf-8 -*-

import datetime
import re
import sys

reload(sys)
sys.setdefaultencoding('utf-8')
from scrapy.selector import Selector
from scrapy.http import Request
from scrapy.spiders import CrawlSpider
from spider.items import RawInsuranceKnowledge


class Qzr_cnSpider(CrawlSpider):
    name = 'qzr_cn_1'
    allowed_domains = ['www.qzr.cn']
    start_urls = [
        'http://www.qzr.cn/small/bxcd/bxcdc/index_1.shtml',
        'http://www.qzr.cn/small/bxcd/bxcdc/index_2.shtml',
        'http://www.qzr.cn/small/bxcd/bxcdc/index_3.shtml'
    ]

    def parse(self, response):
        sel = Selector(response)
        urllist = sel.xpath('//div[@align="left"]/a/@href').extract()
        titles = sel.xpath('//div[@align="left"]/a/text()').extract()

        counter = 0
        while (counter < len(urllist)):
            requesturl = response.urljoin(urllist[counter])
            title = titles[counter]
            request = Request(requesturl, callback=self.parse_item)
            request.meta['title'] = title
            counter += 1
            yield request

    def parse_item(self, response):
        items = []
        item = RawInsuranceKnowledge()
        item['url'] = response.url
        item['terms_question'] = response.meta['title']
        item['explain_answer'] = response.xpath('//td[@class="artibody"]//text()').extract()[-1]
        item['crawltime'] = datetime.datetime.now().strftime('%Y-%m-%d %H:%M')
        item['spider_name'] = self.name
        item['source_domain'] = 'www.qzr.cn^圈中人'
        item['insurance_type'] = ''
        items.append(item)
        print items
        return items

