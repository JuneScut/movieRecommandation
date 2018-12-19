import numpy as np
import random

import mysql.connector

config = {
    'user': 'root',
    'password': 'root0101',
    'host': 'localhost',
    'database': 'moviedb',
    'raise_on_warnings': True
}

cnx = mysql.connector.connect(**config)

def gen_rnd_rating_samples():
    '''
    Generate some random rating samples.
    '''
    cnx = mysql.connector.connect(**config)
    cursor = cnx.cursor()

    sql_get_max_user_id = "select max(`id`) from `user`;"
    sql_get_max_min_movie_id = "select max(`id`), min(`id`) from `movie`;"
    sql_insert_new_user = "insert into `user` values ();"
    sql_insert_rnd_rating = ('''
    insert into `rating` (`user_id`, `movie_id`, `score`) values (%s, %s, %s);
    ''')

    

    # (%(user_id)s, %(movie_id)s, %(score)s);
    
    cursor.execute(sql_get_max_user_id)
    for (user_id,) in cursor:
        max_user_id = user_id
    
    cursor.execute(sql_get_max_min_movie_id)
    for (id1, id2) in cursor:
        max_movie_id, min_movie_id = id1, id2

    sql_maximize_auto_increment_key = f"alter table `user` auto_increment = {max_user_id+1};"
    cursor.execute(sql_maximize_auto_increment_key)

    n_rnd_users = 50
    n_rnd_ratings_per_user = 20
    min_score, max_score = 1, 5

    for i in range(n_rnd_users):
        user_id = max_user_id + i
        cursor.execute(sql_insert_new_user)

        rated_movie_ids = set()

        for j in range(n_rnd_ratings_per_user):
            score = random.randint(min_score, max_score)
            movie_id = random.randint(min_movie_id, max_movie_id)
            while movie_id in rated_movie_ids:
                movie_id = random.randint(min_movie_id, max_movie_id)

            rating_tuple = (user_id, movie_id, score)
            cursor.execute(sql_insert_rnd_rating, rating_tuple)
            rated_movie_ids.add(movie_id)

        cnx.commit()

    cnx.commit()

gen_rnd_rating_samples()