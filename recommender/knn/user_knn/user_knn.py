# -*- coding: utf-8 -*-
# @author: fakeProgrammer0

import mysql.connector
import pandas as pd
import numpy as np

from api_result import Result

from queue import PriorityQueue
from sklearn.neighbors import NearestNeighbors


def user_knn_recommend(target_user_id,
                       k_neighbors=5,
                       k_movies=10,
                       min_rating_count=5):
    '''
    A user-knn recommendation algorithm using cosine similarity
    Read data from the underlying database `moviedb` and perform recommender computation.

    Parameters
    ----------
    target_user_id : int
        The user_id of target recommended user

    k_neighbors : int
        The number of neighbors used in knn algorithm

    k_movies : int
        The number of recommended movies 

    Return
    ------
    result : api_result.Result
        The process result containing recommended movie_ids in the data field

    '''

    config = {
        'user': 'root',
        'password': 'root',
        'host': 'localhost',
        'database': 'moviedb',
        'raise_on_warnings': True
    }

    cnx = mysql.connector.connect(**config)

    # 判断target_user_id是否存在

    sql_read_target_user = f'select * from `user` where `id` = {target_user_id}'
    target_user_df = pd.read_sql_query(sql_read_target_user, con=cnx)
    if len(target_user_df) != 1:
        return Result.bad_request(msg='用户不存在')

    sql_query_target_user_rating_count = f'select count(*) as `rating_count` from `rating` where `user_id` = {target_user_id}'
    rating_count_df = pd.read_sql_query(
        sql_query_target_user_rating_count, con=cnx)
    if rating_count_df['rating_count'][0] < min_rating_count:
        return Result.bad_request(msg='用户评分次数太少')

    # 为了性能考虑，暂时不读取整个User-Movie矩阵
    # 只读取有评分的user_id和有被评分的movie_id，再作映射
    # 毕竟要推荐的内容发生在有评分的地方

    sql_read_rating_pairs = 'select `user_id`, `movie_id`, `score` from `rating`;'
    rating_df = pd.read_sql_query(sql_read_rating_pairs, con=cnx)
    cnx.close()
    # print(rating_df)

    # 对user_id和movie_id进行映射处理
    user_id_list = sorted(list(set(rating_df['user_id'])))
    n_users = len(user_id_list)
    true_user_id2user_id = dict(zip(user_id_list, range(n_users)))
    user_id2_true_user_id = dict(zip(range(n_users), user_id_list))
    rating_df['user_id'] = rating_df['user_id'].map(true_user_id2user_id)

    movie_id_list = sorted(list(set(rating_df['movie_id'])))
    n_movies = len(movie_id_list)
    true_movie_id2movie_id = dict(
        zip(movie_id_list, range(len(movie_id_list))))
    movie_id2_true_movie_id = dict(
        zip(range(len(movie_id_list)), movie_id_list))
    rating_df['movie_id'] = rating_df['movie_id'].map(true_movie_id2movie_id)

    # print(rating_df)

    # 构建Rating矩阵
    R = np.zeros((n_users, n_movies))
    for user_id, movie_id, score in zip(
            rating_df['user_id'], rating_df['movie_id'], rating_df['score']):
        R[user_id, movie_id] = score

    # print(R)

    # 如果数据量太小，改变 k_neighbor 的值
    if n_users < k_neighbors:
        k_neighbors = n_users

    recommended_user_id = true_user_id2user_id[target_user_id]
    X = R[recommended_user_id:recommended_user_id + 1, :]

    # 有很多0值，采用相似性度量为：
    # 暂时采用余弦相似性
    # 因为数据包含user自身，所以 k_neighbors 要加1
    nbrs = NearestNeighbors(n_neighbors=k_neighbors + 1, metric='cosine')  
    nbrs.fit(R)
    distances, indice = nbrs.kneighbors(X)

    distances = distances.flatten()
    indice = indice.flatten()

    # print(indice)
    # print(distances)

    R_neighbor = None
    for idx in indice:
        if R_neighbor is not None:
            R_neighbor = np.vstack((R_neighbor, R[idx:idx + 1, :]))
        else:
            R_neighbor = R[idx:idx + 1, :]

    # 电影被评分次数
    movie_rating_count = (R_neighbor != 0).sum(axis=0)

    # (-score, -count, distance, movie_id）
    # score 转化为负数，这样的话，score越小，优先级越高
    # 不过这样设计会带来的一个问题是，movie_id越小的，元组的优先级越高

    queue = PriorityQueue()

    for nbr_idx, dist in zip(indice, distances):
        if nbr_idx == recommended_user_id:
            continue
        for movie_id in range(n_movies):
            if R[recommended_user_id, movie_id] == 0:  # 被推荐用户没有评分过的电影
                score = R[nbr_idx, movie_id]
                if score:
                    queue.put((-score, -movie_rating_count[movie_id], dist,
                               movie_id))

    recommended_movie_ids = []
    for i in range(k_movies):
        # recommended_movie_ids.append(queue.get()[2])
        score, count, dist, movie_id = queue.get()
        # print('score:%d;count:%d;dist:%.2f;movie_id:%d' % (-score, -count,
        #                                                    dist, movie_id))
        if R[recommended_user_id, movie_id] == 0 and movie_id not in recommended_movie_ids:
            recommended_movie_ids.append(movie_id)

    # print(recommended_movie_ids)

    # 还原movie_id，得到真实的movie_id
    true_recommender_movie_ids = []
    for movie_id in recommended_movie_ids:
        true_recommender_movie_ids.append(movie_id2_true_movie_id[movie_id])

    # print(true_recommender_movie_ids)
    data = {'recommended_movie_ids': true_recommender_movie_ids}
    return Result.ok(data=data)


from datetime import datetime

if __name__ == '__main__':
    now = datetime.today()
    print(now)
    print(user_knn_recommend(30, 5, 10).to_dict())
    print('time consume:', datetime.today() - now)
