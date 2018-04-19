/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:06 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 21:43:40
 */
var websocket = null;

//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://" + ip + "/fws");
} else {
    alert('Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    console.log("error")
};

//连接成功建立的回调方法
websocket.onopen = function (event) {
    console.log("open")
}

//接收到消息的回调方法
websocket.onmessage = function () {
    let json = JSON.parse(event.data);
    console.log(event.data);

    // 好友请求通知
    if (json.type == 0) {
        console.log(event.data);
        $.notification({
            title: "添加好友",
            text: json.sender.name + " (" + json.sender.id + ")    向您发出好友请求",
            media: "<img src='http://" + ip + "/headpics/" + json.sender.headpic + "'>",
            data: json,
            time: 6000,
            onClick: function (user_info) {
                $("#notice-friend-name").text();
                $("#notice-friend-id").text();
                $("#notice-friend-headpic").attr("src", "http://" + ip + "/headpics/" +
                    user_info.sender.headpic);
                $("#add-friend-request").modal('show');

                $.modal({
                    title: "好友申请",
                    text: user_info.sender.name + "(" + user_info.sender.id + ")  向您发出好友请求",
                    buttons: [{
                            text: "同意",
                            onClick: function () {
                                $.ajax({
                                    type: 'POST',
                                    url: 'http://' + ip + '/friends/' + user_info.sender.id + '/agree?param=1',
                                    success: function (json) {
                                        $.hideLoading();
                                        $.toast("您已同意", 1000);
                                        $("#friend-content").html("");                                        
                                        friend_li();
                                    },
                                    error: function () {
                                        $.hideLoading();
                                        $.toast("操作失败", "cancel", 1000);
                                    },
                                    beforeSend: function () {
                                        $.showLoading();
                                    },
                                });
                            }
                        },
                        {
                            text: "拒绝",
                            className: "default",
                            onClick: function () {
                                $.ajax({
                                    type: 'POST',
                                    url: 'http://' + ip + '/friends/' + user_info.sender.id + '/agree?param=0',
                                    success: function (json) {
                                        $.hideLoading();
                                        $.toast("您已拒绝", 1000);
                                    },
                                    error: function () {
                                        $.hideLoading();
                                        $.toast("操作失败", "cancel", 1000);
                                    },
                                    beforeSend: function () {
                                        $.showLoading();
                                    },
                                });
                            }
                        },
                    ]
                });

                $.closeNotification();
            },
        });
        // 同意好友请求刷新好友列表
    } else if (json.type == 1) {
        $("#friend-content").html("");
        friend_li();
        // 接收好友消息通知,并把消息投影到聊天界面
    } else if (json.type == 2) {
        let dm_content = change_bq_img_f_c(json.data);

        console.log(dm_content);

        if ($(".chatBox").css("opacity") == 1) {
            // 当你正在聊天窗口时
            if (json.sender.name == $(".ChatInfoName").html()) {
                let myDate = new Date().toLocaleString();

                $(".chatBox-content-demo").append("<div class='clearfloat'>" +
                    "<div class='author-name'><small class='chat-date' style='float:left;font-size:75%'>" + myDate + "</small> </div><br>" +
                    "<div class='left'><div class='chat-avatars'><img src='" + "http://" + ip + "/headpics/" + json.sender.headpic + "' alt='头像' /></div>" +
                    "<div class='chat-message'> " + dm_content + " </div> " +
                    "</div> </div>");

                $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight)

                // 不在聊天窗口弹出通知
            } else {
                friend_msg_notice(json);
            }
        } else {
            friend_msg_notice(json);
        }

    } else if (json.type == 3) {

        if ($(".chatBox").css("opacity") == 1) {
            // 当你正在聊天窗口时
            if (json.sender.name == $(".ChatInfoName").html()) {
                let myDate = new Date().toLocaleString();

                console.log()
                $(".chatBox-content-demo").append("<div class='clearfloat'>" +
                    "<div class='author-name'><small class='chat-date' style='float:left;font-size:75%'>" + myDate + "</small> </div><br>" +
                    "<div class='left'><div class='chat-avatars'><img src='" + "http://" + ip + "/headpics/" + json.sender.headpic + "' alt='头像' /></div>" +
                    "<div class='chat-message'><img src='http://" + ip + "/chatimages/" + json.data + "' style='max-width:230px'> </div> " +
                    "</div> </div>");

                $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight)

                // 不在聊天窗口弹出通知
            } else {
                friend_msg_notice(json);
            }
        } else {
            friend_msg_notice(json);
        }


        //小袋提示活动开始
    } else if (json.type == 4) {
        let _content = json.data.substring(0, json.data.length - 1);
        _content = _content.split("(#");
        $.notification({
            title: "通知",
            text: _content[0],
            data: _content[1],
            onClick: function (data) {
                show_activity_info(1, data);
                $.closeNotification();
            },
        });
    }

}

