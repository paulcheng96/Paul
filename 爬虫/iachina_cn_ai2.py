# -*- coding: utf-8 -*-

from scrapy.http import FormRequest
import datetime
from scrapy.http import Request
from scrapy.spiders import CrawlSpider
from spider.items import RawInsuranceProduct
import spider.settings as settings
import json
import urllib
import hashlib



class iachina_cn_ai2Spider(CrawlSpider):
    name = "iachina_cn_ai2"
    allowed_domains = ['iachina.cn']
    x = 0

    def start_requests(self):
        """请求列表"""
        datalist = [
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_00_00",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_00_01",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_00_02",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_01_00",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_01_01",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_02_00",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_02_01_00_00",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_02_01_00_01",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_02_01_00_02",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_02_01_01_00",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_02_01_01_01",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_02_01_02",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_02_01_03",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_04_00",},
            {"prodTermsShow.prodTypeCode": "ProdTypeCode_04_01",}
        ]

        for formdata in datalist:

            request = FormRequest(
            url='http://tiaokuan.iachina.cn:8090/sinopipi/loginServlet/publicQueryResult.do',
            formdata=formdata,
            callback=self.parse_pages
            )
            request.meta['code'] = formdata["prodTermsShow.prodTypeCode"]
            yield request


    def parse_pages(self, response):
        pageCount = int(response.xpath('//input[@name="pageCount"]/@value').extract()[0])
        page = 1
        while (page < pageCount + 1):
            formdata = {
                "pageNo": str(page),
                "pageSize": "200",
                "prodTermsShow.prodTypeCode": response.meta['code'],
            }

            """网络请求"""
            yield FormRequest(
                url='http://tiaokuan.iachina.cn:8090/sinopipi/loginServlet/publicQueryResult.do',
                formdata=formdata,
                callback=self.parse_detaillist
            )
            page += 1

    def parse_detaillist(self, response):
        """保险列表"""
        x = response.xpath('//tr[@class="common1"]//input[@class="button"]/@onclick').extract()
        for url in x:
            y = url[9:].split(',')
            newurl = y[-1][1:-2] + y[0][1:-1] + '.html'
            yield Request(newurl, callback=self.parse_pdf)

    def parse_pdf(self, response):
        """下载pdf及命名"""
        link = response.xpath('//td[@align="right"]/a/@href').extract()[0]
        new = response.urljoin(link)
        pdf = response.xpath('//object/@data').extract()[0]
        pdf_url = response.urljoin(pdf)
        pdf_name =response.xpath('//table[@border="1"]/tr[2]/td/text()').extract()[0][:-1]
        row = hashlib.new("md5", pdf_name+pdf_url).hexdigest()
        fileName = settings.IACHINA_PATH + pdf_name+ row +'.pdf'
        content = settings.IACHINA_PATH + str(pdf_name)+ '^' + str(pdf_url)
        try:
            urllib.urlretrieve(pdf_url, fileName)
        except Exception:
            print "download error "
        request = Request(new, callback=self.parse_items)
        request.meta['content'] = content
        yield request

    def parse_items(self, response):
        item = RawInsuranceProduct()
        items = []
        item['url'] = response.url
        insurance_company = response.xpath('//table[@border="1"]/tr[1]/td[2]/text()').extract()[0]
        insurance_company = ''.join(insurance_company.split())
        item['insurance_company'] = insurance_company
        insurance_type = response.xpath('//table[@border="1"]/tr[3]/td[2]/text()').extract()[0]
        insurance_type = ''.join(insurance_type.split())
        item['insurance_type'] = insurance_type
        insurance_product = response.xpath('//table[@border="1"]/tr[2]/td[2]/text()').extract()[0]
        insurance_product = ''.join(insurance_product.split())
        item['insurance_product'] = insurance_product
        item['crawltime'] = datetime.datetime.now().strftime('%Y-%m-%d %H:%M')
        item['spider_name'] = self.name
        item['source_domain'] = 'iachina.cn^中国保险行业协会'
        keys = response.xpath('//table[@border="1"]//strong/text()').extract()
        values = response.xpath('//table[@border="1"]//td/text()').extract()
        new_value = []
        for val in values:
            val = ''.join(val.split())
            new_value.append(val)
        dict1 = dict(zip(keys, new_value))
        dict1['产品内容:'] = response.meta['content']
        item['product_content'] = json.dumps(dict1).decode('unicode-escape')
        items.append(item)
        return items
