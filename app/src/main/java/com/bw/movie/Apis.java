package com.bw.movie;

public class Apis {
    /**
     * 没有网络的提示文字
     */
    public static final String NOTNETWORK="当前网络不可用，请检查网络状态";
    /**
     * 1.注册
     * 接口地址：http://172.17.8.100/movieApi/user/v1/registerUser
     * 请求方式:POST
     * */
    public static final String URL_REGISTER_USER_POST = "user/v1/registerUser";
    /**
     * 2.登陆
     * 接口地址：http://172.17.8.100/movieApi/user/v1/login
     * 请求方式:POST
     * */
    public static final String URL_LOGIN_POST = "user/v1/login";
    /**
     * 3.查询会员首页信息
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/findUserHomeInfo
     * 请求方式：GET
     * */
    public static final String URL_FIND_USER_HOME_INFO_GET = "user/v1/verify/findUserHomeInfo";
    /**
     * 4.修改用户信息
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/modifyUserInfo
     * 请求方式:POST
     * */
    public static final String URL_MODIFY_USER_INFO_POST = "user/v1/verify/modifyUserInfo";
    /**
     * 5.上传用户头像
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/uploadHeadPic
     * 请求方式:POST
     * */
    public static final String URL_UPLOAD_HEADPIC_POST="user/v1/verify/uploadHeadPic";
    /**
     * 6.修改密码
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/modifyUserPwd
     * 请求方式:POST
     * */
    public static final String URL_MODIFY_USER_PWD_POST = "user/v1/verify/modifyUserPwd";
    /**
     * 7.根据用户ID查询用户信息
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/getUserInfoByUserId
     * 请求方式:GET
     * */
    public static final String URL_GET_USER_INFO_BY_USERID_GET = "user/v1/verify/getUserInfoByUserId";
    /**
     * 8.用户签到
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/userSignIn
     * 请求方式:GET
     * */
    public static final String URL_USER_SIGN_IN_GET = "user/v1/verify/userSignIn";
    /**
     * 9.用户购票记录查询列表
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/findUserBuyTicketRecordList
     * 请求方式：GET
     * */
    public static final String URL_FIND_USER_BUY_TICLET_RECORD_LIST_GET = "user/v1/verify/findUserBuyTicketRecordList?status=%d&page=%d&count=10";
    /**
     * 10.微信登陆
     * 接口地址：http://172.17.8.100/movieApi/user/v1/weChatBindingLogin
     * 请求方式：POST
     * */
    public static final String URL_WE_CHAT_BINDING_LOGIN_POST = "user/v1/weChatBindingLogin";
    /**
     * 11.绑定微信账号
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/bindWeChat
     * 请求方式：POST
     * */
    public static final String URL_BIND_WE_CHAT_POST = "user/v1/verify/bindWeChat";
    /**
     * 12.是否绑定微信账号
     * 接口地址：http://172.17.8.100/movieApi/user/v1/verify/whetherToBindWeChat
     * 请求方式：GET
     * */
    public static final String URL_WHETHER_TO_BIND_WE_CHAT_GET = "user/v1/verify/whetherToBindWeChat";

