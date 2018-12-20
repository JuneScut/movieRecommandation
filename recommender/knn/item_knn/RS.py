# coding=utf-8

import numpy  as np
from flask import Flask,json,jsonify,request 
import threading 
import math
import logging

import pandas as pd
import mysql.connector

app = Flask(__name__)

# def Cal_Similiar_Movies(user_id,k):
#     # db = pymysql.connect(host = 'localhost',user = 'root',password = 'root',db = 'moviedb', charset = 'utf8mb4',cursorclass = pymysql.cursors.DictCursor)
#     db = pymysql.connect(host = 'localhost', port=3306, user = 'root',password = 'root',db = 'moviedb', charset = 'utf8mb4',cursorclass = pymysql.cursors.DictCursor)
#     cursor = db.cursor()
    
#     #先获取用户的爱好类别并生成爱好向量
#     get_user_feat = 'select * from favor_category where user_id = %s'%(user_id)
#     cursor.execute(get_user_feat)
#     user_cate_dict = cursor.fetchall()
#     user_vec = np.zeros(27)
#     for per_dict in user_cate_dict:
#         cate_index = per_dict['category_id']
#         user_vec[int(cate_index) - 1] = 5
    
#     #再获取电影的特征类别向量
#     get_movie_feats = 'select * from movie_category'
#     cursor.execute(get_movie_feats)
#     movies_feat_dict = cursor.fetchall()
#     movies_vec = np.zeros((250,27))
#     for per_movie_feat_dict in movies_feat_dict:
#         movie_index = per_movie_feat_dict['movie_id']
#         cate_index = per_movie_feat_dict['category_id']
#         movies_vec[movie_index-1,cate_index-1] = 5
    
#     #最后进行逐个对比计算，从而选出前k个距离最小的id并返回
#     dists_dict = {}
#     j = 0
#     for movie_vec in movies_vec:
#         dist = 0
#         for i in range(len(movie_vec)):
#             dist += math.sqrt(pow(movie_vec[i] - user_vec[i],2))
#         dists_dict[j] = dist
#         j += 1
    
#     sorted_dists_list = sorted(dists_dict.items(),key = lambda x: x[1])
#     movie_result = [x[0] for x in sorted_dists_list[:k]]
#     cursor.close()
#     db.close()
#     return movie_result

def Cal_Similiar_Movies(user_id,k):
    
    config = {
        'user': 'root',
        'password': 'root0101',
        'host': 'localhost',
        'database': 'moviedb',
        'raise_on_warnings': True
    }

    cnx = mysql.connector.connect(**config)
    
    cursor = cnx.cursor()

    sql_get_movies_cnt = "select count(*) from `movie`;"
    sql_get_category_cnt = "select count(*) from `category`;"
    
    cursor.execute(sql_get_movies_cnt)
    for (cnt,) in cursor:
        total_movies_cnt = cnt
    
    cursor.execute(sql_get_category_cnt)
    for (cnt, ) in cursor:
        total_categories_cnt = cnt

    #先获取用户的爱好类别并生成爱好向量
    get_user_feat = 'select * from favor_category where user_id = %s'%(user_id)
    user_category_df = pd.read_sql_query(get_user_feat, con=cnx)

    user_vec = np.zeros(27)
    for user_id, category_id in zip(user_category_df['user_id'], user_category_df['category_id']):
        user_vec[category_id] = 5

    # for per_dict in user_cate_dict:
    #     cate_index = per_dict['category_id']
    #     user_vec[int(cate_index) - 1] = 5
    
    #再获取电影的特征类别向量
    get_movie_feats = 'select * from movie_category'
    movie_category_df = pd.read_sql_query(get_movie_feats, con=cnx)
    
    cnx.close()
    
    # movies_vec = np.zeros((250,27))
    movies_vec = np.zeros((total_movies_cnt,total_categories_cnt))
    for movie_id, category_id in zip(movie_category_df['movie_id'], movie_category_df['category_id']):
        movies_vec[movie_id-1, category_id-1] = 5

    # for per_movie_feat_dict in movies_feat_dict:
    #     movie_index = per_movie_feat_dict['movie_id']
    #     cate_index = per_movie_feat_dict['category_id']
    #     movies_vec[movie_index-1,cate_index-1] = 5
    
    #最后进行逐个对比计算，从而选出前k个距离最小的id并返回
    dists_dict = {}
    j = 0
    for movie_vec in movies_vec:
        dist = 0
        for i in range(len(movie_vec)):
            dist += math.sqrt(pow(movie_vec[i] - user_vec[i],2))
        dists_dict[j] = dist
        j += 1
    
    sorted_dists_list = sorted(dists_dict.items(),key = lambda x: x[1])
    movie_result = [x[0] for x in sorted_dists_list[:k]]
    return movie_result

# @app.route('/users/recommend',methods = ['POST','GET'])
@app.route('/users/<int:user_id>/recommender/category-based', methods = ['GET'])
def recommend(user_id):
    try:
        # a = request.get_data()
        # data = json.loads(a.decode('utf-8'))
        k_movies = int(request.args.get('k_movies'))
        result = {}
        result['data'] = {'recommended_movie_ids': Cal_Similiar_Movies(user_id, k_movies)}
        # result['error'] = 0
        result['status'] = 200
        result['msg'] = 'ok'
        return jsonify(result)
    except Exception as e:
        result = {}
        result['error'] = str(e)
        return jsonify(result)

def run_server():
    handler = logging.FileHandler('RS_Test.log')
    app.logger.addHandler(handler)
    app.run(debug = False, host = '0.0.0.0', port = 7999)

if __name__ == '__main__':
    # Cal_Similiar_Movies(30, 5)
    t1 = threading.Thread(target = run_server)
    t1.start()





