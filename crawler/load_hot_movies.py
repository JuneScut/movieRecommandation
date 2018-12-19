'''
'''

from string import Template
import sys
import time
import pandas as pd
import re
from bs4 import BeautifulSoup
import requests

import pandas as pd
import mysql.connector

import pickle

crawler_ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36"
crawler_headers = {"user-agent": crawler_ua}

def down_html_sample(url, filename="sample.html"):
     r = requests.get(url, headers=crawler_headers)
     with open(__file__ + '/../html_samples/' + filename, 'w', encoding=r.encoding) as html_file:
         html_file.write(r.text)


# 下载html源码，分析要拿的数据
local_hot_movies_url = 'https://movie.douban.com/cinema/nowplaying/guangzhou/'
movie_subject_url = 'https://movie.douban.com/subject/26219713'
# down_html_sample(local_hot_movies_url, 'local_hot_movies.html')
# down_html_sample(movie_subject_url, 'movie_subject.html')





# 定义 Movie 数据结构
# 改造原来的 Top250Movie 数据结构
class Movie:
    # title
    # country
    # year
    # directors
    # stars
    # release_date
    # running_time
    # short_intro
    # douban_score # 有些电影投票数不够，显示为0
    # douban_votes # 有些电影投票数不够，显示为0
    # douban_url
    # pic_url
    # category

    # douban_rank # 只有 douban Top 250 有排名属性 xx
    # douban_quote # 只有 IMDB / douban Top 250 有格言 xx

    def __init__(self):
        self.title = ""
        self.year = None

        self.directors = ""
        self.stars = ""
        self.categories = []
        self.country = ""
        self.release_date = ""
        self.running_time = ""

        self.douban_score = None
        self.douban_votes = None
        self.douban_url = ""
        self.pic_url = ""

        self.short_intro = ""
        self.short_comments = []
    
    # def __init__(self, movieDict):
    #     self.title = str(movieDict['title']) #.replace("\'", "\\\'").replace("\"", "\\\"")
    #     self.douban_score = float(movieDict['douban_score'])
    #     self.country = movieDict['country']
    #     self.year = int(movieDict['year'])
    #     self.douban_votes = int(movieDict['douban_votes'])
    #     self.douban_url = movieDict['douban_url']
    #     self.pic_url = movieDict['pic_url']

    #     self.directors = str(movieDict['directors']).strip("\'[] ").replace("', '", ', ')
    #     self.stars = str(movieDict['stars']).strip("\'[] ").replace("', '", ', ')

    #     self.release_date = str(movieDict['release_date']).strip("\'[] ").replace("', '", ', ')
    #     self.running_time = movieDict['running_time']

    #     self.categories = movieDict['category'].split()
    #     for category in self.categories:
    #         if category and category not in category_set: # 测试category非空
    #             category_set.add(category)

    #     self.short_intro = self.filter_short_intro(movieDict['short_intro'])
    #     self.short_comments = self.filter_short_comments(movieDict['short_comments'])

    def get_info_dict(self):
        result = dict()
        result["title"] = self.title
        result["douban_score"] = self.douban_score
        result["country"] = self.country
        result["year"] = self.year
        result["douban_votes"] = self.douban_votes
        result["douban_url"] = self.douban_url
        result["pic_url"] = self.pic_url
        result["directors"] = self.directors
        result["stars"] = self.stars
        result["release_date"] = self.release_date
        result["running_time"] = self.running_time
        result['short_intro'] = self.short_intro
        return result

def concat_str_in_soups(soup_list):
    result_str = ""
    for soup in soup_list:
        result_str += str(soup.string)
        result_str += ", "

    return result_str.strip(", ")

def substring_after_char(original_str, target_char):
    if original_str.find(target_char):
        return original_str[original_str.find(target_char)+1:]

def load_a_hot_movie(movie_subject_url):
    r = requests.get(movie_subject_url, headers=crawler_headers)
    soup = BeautifulSoup(r.text, 'lxml')

    movie = Movie()
    movie.douban_url = movie_subject_url
    movie.pic_url = soup.find('img', attrs={'rel': 'v:image'})['src']

    # movie.douban_score = float(str(soup.select('strong[property="v:average"]').string))
    score_soup = soup.find('strong', attrs={'class':'ll rating_num','property':"v:average"})
    if score_soup is None or score_soup.string is None:
        movie.douban_score = 0 
    else: # 暂无评分
        movie.douban_score = float(str(score_soup.string))
    
    vote_soup = soup.find('span', attrs={'property':"v:votes"})
    if vote_soup is None or vote_soup.string is None:
        movie.douban_votes = 0
    else:
        movie.douban_votes = int(str(vote_soup.string))

    movie.title = str((soup.select('span[property="v:itemreviewed"]')[0]).string).strip()
    movie.year = int(str((soup.select('span[class="year"]')[0]).string).strip('()'))

    info_soup = soup.select('div[id="info"]')[0]

    directors_a = info_soup.select('a[rel="v:directedBy"]')
    movie.directors = concat_str_in_soups(directors_a)

    stars_span = info_soup.find('span', class_='actor')
    if stars_span is not None: # 原来特么一部电影可以缺少演员的数据
        movie.stars = substring_after_char(str(stars_span.text), ":").replace(" /", ",").strip()

    
    for category_span in info_soup.select('span[property="v:genre"]'):
        movie.categories.append(str(category_span.string).strip())

    movie.country = str(info_soup.find('span', string='制片国家/地区:').next_sibling).replace(" /", ",").strip()
    movie.release_date = concat_str_in_soups(info_soup.select('span[property="v:initialReleaseDate"]')).replace(" /", ",")
    movie.running_time = str(info_soup.find('span', attrs={'property': 'v:runtime'}).string).strip()


    # movie.short_intro = str(soup.find('span', attrs={'property': 'v:summary'}).text).strip()
    intro_span = soup.find('span', attrs={'property': 'v:summary'})
    for element in intro_span.contents:
        movie.short_intro += str(element).strip()

    def is_comment_div(element):
        return element.name == 'div' and element.get('class') == ['comment-item'] and element.has_attr('data-cid')

    def handle_short_comment(comment_item_div):
        if comment_item_div.find('span', class_='hide-item full'):
            return str(comment_item_div.find('span', class_='hide-item full').string).strip()
        return str(comment_item_div.find('span', class_='short').string).strip()

    comment_item_divs = soup.find_all(is_comment_div)
    for comment_item_div in comment_item_divs:
        movie.short_comments.append(handle_short_comment(comment_item_div))

    # print(movie.short_intro)
    # print(movie.get_info_dict())
    return movie