    /**
     * 1查询热门电影列表
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findHotMovieList
     * 请求方式：GET
     * */
    public static final String URL_FIND_HOT_MOVIE_LIST_GET="movie/v1/findHotMovieList?page=%d&count=10";
    /**
     * 2查询正在上映电影列表
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findReleaseMovieList
     * 请求方式：GET
     * */
    public static final String URL_FIND_RELEASE_MOVIE_LIST_GET = "movie/v1/findReleaseMovieList?page=%d&count=10";
    /**
     * 3查询即将上映电影列表
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findComingSoonMovieList
     * 请求方式：GET
     * */
    public static final String URL_FIND_COMING_SOON_MOVIE_LIST_GET = "movie/v1/findComingSoonMovieList?page=%d&count=10";
    /**
     * 4根据电影ID查询电影信息
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findMoviesById
     * 请求方式：GET
     * */
    public static final String URL_FIND_MOVIE_BY_ID_GET= "movie/v1/findMoviesById?movieId=%d";
    /**
     * 5查看电影详情
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findMoviesDetail
     * 请求方式：GET
     * */
    public static final String URL_FIND_MOVIE_DETAIL_GET = "movie/v1/findMoviesDetail?movieId=%d";
    /**
     * 6.查询用户关注的影片列表
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/verify/findMoviePageList
     * 请求方式：GET
     * */
    public static final String URL_FIND_MOVIE_PAGE_LIST_GET = "movie/v1/verify/findMoviePageList?page=%d&count=10";
    /**
     * 7.关注电影
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/verify/followMovie
     * 请求方式：GET
     * */
    public static final String URL_FOLLOW_MOVIE_GET = "movie/v1/verify/followMovie?movieId=%d";
    /**
     * 8.取消关注电影
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/verify/cancelFollowMovie
     * 请求方式：GET
     * */
    public static final String URL_CANCEL_FOLLOW_MOVIE_GET = "movie/v1/verify/cancelFollowMovie?movieId=%d";
    /**
     * 9.查询影片评论
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findAllMovieComment
     * 请求方式：GET
     * */
    public static final String URL_FIND_MOVIE_COMMENT_GET = "movie/v1/findAllMovieComment?movieId=%d&page=%d&count=%d";
    /**
     * 10.添加用户对影片的评论
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/verify/movieComment
     * 请求方式：POST
     * */
    public static final String URL_MOVIE_COMMENT_POST = "movie/v1/verify/movieComment";
    /**
     * 11.查询影片评论回复
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findCommentReply
     * 请求方式：GET
     * */
    public static final String URL_FIND_COMMENT_REPLY_GET ="movie/v1/findCommentReply?commentId=%d&page=%d&count=%d";
    /**
     * 12.添加用户对评论的回复
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/verify/commentReply
     * 请求方式：POST
     * */
    public static final String URL_COMMENT_REPLY_POST = "movie/v1/verify/commentReply";
    /**
     * 13.电影评论点赞
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/verify/movieCommentGreat
     * 请求方式：POST
     * */
    public static final String URL_MOVIE_COMMENT_GREAT_POST = "movie/v1/verify/movieCommentGreat";
    /**
     * 14.根据影院ID查询该影院当前排期的电影列表
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findMovieListByCinemaId
     * 请求方式：GET
     * */
    public static final String URL_FIND_MOVIE_LIST_BY_CINEMAID_GET ="movie/v1/findMovieListByCinemaId?cinemaId=%d";
    /**
     * 15.根据电影ID和影院ID查询电影排期列表
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findMovieScheduleList
     * 请求方式：GET
     * */
    public static final String URL_FIND_MOVIE_SCHEDULE_LIST ="movie/v1/findMovieScheduleList?cinemasId=%d&movieId=%d";
    /**
     * 16.根据电影ID查询当前排片该电影的影院列表
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findCinemasListByMovieId
     * 请求方式：GET
     * */
    public static final String URL_FIND_CINEMAS_LIST_BY_MOVIE_ID_GET = "movie/v1/findCinemasListByMovieId?movieId=%d";
    /**
     * 17.购票下单
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/verify/buyMovieTicket
     * 请求方式：POST
     * */
    public static final String URL_BUY_MOVIE_TICKET_POST = "movie/v1/verify/buyMovieTicket";
    /**
     * 18.支付
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/verify/pay
     * 请求方式：POST
     * */
    public static final String URL_PAY_POST = "movie/v1/verify/pay";
    /**
     * 19.根据影院ID查询该影院下即将上映的电影列表
     * 接口地址：http://172.17.8.100/movieApi/movie/v1/findSoonMovieByCinemaId
     * 请求方式：GET
     * */
    public static final String URL_FIND_SOON_MOVIEW_BY_CINEMAID_GET = "movie/v1/findSoonMovieByCinemaId?cinemaId=%d";

