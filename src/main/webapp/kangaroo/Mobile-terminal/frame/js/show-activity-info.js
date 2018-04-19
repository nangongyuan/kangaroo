/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:11 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-17 15:52:28
 */
var user_type = 0; //1房主 0用户
$(function () {
    $(".activity-information-back").click(function () {

        $("#frame-main").css("display", "block");
        $("#show-activity-info").animate({
            top: 0,
            left: "102%",
            opacity: 0,
        }, 250);

        setTimeout(function () {
            $("#show-activity-info").css("display", "none");
        }, 250);
    });

    $(".activity-information-button-left1").click(function () {
        $.modal({
            title: "选择分享的好友",
            text: '<div style="height:150px;overflow-y:scroll">' +
                '<ul style="padding:0;height:100%;" id="share-friend-ul-id"></ul>' +
                '</div>',
            buttons: [{
                text: "取消",
                className: "default",
                onClick: function () {
                    $.closeModal();
                }
            }, ]
        });

        $.ajax({
            type: 'GET',
            url: 'http://' + ip + '/friends',
            success: function (json) {
                $.hideLoading();
                if (json.success == true) {
                    activity_number = json.data.length;
                    for (let i = 0; i < activity_number; i++) {
                        $("#share-friend-ul-id").append("<li id='share-" + json.data[i].id + "'onclick='share_li(this)' style='font-size: 17px;line-height: 2;'>" + json.data[i].friendremark + "</li>");
                    }
                } else {
                    $.toast("获取好友列表失败", "cancel", 1000);
                }

            },
            beforeSend: function () {
                $.showLoading("正在获取好友列表");
            },
            error: function () {
                $.hideLoading();
                $.toast("获取好友列表失败", "cancel", 1000);
            },
        });
    })

    $("#activity-information-more-button").click(function () {
        $.actions({
            title: "选择操作",
            actions: [{
                text: "查看活动红包信息",
                onClick: function () {
                    $("#activity-information-redpacket-popub").popup();

                    $.ajax({
                        type: 'GET',
                        url: 'http://' + ip + '/activity/' + now_activity_id,
                        success: function (json) {
                            $.hideLoading();
                            if (json.success == true) {
                                $(".ainfo-popub-content-redpacket").html("");
                                for (let i = 0; i < json.data.redPackets.length; i++) {
                                    $(".ainfo-popub-content-redpacket").append(
                                        "<div id='ainfo-popub-all-rp-" + json.data.redPackets[i].id + "'>" +
                                        "<div class='weui-cell' onclick='show_slidedwon_rp(this)' id='ainfo-popub-button-rp-" + json.data.redPackets[i].id + "'>" +
                                        "<div class='weui-cell__bd'>" +
                                        json.data.redPackets[i].id +
                                        "</div>" +
                                        "<div class='weui-cell__ft'>总金额:<span style='color:red'>" +
                                        json.data.redPackets[i].money + "￥</span>&nbsp;<span style='font-size:12px;'> " +
                                        date_Format2(new Date(json.data.redPackets[i].addtime)) + "</span>&#12288;" +
                                        "<img id='ainfo-popub-icon-rp-" + json.data.redPackets[i].id + "' src='img/end-activity/slideDown.png' style='width:20px;height:20px;margin-right:-10px'></div>" +
                                        "</div>" +
                                        "<div class='weui-cells' style='display:none;' id='ainfo-popub-li-rp-" + json.data.redPackets[i].id + "'>" +
                                        "</div>" +
                                        "</div>"
                                    )

                                    for (let j = 0; j < json.data.redPackets[i].redPacketRecords.length; j++) {
                                        $("#ainfo-popub-li-rp-" + json.data.redPackets[i].id).append(
                                            "<div class='weui-cell'>" +
                                            "<div class='weui-cell__bd'>" +
                                            json.data.redPackets[i].redPacketRecords[j].name +
                                            "</div>" +
                                            "<div class='weui-cell__ft'>金额：" + json.data.redPackets[i].redPacketRecords[j].money +
                                            "￥&nbsp;<span style='font-size:12px;'> " + date_Format2(new Date(json.data.redPackets[i].redPacketRecords[j].addtime)) +
                                            "</span></div>" +
                                            "</div>"
                                        )
                                    }

                                    $(".ainfo-popub-content-redpacket .weui-cell:first").css("margin-top", "150px");
                                }

                            } else {
                                $.toast("获取红包信息失败", "cancel", 1000);
                            }
                        },
                        error: function () {
                            $.hideLoading();
                            $.toast("获取红包信息失败", "cancel", 1000);
                        },
                        beforeSend: function () {
                            $.showLoading("请稍等");
                        }
                    })
                }
            }, {
                text: "查看活动投票信息",
                onClick: function () {
                    $("#activity-information-poll-popub").popup();

                    $.ajax({
                        type: 'GET',
                        url: 'http://' + ip + '/activity/' + now_activity_id,
                        success: function (json) {
                            $.hideLoading();
                            if (json.success == true) {
                                $(".ainfo-popub-content-poll").html("");
                                for (let i = 0; i < json.data.votes.length; i++) {
                                    $(".ainfo-popub-content-poll").append(
                                        "<div id='ainfo-popub-all-poll-" + json.data.votes[i].id + "'>" +
                                        "<div class='weui-cell' onclick='show_slidedwon_poll(this)' id='ainfo-popub-button-poll-" + json.data.votes[i].id + "'>" +
                                        "<div class='weui-cell__bd'>" +
                                        json.data.votes[i].id + ":" + json.data.votes[i].name +
                                        "</div>" +
                                        "<div class='weui-cell__ft'><span style='font-size:12px;'> " +
                                        date_Format2(new Date(json.data.votes[i].addtime)) + "</span>" +
                                        "<img id='ainfo-popub-icon-poll-" + json.data.votes[i].id + "' src='img/end-activity/slideDown.png' style='width:20px;height:20px;margin-right:-10px'></div>" +
                                        "</div>" +
                                        "<div class='weui-cells' style='display:none;' id='ainfo-popub-li-poll-" + json.data.votes[i].id + "'>" +
                                        "</div>" +
                                        "</div>"
                                    )

                                    for (let j = 0; j < json.data.votes[i].voteGroups.length; j++) {
                                        $("#ainfo-popub-li-poll-" + json.data.votes[i].id).append(
                                            "<div class='weui-cell'>" +
                                            "<div class='weui-cell__bd'>&#12288;&#12288;" +
                                            json.data.votes[i].voteGroups[j].name +
                                            "</div>" +
                                            "<div class='weui-cell__ft'>" + json.data.votes[i].voteGroups[j].voteRecords.length + "票</div>" +
                                            "</div>"
                                        )
                                    }

                                    $(".ainfo-popub-content-poll .weui-cell:first").css("margin-top", "150px");
                                }

                            } else {
                                $.toast("获取红包信息失败", "cancel", 1000);
                            }
                        },
                        error: function () {
                            $.hideLoading();
                            $.toast("获取红包信息失败", "cancel", 1000);
                        },
                        beforeSend: function () {
                            $.showLoading("请稍等");
                        }
                    })
                },

            }, {
                text: "查看活动抽奖信息",
                onClick: function () {
                    $("#activity-information-gift-popub").popup();

                    $.ajax({
                        type: 'GET',
                        url: 'http://' + ip + '/activity/' + now_activity_id,
                        success: function (json) {
                            $.hideLoading();
                            if (json.success == true) {

                                $(".ainfo-popub-content-gift").html("");
                                for (let i = 0; i < json.data.lotteries.length; i++) {
                                    for (let j = 0; j < json.data.lotteries[i].lotteryRecords.length; j++) {

                                        $(".ainfo-popub-content-gift").append(
                                            "<div class='weui-cells'>" +
                                            "<div class='weui-cell'>" +
                                            "<div class='weui-cell__bd'>" +
                                            json.data.lotteries[i].prizename +
                                            "</div>" +
                                            "<div class='weui-cell__ft'>" +
                                            json.data.lotteries[i].lotteryRecords[j].name +
                                            "</div>" +
                                            "</div>" +
                                            "</div>"
                                        );

                                    };

                                    $(".ainfo-popub-content-gift .weui-cell:first").css("margin-top", "150px");
                                }
                            } else {
                                $.toast("获取抽奖信息失败", "cancel", 1000);
                            }
                        },
                        error: function () {
                            $.hideLoading();
                            $.toast("获取抽奖信息失败", "cancel", 1000);
                        },
                        beforeSend: function () {
                            $.showLoading("请稍等");
                        }
                    })
                },

            }, {
                text: "查看活动签到信息",
                onClick: function () {
                    $("#activity-information-sign-popub").popup();

                    $.ajax({
                        type: 'GET',
                        url: 'http://' + ip + '/activity/' + now_activity_id,
                        success: function (json) {
                            $.hideLoading();
                            if (json.success == true) {

                                $(".ainfo-popub-content-sign").html("");
                                for (let i = 0; i < json.data.signs.length; i++) {

                                    $(".ainfo-popub-content-sign").append(
                                        "<div class='weui-cells'>" +
                                        "<div class='weui-cell'>" +
                                        "<div class='weui-cell__bd'>" +
                                        json.data.signs[i].name +
                                        "</div>" +
                                        "<div class='weui-cell__ft'>" +
                                        json.data.signs[i].id +
                                        "</div>" +
                                        "</div>" +
                                        "</div>"
                                    );


                                    $(".ainfo-popub-content-sign .weui-cell:first").css("margin-top", "150px");
                                }
                            } else {
                                $.toast("获取签到信息失败", "cancel", 1000);
                            }
                        },
                        error: function () {
                            $.hideLoading();
                            $.toast("获取签到信息失败", "cancel", 1000);
                        },
                        beforeSend: function () {
                            $.showLoading("请稍等");
                        }
                    })
                },

            }, {
                text: "查看活动评分",
                onClick: function () {
                    $.ajax({
                        type: 'GET',
                        url: 'http://' + ip + '/activity/' + now_activity_id,
                        success: function (json) {
                            $.hideLoading();
                            if (json.success == true) {
                                $.alert(json.data.grade + "分","评分");                                
                            } else {
                                $.toast("获取活动评分失败", "cancel", 1000);
                            }
                        },
                        error: function () {
                            $.hideLoading();
                            $.toast("获取活动评分失败", "cancel", 1000);
                        },
                        beforeSend: function () {
                            $.showLoading("请稍等");
                        }
                    })
                },

            }]
        });
    })

    $(".ainfo-popub-back").click(function () {
        $.closePopup();
    })

})