def crawler_hot_movies():
    r = requests.get(local_hot_movies_url, headers=crawler_headers)
    soup = BeautifulSoup(r.text, 'lxml')

    # hot_movie_lis = soup.find_all("li", data-category="nowplaying")
    hot_movie_lis = soup.select('li[data-category="nowplaying"]')

    hot_movie_subject_urls = []
    for hot_movie_li in hot_movie_lis:
        subject_link_a = hot_movie_li.find("a", "ticket-btn")
        hot_movie_subject_urls.append(str(subject_link_a['href']).replace("?from=playing_poster", ""))

    hot_movies = []
    for i, hot_movie_subject_url in enumerate(hot_movie_subject_urls):
        try:
            hot_movies.append(load_a_hot_movie(hot_movie_subject_url))
        except Exception as ex:
            print(ex)
            continue

        time.sleep(5)
        print(hot_movies[i].title)

    return hot_movies

def load_movies_into_db(movies):
    
    import mysql.connector

    config = {
        'user': 'root',
        'password': 'root',
        'host': 'localhost',
        'database': 'moviedb',
        'raise_on_warnings': True
    }

    cnx = mysql.connector.connect(**config)
    cursor = cnx.cursor()

    # --- 防止插入重复值
    original_movies = movies
    movies = []
    sql_read_movie_titles = 'select `title` from `movie`;'
    title_df = pd.read_sql_query(sql_read_movie_titles, con=cnx)
    title_set = set(list(title_df['title']))
    for movie in original_movies:
        if movie.title not in title_set:
            movies.append(movie)

    # ------- insert into `movie` table ----------------
    sql_select_movie_id = '''SELECT `id` AS `movie_id` FROM `movie` WHERE `title` = \'{}\';'''

    sql_insert_movie = ('''INSERT INTO `movie` 
            (`title`, `douban_score`,`country`,`year`,`douban_votes`,`douban_url`, `pic_url`, `directors`,`stars`,`release_date`,`running_time`, `short_intro`)
            VALUES (%(title)s, %(douban_score)s, %(country)s, %(year)s, %(douban_votes)s, %(douban_url)s, %(pic_url)s, %(directors)s, %(stars)s, %(release_date)s, %(running_time)s, %(short_intro)s)''')

    for movie in movies:
        try:
            cursor.execute(sql_insert_movie, movie.get_info_dict())
        except Exception as ex:
            print(ex)

    # connection is not autocommit by default. So you must commit to save your changes.
    cnx.commit()

    # ------- category ----------------
    # TODO: 从数据库中读数据
    category_set = set()
    for movie in movies:
        for category in movie.categories:
            category_set.add(category)

    sql_insert_category = "INSERT INTO `category` (`title`) VALUES (\'{}\')"
    for category in category_set:
        try:
            cursor.execute(sql_insert_category.format(category))
            # cursor.execute(insert_category_sql, category)
        except Exception as ex:
            print(ex)
    cnx.commit()

    # ------- short_comment && movie_category ----------------

    sql_select_category_id = "SELECT `id` FROM `category` where `title` = \'{}\'"
    sql_insert_moive_category = "INSERT INTO `movie_category` (`movie_id`, `category_id`) VALUES ({}, {})"
    sql_insert_short_comment = ('''INSERT INTO `short_comment` (`movie_id`, `content`, `hash`) values (%s, %s, %s)''')

    import hashlib

    for movie in movies:
        try:
            title = movie.title
            cursor.execute(sql_select_movie_id.format(title))
            for (id,) in cursor:
                movie_id = id
            
            for category in movie.categories:
                cursor.execute(sql_select_category_id.format(category))
                for (id,) in cursor:
                    category_id = id
                cursor.execute(sql_insert_moive_category.format(movie_id, category_id))
            cnx.commit()
            
            for comment in movie.short_comments:
                md = hashlib.md5()
                md.update(comment.encode(encoding='UTF-8'))
                comment_hash = md.hexdigest()
                data_comment = (movie_id, comment, comment_hash)
                cursor.execute(sql_insert_short_comment, data_comment)
                cnx.commit()
        except Exception as ex:
            print(ex)
            print('movie', movie.title)
            print('comment: ',comment)

    cnx.close()
    print("Done!")

if __name__ == "__main__":
    # null_score_url = 'https://movie.douban.com/subject/30258232/'
    # load_a_hot_movie(null_score_url)

    movies = crawler_hot_movies()

    dump_filename = 'movies.dat'
    with open(dump_filename, "wb") as f:
        pickle.dump(movies, f)

    with open(dump_filename, "rb") as f:
        movies = pickle.load(f)
    load_movies_into_db(movies)

# for category in self.categories:
        #     if category and category not in category_set: # 测试category非空
        #         category_set.add(category)













