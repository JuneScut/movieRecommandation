from flask import Flask, request

from user_knn import user_knn_recommend

app = Flask(__name__)


@app.route('/users/<int:user_id>/recommender/user-knn', methods=['GET'])
def user_knn_controller(user_id):

    k_neighbors = int(request.args.get('k_neighbors'))
    if k_neighbors == None:
        k_neighbors = 5

    k_movies = int(request.args.get('k_movies'))
    if k_movies == None:
        k_movies = 10

    return user_knn_recommend(user_id, k_neighbors, k_movies).get_response()