function share_li(objct) {
    select_li_id = objct.id.split("-");

    let json_send = '{"type":4,"data":"我给你分享了 ' + now_activity_title + ' 这个活动，一起去看看吧！~(#' + now_activity_id + ')","receiver":' + select_li_id[1] + '}';
    send(json_send);
    $.toast('分享成功', 1000);

    $.closeModal();
}

var now_activity_id = "";
var now_activity_title = "";

function show_activity_info(type, activity_information_id) {

    if (type == 1) {
        now_activity_id = activity_information_id
    } else {
        activity_information_id = activity_information_id.id.split("-");
        now_activity_id = activity_information_id[2];
    }

    $("#show-activity-info").css("display", "block");
    $("#show-activity-info").animate({
        top: 0,
        left: 0,
        opacity: 1
    }, 250);

    setTimeout(function () {
        $("#frame-main").css("display", "none");
    }, 250);

    $.ajax({
        type: 'GET',
        url: "http://" + ip + "/activity/" + now_activity_id + "/detail",
        success: function (json) {
            if (json.success) {
                now_activity_title = json.data.name
                $(".activity-information-title").html(now_activity_title);
                $(".activity-information-type").html(json.data.typeName);
                $(".activity-information-creater").html(json.data.creater);
                $(".activity-information-area").html(json.data.area + "&#12288 " + json.data.site);
                let s = date_Format(new Date(json.data.startTime));
                let e = date_Format(new Date(json.data.endTime));
                $(".activity-information-date").html(s + "&#12288 至&#12288 " + e);
                $(".activity-information-subscribe").html(json.data.subscribeNum + "/" + json.data.max);
                $(".activity-information-button-right").attr("id", "right-button-" + now_activity_id);
                $(".activity-information-intro").html(json.data.intro);
                $(".activity-information-img").attr("src", "http://" + ip + "/advertisings/" + json.data.pic)

                if (json.data.secretkey != null) {
                    user_type = 1;
                    $("#activity-information-key").show();
                    $(".activity-information-key").html(json.data.secretkey);
                    $("#room-nav-text-info").show();
                    $("#room-install").show();
                    $(".room-nav-graphical").css("left", "62px")
                } else {
                    user_type = 0;
                    $("#activity-information-key").hide();
                    $("#room-nav-text-info").hide();
                    $("#room-install").css("display", "none");
                    $(".room-nav-graphical").css("left", "30px")
                }

                if (json.data.status == 1) {
                    if (json.data.subscribe == 1) {
                        $(".activity-information-button-right").html('<i class="weui-icon-success-no-circle"></i>' + "已预订");
                        $(".activity-information-button-right").css('background-color', '#e8e8e8');
                        $(".activity-information-button-right").css('color', '#000');
                        $(".activity-information-button-right").attr("onclick", "unsubscribe(this)");
                    } else {
                        $(".activity-information-button-right").html("我要参加");
                        $(".activity-information-button-right").css('background-color', 'rgb(252, 183, 72)');
                        $(".activity-information-button-right").css('color', '#FFF');
                        $(".activity-information-button-right").attr("onclick", "subscribe(this)");
                    }
                    $("#activity-information-more-button").hide();
                } else if (json.data.status == 0) {
                    if (json.data.password) {
                        $(".activity-information-button-right").html("<i class='fa fa-lock' style='font-size:18px'></i>&nbsp;&nbsp;进入会场");
                        $(".activity-information-button-right").attr("onclick", "inputlock()");
                    } else {
                        $(".activity-information-button-right").html("进入会场");
                        $(".activity-information-button-right").attr("onclick", "join()");
                    }
                    $(".activity-information-button-right").css('background-color', 'rgb(252, 183, 72)');
                    $(".activity-information-button-right").css('color', '#FFF');
                    $("#activity-information-more-button").hide();
                } else {
                    $(".activity-information-button-right").html("已结束");
                    $(".activity-information-button-right").removeAttr('onclick');
                    $(".activity-information-button-right").css('background-color', '#e8e8e8');
                    $(".activity-information-button-right").css('color', '#000');
                    if (type == 2) { //表示房主
                        $("#activity-information-more-button").show();
                    }
                }

                mySwiper = new Swiper('#swiper-one', {

                    initialSlide: user_type,
                    speed: 500,
                    width: document.documentElement.clientWidth,
                    height: document.documentElement.clientHeight,
                    on: {
                        destroy: function () {
                            console.log('你销毁了Swiper;');
                        },
                        slideChangeTransitionEnd: function () {
                            let i = this.activeIndex;
                            if (user_type == 1) {
                                if (i == 0) {
                                    $(".room-nav-graphical").animate({
                                        left: "0",
                                    })
                                } else if (i == 1) {
                                    $(".room-nav-graphical").animate({
                                        left: "63",
                                    })
                                } else if (i == 2) {
                                    $(".room-nav-graphical").animate({
                                        left: "126",
                                    })
                                } else if (i == 3) {
                                    $(".room-nav-graphical").animate({
                                        left: "189",
                                    })
                                }
                            } else {
                                if (i == 0) {
                                    $(".room-nav-graphical").animate({
                                        left: "29",
                                    })
                                } else if (i == 1) {
                                    $(".room-nav-graphical").animate({
                                        left: "93",
                                    })
                                } else if (i == 2) {
                                    $(".room-nav-graphical").animate({
                                        left: "157",
                                    })
                                }
                            }
                        },
                    },
                })

                mySwiper.update(); //更新数量
                $.hideLoading();

            } else {
                $.hideLoading();
                $.toast("获取活动信息失败,请重试", "cancel", 1000);
            }

        },
        error: function () {
            $.hideLoading();
            $.toast("获取活动信息失败,请重试", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        },
    });

}

function subscribe(activity_id) {
    activity_id = activity_id.id.split("-");

    $.ajax({
        type: 'POST',
        url: "http://" + ip + "/activity/" + activity_id[2] + "/subscrible",
        success: function (json) {
            if (json.success) {
                $.hideLoading();
                $.toast("预约成功", 1000);
                $(".activity-information-button-right").html('<i class="weui-icon-success-no-circle"></i>' + "已预订");
                $(".activity-information-button-right").css('background-color', '#e8e8e8');
                $(".activity-information-button-right").css('color', '#000');
                $(".activity-information-button-right").attr("onclick", "unsubscribe(this)");
            } else {
                $.hideLoading();
                $.toast("预约失败", "cancel", 1000);
            }
        },
        error: function () {
            $.hideLoading();
            $.toast("预约失败", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        },
    })
}

function unsubscribe(activity_id) {
    activity_id = activity_id.id.split("-");

    $.ajax({
        type: 'POST',
        url: "http://" + ip + "/activity/" + activity_id[2] + "/cancel",
        success: function (json) {
            if (json.success) {
                $.hideLoading();
                $.toast("取消预约成功", 1000);
                $(".activity-information-button-right").html("我要参加");
                $(".activity-information-button-right").css('background-color', 'rgb(252, 183, 72)');
                $(".activity-information-button-right").css('color', '#FFF');
                $(".activity-information-button-right").attr("onclick", "subscribe(this)");
            } else {
                $.hideLoading();
                $.toast("取消预约失败", "cancel", 1000);
            }
        },
        error: function () {
            $.hideLoading();
            $.toast("取消预约失败", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        },
    })
}

function inputlock() {
    $.prompt({
        title: '请输入房间密码',
        input: '',
        empty: false, // 是否允许为空
        onOK: function (input) {
            join(input);
        },
        onCancel: function () {
            //点击取消
        }
    });
}

function join(password) {
    $.ajax({
        type: "GET",
        url: "http://" + ip + "/activity/" + now_activity_id + "/click?password=" + password,
        success: function (json) {
            if (json.type == 1) {
                $.toast("活动未开始", "cancel", 1000);
            } else if (json.type == 2) {
                $.toast("活动已结束", "cancel", 1000);
            } else if (json.type == 4) {
                $.toast("活动已达最大人数", "cancel", 1000);
            } else {
                if (json.data == 0) {
                    room_websocket_function();
                    $(".activity-information-back").trigger("click");
                    if (window.screen.width >= 1024) {
                        $("#room").animate({
                            left: '-400px',
                            top: '700px',
                            opacity: '1'
                        }, 250);
                    } else {
                        $("#room").animate({
                            left: '0',
                            opacity: '1'
                        }, 250);
                    }
                    $("#frame-main").animate({
                        opacity: '0'
                    }, 250);

                    $(".chat-close").trigger("click");

                    $("#frame-main").css("display", "none");
                    $("#room").css("display", "block");
                    $(".no-vote").show();
                    $("#room-vote").html("");
                    $("#room-vote").hide();
                    Initialization_poll();
                    Initialization_gift()

                } else {
                    $.toast("密码错误", "cancel", 1000);
                }
            }
        }
    })
}

function Initialization_poll() {
    var datalist = new Array;
    $.ajax({
        type: "GET",
        url: "http://" + ip + "/activity/" + now_activity_id + "/votes",
        success: function (json) {
            if (json.success == true) {
                for (let i = 0; i < json.data.length; i++) {

                    if (now_activity_id == json.data[i].aid) {
                        $(".no-vote").hide();
                        $("#room-vote").show();
                        var rid = parseInt(Math.random() * 10000000);
                        var _vote_title = json.data[i].name;
                        if (json.data[i].multiselect != 0) {
                            _vote_title = _vote_title + "(多选)"
                        }
                        $("#room-vote").append(
                            "<div class='room-vote-panel' id='room-vote-content-a-" + rid + "'>" +
                            "<div class='weui-cell' style='padding:15px 15px;font-size:15px;'>" +
                            "<div class='weui-cell__bd'></div>" +
                            "<div class='weui-cell__ft'>" + _vote_title + "</div>" +
                            "</div>" +
                            "<div id='room-vote-content-" + json.data[i].id + "'>" +
                            "</div>" +
                            "<a href='javascript:;' class='weui-btn weui-btn_primary' style='background-color:rgb(252, 183, 72);border-radius:0'>投票</a>" +
                            "</div>"
                        )

                        if (json.data[i].multiselect == 0) {
                            for (let j = 0; j < json.data[i].voteGroups.length; j++) {
                                $("#room-vote-content-" + json.data[i].id).append(
                                    "<label class='weui-cell weui-check__label' for='x" + json.data[i].voteGroups[j].id + "' id='label-" + json.data[i].voteGroups[j].id + "' >" +
                                    "<div class='weui-cell__bd'>" +
                                    "<p>" + json.data[i].voteGroups[j].name + "</p>" +
                                    "</div>" +
                                    "<div class='weui-cell__ft'>" +
                                    "<input type='radio' class='weui-check' name='radio-" + json.data[i].id + "' id='x" + json.data[i].voteGroups[j].id + "'>" +
                                    "<span class='weui-icon-checked-radius' style='display:none'>" +
                                    "<i class='fa fa-check' style='color: green;'></i>" +
                                    "</span>" +
                                    "</div>" +
                                    "</label>"
                                )
                            }
                            initPanel("#room-vote-content-a-" + rid);
                            $("#label-" + json.data[i].voteGroups[0].id).trigger("click");
                        } else {
                            for (let j = 0; j < json.data[i].voteGroups.length; j++) {
                                $("#room-vote-content-" + json.data[i].id).append(
                                    "<label class='weui-cell weui-check__label' for='x" + json.data[i].voteGroups[j].id + "' id='label-" + json.data[i].voteGroups[j].id + "' >" +
                                    "<div class='weui-cell__bd'>" +
                                    "<p>" + json.data[i].voteGroups[j].name + "</p>" +
                                    "</div>" +
                                    "<div class='weui-cell__fd'>" +
                                    "<input type='checkbox' class='weui-check' name='checkbox" + json.data[i].id + "' id='s" + json.data[i].voteGroups[j].id + "'>" +
                                    "<span class='weui-icon-checked-check' style='display:none'>" +
                                    "<i class='fa fa-check' style='color: green;'></i>" +
                                    "</span>" +
                                    "</div>" +
                                    "</label>"
                                )
                            }
                            initPanelCheck("#room-vote-content-a-" + rid);
                        }

                        // 判断是否已投票
                        if (json.data[i].voteIndexs != "") {
                            $("#room-vote-content-a-" + rid).find("a").html("已投票");
                            $("#room-vote-content-a-" + rid).find("a").css("pointer-events", "none");
                            $("#room-vote-content-a-" + rid).find("a").css("background-color", "#e8e8e8");
                            $("#room-vote-content-a-" + rid).find("a").css("color", "#000");
                            $("#room-vote-content-a-" + rid).find('label').css("pointer-events", "none");
                            for (let j = 0; j < json.data[i].voteIndexs.length; j++) {
                                $("#label-" + json.data[i].voteIndexs[j]).trigger("click");
                            }
                        }
                    }

                }

                //初始化显示投票picker
                for (let i = 0; i < json.data.length; i++) {
                    var info = {
                        "title": json.data[i].name,
                        "value": json.data[i].id
                    };
                    datalist.push(info);
                }
                $("#room-show-poll").select({
                    title: "",
                    items: datalist,
                    autoClose: false,
                    beforeClose: function (value, titles) {
                        msg = '{"type":14,"receiver":' + now_activity_id + ',"data":' + value + '}';
                        send_room_barrage(msg);
                    }
                });
            } else {
                console.log("投票信息载入失败");
            }
        }
    })
}

function Initialization_gift() {
    let datalist = new Array;
    $.ajax({
        type: "GET",
        url: "http://" + ip + "/chattingrecord/" + now_activity_id + "/lottery",
        success: function (json) {
            if (json.success == true) {
                for (let i = 0; i < json.data.length; i++) {
                    for (let j = 0; j < json.data[i].lotteryRecords.length; j++) {
                        $("#room-gift").append(
                            "<div class='weui-cell' style='padding:15px 15px;font-size:15px;'>" +
                            "<div class='weui-cell__bd'>" +
                            json.data[i].lotteryRecords[j].name +
                            "</div>" +
                            "<div class='weui-cell__ft'>" +
                            "抽中了" + json.data[i].name + "：" + json.data[i].prizename +
                            "</div>" +
                            "</div>"
                        )
                    }
                }
            } else {
                console.log("抽奖信息载入失败");
            }
        },
        error: function () {
            console.log("抽奖信息载入失败");
        }
    })
}

function show_slidedwon_rp(obj) {
    let id = obj.id.split("-");
    let $_obj = $(obj).parents().find("#ainfo-popub-all-rp-" + id[4]).find("#ainfo-popub-li-rp-" + id[4]);
    let $_icon = $(obj).parents().find("#ainfo-popub-icon-rp-" + id[4]);

    if ($_obj.css("display") == "none") {
        $_obj.slideDown();
        $_icon.attr("src", "img/end-activity/slideUp.png")
    } else {
        $_obj.slideUp();
        $_icon.attr("src", "img/end-activity/slideDown.png")
    }
}

function show_slidedwon_poll(obj) {
    let id = obj.id.split("-");
    let $_obj = $(obj).parents().find("#ainfo-popub-all-poll-" + id[4]).find("#ainfo-popub-li-poll-" + id[4]);
    let $_icon = $(obj).parents().find("#ainfo-popub-icon-poll-" + id[4]);

    if ($_obj.css("display") == "none") {
        $_obj.slideDown();
        $_icon.attr("src", "img/end-activity/slideUp.png")
    } else {
        $_obj.slideUp();
        $_icon.attr("src", "img/end-activity/slideDown.png")
    }
}