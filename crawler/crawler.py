# coding=utf-8

import re
import requests
import csv
import pymysql
import lxml.html

DOWNLOAD_URL = 'http://movie.douban.com/top250/'

def download_page(url): # 下载页面
    return requests.get(url, headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0'}).text

def get_detail(url):
    tar = requests.get(url,headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0'}).text
    tar_html = lxml.html.fromstring(tar)
    result = list()
    #导演
    Directors = tar_html.xpath("//a[@rel='v:directedBy']/text()")
    result.append(Directors)
    #演员
    Starring = tar_html.xpath("//a[@rel='v:starring']/text()")
    result.append(Starring)
    #上映日期
    Date = tar_html.xpath("//span[@property='v:initialReleaseDate']/@content")
    result.append(Date)
    #片长
    length = tar_html.xpath("//span[@property='v:runtime']/text()")[0]
    result.append(length)
    #短评
    short_comment = tar_html.xpath("//span[@class='short']/text()")
    result.append(short_comment)
    #简介
    story = tar_html.xpath("//span[@property='v:summary']/text()")
    result.append(story)
    return result

def parse_html(html, writer, movies_info):  # 使用lxml爬取数据并清洗
    tree = lxml.html.fromstring(html)

    movies = tree.xpath("//ol[@class='grid_view']/li")
    i = 0
    for movie in movies:

        #获取其电影的独自链接,获取导演，主演，上映日期，片长，短评,简介
        own_url = movie.xpath("//div[@class='hd']/a/@href")[i]
        Directors, Stars, Date, Length, ShortComment, story = get_detail(own_url)

        name_num = len(movie.xpath("descendant::span[@class='title']"))
        name = ''
        for num in range(0, name_num):
            name += movie.xpath("descendant::span[@class='title']")[num].text.strip()
        name = ' '.join(name.replace('/', '').split())  # 清洗数据
        # 排名，豆瓣页面
        num = movie.xpath("descendant::em/text()")[0]
        # < class 'lxml.etree._ElementUnicodeResult'>
        url = movie.xpath("descendant::div[@class='pic']/a/@href")[0]
        # 年份，国家，类型
        data = movie.xpath("descendant::div[@class='bd']/p")[0].xpath('string(.)')
        info = re.findall(r'\d.*', data)[0].split('/')
        year, country, category = info[0].strip(), info[1].strip(), info[2].strip()
        # 得分，投票数
        score = movie.xpath("descendant::div[@class='star']/span")[1].text
        voting_num = movie.xpath("descendant::div[@class='star']/span")[3].text

        #海报url 
        image = movie.xpath("//img/@src")
        #print(image)
        if image:
            pic_url = image[i]

        #quote 
        quote_temp = movie.xpath("//p[@class='quote']/span")[i].text
        if quote_temp:
            quote = quote_temp

        movie_info = (int(num), name, float(score), country, str(year), category, int(voting_num[0:-3]), url,str(pic_url),str(quote),str(Directors),str(Stars),str(Date),str(Length),str(ShortComment),str(story))
        movies_info.append(movie_info)
        #print(movie_info)
        writer.writerow(movie_info)
        i += 1
    try:
        next_page = tree.xpath("//span[@class='next']/a/@href")[0]
        return DOWNLOAD_URL + next_page
    except:
        return None


def main():
    url = DOWNLOAD_URL
    # 将数据导入到csv文件中
    writer = csv.writer(open('movies.csv', 'w', newline='', encoding='utf-8'))
    fields = ('rank',  'name', 'score', 'country', 'year', 'category', 'votes', 'douban_url','pic_url','quote','Directors','Stars','Date','Length','ShortComment','short_introduction')
    writer.writerow(fields)
    movies_info = []
    while url:
        html = download_page(url)
        url = parse_html(html, writer, movies_info)

if __name__ == '__main__':
    main()