    /**
     * 1.查询推荐影院信息
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/findRecommendCinemas
     * 请求方式：GET
     * */
    public static final String URL_FIND_RECOMMEND_CINEMAS_GET = "cinema/v1/findRecommendCinemas?page=%d&count=10";
    /**
     * 2.查询附近影院
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/findNearbyCinemas
     * 请求方式：GET
     * */
    public static final String URL_FIND_NEAR_BY_CINEMAS_GET = "cinema/v1/findNearbyCinemas?longitude=%f&latitude=%f&page=%d&count=10";
    /**
     * 3.查询电影信息明细
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/findCinemaInfo
     * 请求方式：GET
     * */
    public static final String URL_FIND_CINEMA_INFO_GET = "cinema/v1/findCinemaInfo?cinemaId=%d";
    /**
     * 4.根据电影名称模糊查询电影院
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/findAllCinemas
     * 请求方式：GET
     * */
    public static final String URL_FIND_ALL_CINEMAS_GET = "cinema/v1/findAllCinemas";
    /**
     * 5.查询用户关注的影院信息
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/verify/findCinemaPageList
     * 请求方式：GET
     * */
    public static final String URL_FIND_CINEAM_PAGE_LIST_GET = "cinema/v1/verify/findCinemaPageList?page=%d&count=10";
    /**
     * 6.关注影院
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/verify/followCinema
     * 请求方式：GET
     * */
    public static final String URL_FOLLOW_CINEAM_GET = "cinema/v1/verify/followCinema?cinemaId=%d";
    /**
     * 7.取消关注影院
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/verify/cancelFollowCinema
     * 请求方式：GET
     * */
    public static final String URL_CANCEL_FOLLOW_CINEAM_GET = "cinema/v1/verify/cancelFollowCinema?cinemaId=%d";
    /**
     * 8.查询影院用户评论列表
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/findAllCinemaComment
     * 请求方式：GET
     * */
    public static final String URL_FIND_ALL_CINEAM_COMMENT_GET = "cinema/v1/findAllCinemaComment?cinemaId=%d&page=%d&count=10";
    /**
     * 9.影院评论
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/verify/cinemaComment
     * 请求方式：POST
     * */
    public static final String URL_CINEMA_COMMENT_POST = "cinema/v1/verify/cinemaComment";
    /**
     * 10.影院评论点赞
     * 接口地址：http://172.17.8.100/movieApi/cinema/v1/verify/cinemaCommentGreat
     * 请求方式：POST
     * */
    public static final String URL_CINEMA_COMMENT_GREAT_POST = "cinema/v1/verify/cinemaCommentGreat";

    /**
     *1.意见反馈
     * 接口地址：http://172.17.8.100/movieApi/tool/v1/verify/recordFeedBack
     * 请求方式：POST
     * */
    public static final String URL_RECORD_FEED_BACK_POST = "tool/v1/verify/recordFeedBack";
    /**
     * 2.查询新版本
     * 接口地址：http://172.17.8.100/movieApi/tool/v1/findNewVersion
     * 请求方式：GET
     * */
    public static final String URL_FIND_NEW_VERSION_GET = "tool/v1/findNewVersion";
    /**
     * 3.查询系统消息列表
     * 接口地址：http://172.17.8.100/movieApi/tool/v1/verify/findAllSysMsgList
     * 请求方式：GET
     * */
    public static final String URL_FIND_ALL_SYS_MSG_LIST = "tool/v1/verify/findAllSysMsgList";
    /**
     * 4.系统消息读取状态修改
     * 接口地址：http://172.17.8.100/movieApi/tool/v1/verify/changeSysMsgStatus
     * 请求方式：GET
     * */
    public static final String URL_CHABGE_SYS_MSG_STATUS_GET = "tool/v1/verify/changeSysMsgStatus";
    /**
     * 5.查询用户当前未读消息数量
     * 接口地址：http://172.17.8.100/movieApi/tool/v1/verify/findUnreadMessageCount
     * 请求方式：GET
     * */
    public static final String URL_FIND_UNREAD_MESSAGE_COUNT_GET = "tool/v1/verify/findUnreadMessageCount";
    /**
     * 6.上传消息推送使用的token
     * 接口地址：http://172.17.8.100/movieApi/tool/v1/verify/uploadPushToken
     * 请求方式：POST
     * */
    public static final String URL_UP_LOAD_PUSH_TOKEN_POST = "tool/v1/verify/uploadPushToken";
    /**
     * 7.微信分享前置接口，获取分享所需参数
     * 接口地址：http://172.17.8.100/movieApi/tool/v1/wxShare
     * 请求方式：GET
     * */
    public static final String URL_WX_SHARE_GET = "tool/v1/wxShare";
}
