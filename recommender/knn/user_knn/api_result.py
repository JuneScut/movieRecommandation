from flask import json, jsonify
from flask import make_response

# 统一返回数据格式
class Result:
    def __init__(self, status, msg, data=None):
        self.status = status
        self.msg = msg
        self.data = data

    def to_dict(self):
        result_dict = {
            'status': self.status,
            'msg': self.msg,
            'data': self.data
        }
        return result_dict

    def to_json(self):
        return json.dumps(self.to_dict())
        # return jsonify(status=self.status, msg=self.msg, data=self.data)

    def get_response(self):
        resp = make_response()
        resp.headers['Content-Type'] = 'application/json;charset=UTF-8'
        resp.response = self.to_json()
        return resp

    @staticmethod
    def bad_request(msg='Bad Request', data=None):
        return Result(400, msg, data)

    @staticmethod
    def ok(msg='ok', data=None):
        return Result(200, msg, data)
