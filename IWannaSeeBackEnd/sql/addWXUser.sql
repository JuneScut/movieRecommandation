use `moviedb`;

alter table `user` drop column `password`;
alter table `user` drop column `username`;

delete from `user`;

create table if not exists `wx_mp_user` (
  `user_id` int(11) not null,
  `openid` varchar(99) not null comment '微信小程序openid，能够标识微信用户在[小程序平台]的唯一性',
  primary key (`openid`),
  unique key (`user_id`),
  foreign key (`user_id`) references `user`(`id`) on delete cascade
);
