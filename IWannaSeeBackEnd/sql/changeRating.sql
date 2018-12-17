use `moviedb`;
alter table `rating` drop column `rating`;
alter table `rating` drop column `comment`;

alter table `rating` add column (
	`score` numeric (1,0) default 3 comment '用户评分，取值为1-5',
	`comment` text comment '用户评论'
);