/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:27 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 21:36:13
 */
$(function () {

    friend_li();
    $("#friend-information-div").css("height", document.documentElement.clientHeight - 75);
    $("#friend-information-div").css("width", document.documentElement.clientWidth);
    $("#friend-content").css("height", document.documentElement.clientHeight - 150)
    $("#friend-content").css("width", document.documentElement.clientWidth)

    $(".activity-sort").css("width", document.documentElement.clientWidth);

    $(".friend-information-back").click(function () {

        $("#friend-information-div").animate({
            left: "102%",
            opacity: 0
        }, 200);
        $("#friend-content").css("display", "block");

        setTimeout(function () {
            $("#friend-information-div").css("display", "none");
        }, 200);
    });

    // 修改备注
    $("#mark").click(function () {
        $.prompt({
            title: '设置好友备注',
            input: $(".friend-information-mark").html(),
            empty: true, // 是否允许为空
            onOK: function (input) {
                $(".friend-information-mark").html(input);

                $.ajax({
                    type: 'POST',
                    url: "http://" + ip + "/friends/friendremark?uid=" + friend_information_id[2] + "&remark=" + input,
                    success: function (json) {
                        if (json.success == true) {
                            $.hideLoading();
                            $.toast("修改成功", 1000);
                            $("#" + friend_information_id[2]).find(".weui-cell__bd_content").html(input)
                        } else {
                            $.toast("修改失败请重试", "cancel", 1000);
                        }
                    },
                    error: function () {
                        $.toast("修改失败请重试", "cancel", 1000);
                    }
                })
            },
            onCancel: function () {
                //点击取消
            },
            beforeSend: function () {
                $.showLoading();
            }
        });
    });

    $("#delete-friend-button").click(function () {
        delete_friend(this);
    });

    $("#send-msg-button").click(function () {
        $("#chat-" + friend_information_id[1] + "-" + friend_information_id[2]).trigger("click");
        $(".friend-information-back").trigger("click");
    });
});

// 删除好友
function delete_friend(data) {

    let id = data.id.split("-");

    $.confirm({
        title: '确认删除好友？',
        text: '',
        onOK: function () {
            console.log(id[2]);
            $.ajax({
                type: 'POST',
                url: 'http://' + ip + '/friends/' + id[2] + '/del',
                success: function (json) {
                    $.hideLoading();
                    if (json.success == true) {
                        $.toast("删除好友成功", 1000);
                        $("#" + id[2]).remove();
                        $(".friend-information-back").trigger("click");
                    } else {
                        $.toast("操作失败", "cancel", 1000);
                    }
                },
                error: function () {
                    $.toast("删除失败，请重试", "cancel", 1000);
                },
                beforeSend: function () {
                    $.showLoading();
                }
            });
        },
        onCancel: function () {}
    });



}

var friend_json;

// 加载好友列表
function friend_li() {
    $("#friend-content").html("");
    $.ajax({
        type: 'GET',
        url: 'http://' + ip + '/friends',
        success: function (json) {
            friend_json = json;
            if (json.success == true) {
                for (let i = 0; i < json.data.length; i++) {
                    $("#friend-content").append(
                        "<div class='weui-cell weui-cell_swiped' id='" + json.data[i].id + "'>" +
                        "<div class='weui-cell__bd'>" +
                        "<div class='weui-cell'>" +
                        "<div class='weui-cell__hd'>" +
                        "<img style='width:30px;height:30px;margin-right:5px;border-radius:10px;' src='http://" + ip + "/headpics/" + json.data[i].headpic + "' id='headpic-" + i + "-" + json.data[i].id + "' onclick='open_friend_information(this)'>" +
                        "<div class='unread' id='" + json.data[i].id + "-unread-" + i + "'>!</div>" +
                        "</div>" +
                        "<div class='weui-cell__bd weui-cell__bd_content'  onclick='open_chat_ui(this)' id='chat-" + i + "-" + json.data[i].id + "'>" +
                        json.data[i].friendremark +
                        "</div>" +
                        // "<div class='weui-cell__ft'>向左滑动试试</div>" +
                        "</div>" +
                        "</div>" +
                        "<div class='weui-cell__ft'>" +
                        "<div class='weui-swiped-btn weui-swiped-btn_default close-swipeout' style='line-height:30px;background-color:#df4f4f' id='delete-swiped-" + json.data[i].id + "' onclick='delete_friend(this)'>删除好友</div>" +
                        "</div>" +
                        "</div>"
                    )

                    if (json.data[i].unread == true) {
                        $("#" + json.data[i].id + "-unread-" + i).css("display", "block");
                    }
                }

            } else {

            }
        }
    });
}

var receive = "";

// 打开好友聊天界面
function open_chat_ui(friend_chat_id) {
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

    let number = friend_chat_id.id.split("-");

    receive = friend_json.data[number[1]].id;
    $(".ChatInfoName").html(friend_json.data[number[1]].friendremark);
    $(".ChatInfoHead img").attr("src", "http://" + ip + "/headpics/" + friend_json.data[number[1]].headpic);

    $(".chatBox-content-demo").html("");

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
                        send_content = "<img src='http://" + ip + "/chatimages/" + json.data[i].content + "' style='max-width:230px;'>";
                    }

                    if (json.data[i].sender == receive) {
                        $(".chatBox-content-demo").append("<div class='clearfloat'>" +
                            "<div class='author-name'><small class='chat-date' style='float:left;font-size:75%'>" + date_history + "</small> </div><br>" +
                            "<div class='left'><div class='chat-avatars'><img src='" + "http://" + ip + "/headpics/" + friend_json.data[number[1]].headpic + "' alt='头像' /></div>" +
                            "<div class='chat-message'>" + send_content + "</div>" +
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
            $("#chatBox-content-demo").scrollTop($("#chatBox-content-demo")[0].scrollHeight)
        }
    });

}

var friend_information_id;

// 打开好友信息界面
function open_friend_information(friend_information) {

    friend_information_id = friend_information.id.split("-");

    $("#friend-information-div").css("display", "block");
    $("#friend-information-div").animate({
        left: 0,
        opacity: 1
    }, 200);

    setTimeout(function () {
        $("#friend-content").css("display", "none");
        $(".")
    }, 200);

    $.ajax({
        type: "GET",
        url: "http://" + ip + "/friends/" + friend_information_id[2],
        success: function (json) {
            $.hideLoading();
            if (json.success == true) {
                if (json.data.sex == "男") {
                    $(".friend-information-sex").attr("src", "../friend/img/boy.png");
                } else {
                    $(".friend-information-sex").attr("src", "../friend/img/girl.png");
                }
                $(".weui-btn_default").show();
                $(".friend-information-headpic").attr("src", "http://" + ip + "/headpics/" + json.data.headpic);
                $(".friend-information-name").html(json.data.name);
                $(".friend-information-mark").html(json.data.friendremark);
                $(".friend-information-area").html(json.data.area);
                $(".friend-information-phone").html(json.data.phone);
                $(".weui-btn_default").attr("id", "delete-button-" + json.data.id);
                if (friend_information_id[2] == 0) {
                    $("#delete-button-0").hide();
                }
            } else {
                $.toast("获取好友信息失败", "cancel", 1000);
            }
        },
        error: function () {
            $.hideLoading();
            $.toast("获取好友信息失败", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        }
    });



}