drop database if exists moviedb;
create database if not exists moviedb character set utf8;
use moviedb;

drop table if exists `movie_rating`;
drop table if exists `movie_collection`;
drop table if exists `short_comment`;
drop table if exists `movie_category`;
drop table if exists `category`;
drop table if exists `movie`;

/* ------------------ user ------------------ */
/* user 具体字段根据微信小程序开发的支持和推荐算法需要具体再做更改 */

create table if not exists `user`(
  `id` int(11) not null auto_increment,
  `username` varchar(99),
  `password` varchar(99),
  /* `salt` varchar(512), */
  `gender` numeric(1, 0) default 0 comment '性别：0未知，1男，2女',
  `age` numeric(2, 0) default 0,
  primary key(`id`),
  unique key(`username`)
);

/* ------------------ movie ------------------ */

create table if not exists `movie`(
  `id` int(11) not null auto_increment,
  `title` varchar(99) not null comment '电影名称',
  `country` varchar(99),
  `year` int(4) unsigned,
  `directors` varchar(99),
  `stars` varchar(1024),
  `release_date` varchar(99) comment '上映日期',
  `running_time` varchar(99) comment '电影时长',
  `short_intro` varchar(4096) default '' comment '电影简介',
--   `short_intro` text comment '电影简介',
  `douban_rank` int(11) unsigned comment '豆瓣排名',
  `douban_score` numeric(3, 1) comment '豆瓣评分， 0-10分',
  `douban_votes` int(11) unsigned comment '豆瓣投票数',
  `douban_quote` varchar(256) comment '豆瓣格言',
  `douban_url` varchar(256) comment '豆瓣链接',
  `pic_url` varchar(256) comment '豆瓣剧照',
  primary key (`id`),
  unique key (`title`)
);

create table if not exists `category`(
  `id` int(11) not null auto_increment,
  `title` varchar(99) not null comment '类别名称',
  `alias` varchar(99) default '' comment '类别别名',
  `desc` varchar(1024) default '' comment '类别详情',
  primary key (`id`),
  unique key(`title`)
);

create table if not exists `movie_category`(
  `movie_id` int(11) not null,
  `category_id` int(11) not null,
  primary key(`movie_id`, `category_id`),
  foreign key(`movie_id`) references `movie`(`id`) on delete cascade,
  foreign key(`category_id`) references `category`(`id`) on delete cascade
);

create table if not exists `short_comment`(
   `id` int(11) not null auto_increment,
   `movie_id` int(11) not null,
   `content` varchar(4096) not null comment '评论内容',
--    `content` text not null comment '评论内容',
   `hash` varchar(256) not null comment '哈希值，用于校验评论内容是否重复',
   primary key(`id`),
   unique key(`movie_id`, `hash`),
   foreign key(`movie_id`) references `movie`(`id`) on delete cascade
);

/* ------------------ <user, movie> ------------------ */
/* 用户历史查看记录 */
create table if not exists `history`(
  `id` int(11) not null,
  `user_id` int(11) not null,
  `movie_id` int(11) not null,
  `date` varchar(99) not null,
  primary key(`id`),
  foreign key(`user_id`) references `user`(`id`) on delete cascade,
  foreign key(`movie_id`) references `movie`(`id`) on delete cascade
);

/* 用户喜欢的类别 */
create table if not exists `favor_category`(
  `user_id` int(11) not null,
  `category_id` int(11) not null,
  primary key(`user_id`, `category_id`),
  foreign key(`user_id`) references `user`(`id`) on delete cascade,
  foreign key(`category_id`) references `category`(`id`) on delete cascade
);

/* 用户表示自己看过的电影 */
create table if not exists `watches`(
  `user_id` int(11) not null,
  `movie_id` int(11) not null,
  primary key(`user_id`, `movie_id`),
  foreign key(`user_id`) references `user`(`id`) on delete cascade,
  foreign key(`movie_id`) references `movie`(`id`) on delete cascade
);

/* 用户收藏列表 */
create table if not exists `collection` (
  `id` int(11) not null auto_increment,
  `user_id` int(11) not null,
  `movie_id` int(11) not null,
  primary key(`id`),
  unique key (`user_id`, `movie_id`),
  foreign key(`user_id`) references `user`(`id`) on delete cascade,
  foreign key(`movie_id`) references `movie`(`id`) on delete cascade
);

/* 用户评分+评论 */
create table if not exists `rating` (
  `user_id` int(11) not null,
  `movie_id` int(11) not null,
  `rating` numeric(1,0) default 3,
  `comment` text,
  primary key(`user_id`, `movie_id`),
  foreign key(`user_id`) references `user`(`id`) on delete cascade,
  foreign key(`movie_id`) references `movie`(`id`) on delete cascade
);

/* ------------ view ----------------- */
create view `movie_categories_v` as
select `movie_id`, group_concat(C.title separator ' ') as category
from `movie_category` as MC inner join `category` as C on MC.category_id = C.id
group by `movie_id`;
