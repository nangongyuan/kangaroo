/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:00 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 16:42:29
 */
var room_websocket = null;
var room_redpacket_data = null;
var room_people_num = 0;

function room_websocket_function() {

    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        room_websocket = new WebSocket("ws://" + ip + "/aws");
    } else {
        alert('Not support websocket')
    }

    //连接发生错误的回调方法
    room_websocket.onerror = function () {
        console.log("error")
    };

    //连接成功建立的回调方法
    room_websocket.onopen = function (event) {
        console.log("open")
    }

    //接收到消息的回调方法
    room_websocket.onmessage = function () {
        let json = JSON.parse(event.data);
        console.log(event.data);

        // 用户弹幕
        if (json.type == 0) {

            let dm_content = change_bq_img_f(json.data);
            $(".roomBox-content").append(
                "<div class='room-barrage'><span class='room-barrage-user-name'>" + json.sender.name + "&nbsp;</span>:&nbsp;" + dm_content + "</div>"
            )
            
            $(".roomBox-content").scrollTop = $(".roomBox-content").scrollHeight;
            
            // 投票信息
        } else if (json.type == 8) {
            if (json.data.aid == now_activity_id) {
                $(".no-vote").hide();
                $("#room-vote").show();
                $.toptip('房主发起投票', 'warning');
                var rid = parseInt(Math.random() * 10000000);
                var _vote_title = json.data.name;
                if (json.data.multiselect != 0) {
                    _vote_title = _vote_title + "(多选)"
                }
                $("#room-vote").append(
                    "<div class='room-vote-panel' id='room-vote-content-a-" + rid + "'>" +
                    "<div class='weui-cell' style='padding:15px 15px;font-size:15px;'>" +
                    "<div class='weui-cell__bd'></div>" +
                    "<div class='weui-cell__ft'>" + _vote_title + "</div>" +
                    "</div>" +
                    "<div id='room-vote-content-" + json.data.name + "'>" +
                    "</div>" +
                    "<a href='javascript:;' class='weui-btn weui-btn_primary' style='background-color:rgb(252, 183, 72);border-radius:0'>投票</a>" +
                    "</div>"
                )

                //判断是否已投票

                if (json.data.multiselect == 0) {
                    for (let i = 0; i < json.data.voteGroups.length; i++) {
                        $("#room-vote-content-" + json.data.name).append(
                            "<label class='weui-cell weui-check__label' for='x" + json.data.voteGroups[i].id + "' id='label-" + json.data.voteGroups[i].id + "' >" +
                            "<div class='weui-cell__bd'>" +
                            "<p>" + json.data.voteGroups[i].name + "</p>" +
                            "</div>" +
                            "<div class='weui-cell__ft'>" +
                            "<input type='radio' class='weui-check' name='radio-" + json.data.name + "' id='x" + json.data.voteGroups[i].id + "'>" +
                            "<span class='weui-icon-checked-radius' style='display:none'>" +
                            "<i class='fa fa-check' style='color: green;'></i>" +
                            "</span>" +
                            "</div>" +
                            "</label>"
                        )
                    }
                    initPanel('#' + "room-vote-content-a-" + rid);
                    $("#label-" + json.data.voteGroups[0].id).trigger("click");
                } else {
                    for (let i = 0; i < json.data.voteGroups.length; i++) {
                        $("#room-vote-content-" + json.data.name).append(
                            "<label class='weui-cell weui-check__label' for='x" + json.data.voteGroups[i].id + "' id='label-" + json.data.voteGroups[i].id + "' >" +
                            "<div class='weui-cell__bd'>" +
                            "<p>" + json.data.voteGroups[i].name + "</p>" +
                            "</div>" +
                            "<div class='weui-cell__fd'>" +
                            "<input type='checkbox' class='weui-check' name='checkbox" + json.data.name + "' id='s" + json.data.voteGroups[i].id + "'>" +
                            "<span class='weui-icon-checked-check' style='display:none'>" +
                            "<i class='fa fa-check' style='color: green;'></i>" +
                            "</span>" +
                            "</div>" +
                            "</label>"
                        )
                    }
                    initPanelCheck('#' + "room-vote-content-a-" + rid);
                }

            }
            // 用户进入房间
        } else if (json.type == 10) {
            $(".roomBox-content").append(
                "<div class='room-barrage'><span class='room-barrage-user-join'>" + json.data + "&nbsp;&nbsp;进入房间</div>"
            )
            $(".roomBox-content").scrollTop = $(".roomBox-content").scrollHeight;
            
            //获取抽奖信息
        } else if (json.type == 11) {
            for (let i = 0; i < json.data.users.length; i++) {
                $("#room-gift").append(
                    "<div class='weui-cell' style='padding:15px 15px;font-size:15px;'>" +
                    "<div class='weui-cell__bd'>" +
                    json.data.users[i].name +
                    "</div>" +
                    "<div class='weui-cell__ft'>" +
                    "抽中了" + json.data.name + "---" + json.data.prizename +
                    "</div>" +
                    "</div>"
                )
            }
            // 发言模式被修改
        } else if (json.type == 13) {
            if (now_activity_id == json.receiver) {
                
                if (json.data == 1) {
                    $.toptip('房主已将聊天模式设置为全员禁言', 'warning');
                    console.log(user_type + ":" + speak_type);
                    if(user_type == 1){
                        speak_type = 1;                                            
                    }else{
                        speak_type = 0;                                                                    
                    }
                } else {
                    $.toptip('房主已将聊天模式设置为无限制', 'success');
                    speak_type = 1;
                }
            }

            // 收到评分请求
        } else if (json.type == 15) {
            $.modal({
                title: "请您给本次活动评个分吧~",
                text: "<div id='starRating'>" +
                    "<p class='scorephoto'>" +
                    "<span><i class='high'></i><i class='nohigh'></i></span>" +
                    "<span><i class='high'></i><i class='nohigh'></i></span>" +
                    "<span><i class='high'></i><i class='nohigh'></i></span>" +
                    "<span><i class='high'></i><i class='nohigh'></i></span>" +
                    "<span><i class='high'></i><i class='nohigh'></i></span>" +
                    "</p><p class='starNum'>0.0分</p>",
                buttons: [{
                        text: "确定",
                        onClick: function () {
                            msg = '{"type":16,"receiver":' + now_activity_id + ',"data":' +
                                starRating + '}'
                            send_room_barrage(msg);
                        }
                    },
                    {
                        text: "不参与",
                        className: "default",
                        onClick: function () {
                            $.closeModal();
                        }
                    },
                ]
            });

            // 评分
            $('.scorephoto span').on('mouseenter', function () {
                var index = $(this).index() + 1;
                $(this).prevAll().find('.high').css('z-index', 1)
                $(this).find('.high').css('z-index', 1)
                $(this).nextAll().find('.high').css('z-index', 0)
                $('.starNum').html((index * 2).toFixed(1) + '分')
            })
            $('.scorephoto span').click(function () {
                var index = $(this).index() + 1;
                $(this).prevAll().find('.high').css('z-index', 1)
                $(this).find('.high').css('z-index', 1)
                starRating = index;
                $('.starNum').html(starRating.toFixed(1) + '分');
            })

            // 评分请求失败
        } else if (json.type == 17) {
            $.toptip('您已发起过评分请求', 'error');

            // 收到在线人数
        } else if (json.type == 21) {
            room_people_num = json.data;
            $(".roomBox-content-pnum span").html(room_people_num);
            // 弹出红包
        } else if (json.type == 18) {
            $("#romm_red_packet_modal").fadeIn(300);
            room_redpacket_data = json.data;
            $(".chuai").attr("onclick", "room_red_packet_modal_img()");
            $(".chuai p").html("");
            // 抢到红包金额
        } else if (json.type == 20) {
            if (json.data <= 0) {
                $(".chuai p").html("很遗憾，红包被抢完啦");
            } else {
                $(".chuai p").html("恭喜你，抢到&nbsp;" + json.data + "&nbsp;元！");
            }
            //房主发起签到广播给用户
        } else if (json.type == 27) {
            $.toptip('房主发起了签到', 'warning', 2000);

            $("#huochairenqiandao").show();

            $("#huochairenqiandao").animate({
                right: "60px"
            }, 200);
            $("#huochairenqiandao").animate({
                right: "0px"
            }, 400);

            //用户分享的图片显示在弹幕上
        } else if (json.type == 23) {

        } else if (json.type == 26) {
            if (json.data == "success") {
                $.toast("签到成功", 500);
                $("#huochairenqiandao").animate({
                    right: "60px"
                }, 400);
                $("#huochairenqiandao").animate({
                    right: "-50px"
                }, 200);
                setTimeout(function () {
                    $("#huochairenqiandao").hide();
                }, 600);
            } else {
                $.toast(json.data, 500);
            }
        }

    }

    //连接关闭的回调方法
    room_websocket.onclose = function () {
        console.log("close")
        $(".room-back").trigger("click");
        $("#huochairenqiandao").hide();

    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    room_websocket.onbeforeunload = function () {
        room_websocket.close();
    }


}

//关闭连接
function closeWebSocket() {
    room_websocket.close();
}

//发送消息
function send_room_barrage(msg) {
    console.log(msg);
    room_websocket.send(msg);
}

//格式化表情 (反转)
function change_bq_img_f(str){
    do{
        temp = str;
        str = str.replace('{/','<img class="emoji-picker-image  emoji-picker-image-room" style="background-position: ');
        str = str.replace('/}',';" src="img/white.png">');
    }while(str!=temp);
    return temp;
}