//连接关闭的回调方法
websocket.onclose = function () {
    console.log("close")
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    websocket.close();
}

//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    alert("show");
}

//关闭连接
function closeWebSocket() {
    websocket.close();
}

//发送消息
function send(msg) {
    websocket.send(msg);
    console.log(msg);
}

function friend_msg_notice(json) {
    $.notification({
        title: "好友消息",
        text: json.sender.name + " (" + json.sender.id + ")    有新消息......",
        media: "<img src='http://" + ip + "/headpics/" + json.sender.headpic + "'>",
        data: json,
        time: 6000,
        onClick: function (msg_info) {
            $.closeNotification();
            $(".chatBox-send").show()
            if (window.screen.width >= 1024) {
                $(".chatBox").animate({
                    left: '-400px',
                    top: '700px',
                    opacity: '1'
                }, 250);
            } else {
                $(".chatBox").animate({
                    left: '0',
                    opacity: '1'
                }, 250);
            }
            $("#frame-main").animate({
                opacity: '0'
            }, 250);
            $("#frame-main").css("display", "none");
            $("#chat").css("display", "block");
            $(".ChatInfoName").html(msg_info.sender.name);
            $(".ChatInfoHead img").attr("src", "http://" + ip + "/headpics/" + msg_info.sender.headpic);

            $(".chatBox-content-demo").html("");

            receive = msg_info.sender.id;

            $.ajax({
                type: 'GET',
                url: 'http://' + ip + '/chattingrecord/' + receive + '/friend',
                success: function (json) {
                    if (json.success == true) {
                        for (let i = json.data.length - 1; i >= 0; i--) {
                            let date_history = new Date(json.data[i].createtime).toLocaleString();

                            let send_content = "";
                            if (json.data[i].type == 2) {                                
                                send_content = change_bq_img_f_c(json.data[i].content);
                            } else if (json.data[i].type == 4) {
                                let _content = json.data[i].content.substring(0, json.data[i].content.length - 1);
                                _content = _content.split("(#");
                                send_content = "<span style='color:blue' onclick='show_activity_info(1," + _content[1] + ")'>" + _content[0] + "</span>";

                            } else if (json.data[i].type == 3) {
                                send_content = "<img src='http://" + ip + "/chatimages/" + json.data[i].content + "'>";
                            }


                            if (json.data[i].sender == receive) {
                                $(".chatBox-content-demo").append("<div class='clearfloat'>" +
                                    "<div class='author-name'><small class='chat-date' style='float:left;font-size:75%'>" + date_history + "</small> </div><br>" +
                                    "<div class='left'><div class='chat-avatars'><img src='" + "http://" + ip + "/headpics/" + msg_info.sender.headpic + "' alt='头像' /></div>" +
                                    "<div class='chat-message'> " + send_content + "</div> " +
                                    "</div> </div>");
                            } else {
                                $(".chatBox-content-demo").append("<div class='clearfloat'>" +
                                    "<div class='author-name'><small class='chat-date' style='float:right;font-size:75%'>" + date_history + "</small> </div><br>" +
                                    "<div class='right'><div class='chat-message'>" + send_content + "</div> " +
                                    "<div class='chat-avatars'><img src='" + "http://" + ip + "/headpics/" + myself_headpic + "' alt='头像' /></div>" +
                                    "</div> </div>");
                            }
                        }
                    } else {

                    }
                    //聊天框默认最底部
                    $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight)
                }
            });


        },
    });
}

function change_bq_img_f_c(str){
    do{
        temp = str;
        str = str.replace('{/','<img class="emoji-picker-image emoji-picker-image-chat" style="background-position: ');
        str = str.replace('/}',';" src="img/white.png">');
    }while(str!=temp);
    return temp;
}