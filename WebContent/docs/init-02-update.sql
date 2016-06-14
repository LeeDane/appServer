/*为博客增加是否推荐的列*/
alter table t_blog add COLUMN recommend BOOLEAN DEFAULT false;

/*为签到表添加唯一性约束*/
alter table t_sign_in add constraint sign_in_unique UNIQUE(create_user_id, create_date);

/*为聊天背景与用户关系表添加唯一性约束*/
alter table t_chat_bg_user add constraint chat_bg_user_unique UNIQUE(create_user_id, chat_bg_table_id);