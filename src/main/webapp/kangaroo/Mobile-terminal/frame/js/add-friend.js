/*
 * @Author: shn 
 * @Date: 2018-04-11 18:30:44 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 14:03:54
 */

$(function () {
    $("#add-friend-ul").css("width", window.screen.width)
    $(".add-friend-content").css("height", window.screen.height - 200)
    // searchBar 方法
    $(document).on("click touchstart", ".weui-search-bar__label", function (e) {
            $(e.target).parents(".weui-search-bar").addClass("weui-search-bar_focusing").find('input').focus();
        })

        .on("click", ".weui-search-bar__cancel-btn", function (e) {
            var $input = $(e.target).parents(".weui-search-bar").removeClass("weui-search-bar_focusing").find(".weui-search-bar__input").val("").blur();
            $("#add-friend-ul").html("");
        })
        .on("click", ".weui-icon-clear", function (e) {
            var $input = $(e.target).parents(".weui-search-bar").find(".weui-search-bar__input").val("").focus();
            $("#add-friend-ul").html("");
            add_friend_apply();
        });

    // 键盘按键up事件
    $("#searchUser").keyup(function () {
        $("#add-friend-ul").html("");
        add_data($("#searchUser").val());
    })

    $("#searchUser").keydown(function (e) {
        if (e.keyCode == 13) {
            event.preventDefault();
        }
    });

    // 返回按钮
    $(".add-friend-back").click(function () {
        $("#frame-main").css("display", "block");

        $("#frame-add-friend").animate({
            left: '100%',
            opacity: '0'
        }, 250);
        $("#frame-main").animate({
            opacity: '1'
        }, 250);

        setTimeout(function () {
            $("#frame-add-friend").css("display", "none");
            $(".add-friend-back").css("display", "none");
        }, 250);

        $("#add-friend-ul").html("");
    });

})

// 根据搜索值搜索用户并添加至div
function add_data(search_wd) {
    if (search_wd != '') {
        $.ajax({
            type: 'GET',
            url: 'http://' + ip + '/friends/search?wd=' + search_wd + '&pagenum=1&pagesize=100',
            success: function (json) {
                if (json.success == true) {
                    for (let i = 0; i < json.data.rows.length; i++) {
                        $("#add-friend-ul").append(
                            "<div class='weui-cell'>" +
                            "<div class='weui-cell__hd'>" +
                            "<img style='width:30px;margin-right:5px;display:block;border-radius:50px;' src='http://" + ip + "/headpics/" + json.data.rows[i].headpic + "'>" +
                            "</div>" +
                            "<div class='weui-cell__bd'>" +
                            json.data.rows[i].name + "(" + json.data.rows[i].id + ")" +
                            "</div>" +
                            "<div class='weui-cell__ft'>" +
                            "<img style='width:15px;display:block;' src='img/add-friend.png' id='" + json.data.rows[i].id + "' onclick='add_friend_button(this)'>" +
                            "</div>" +
                            "</div>"
                        )
                    }

                } else {

                }
            }
        });
    }
}

// 用户右边的添加好友事件
function add_friend_button(data) {

    $.ajax({
        type: 'POST',
        url: 'http://' + ip + '/friends/' + data.id,
        success: function (json) {
            $.hideLoading();
            if (json.success == true) {
                $.toast("发送请求成功", 1000);
            } else {
                $.toast("您已发送请求", "cancel", 1000);
            }
        },
        error: function () {
            $.hideLoading();
            $.toast("发送请求失败", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading("正在发送请求");
        },
    });

}


function add_friend_apply() {
    $.ajax({
        type: 'GET',
        url: 'http://' + ip + '/friends/request',
        success: function (json) {
            if (json.success == true) {
                for (let i = 0; i < json.data.length; i++) {
                    $("#add-friend-ul").append(
                        "<div class='weui-cell'>" +
                        "<div class='weui-cell__hd'>" +
                        "<img style='width:30px;margin-right:5px;display:block;border-radius:50px;' src='http://" + ip + "/headpics/" + json.data[i].headpic + "'>" +
                        "</div>" +
                        "<div class='weui-cell__bd'>" +
                        "<span class='add-friend-content-name'>" + json.data[i].name + "</span>" +
                        "(<span class='add-friend-content-id'>" + json.data[i].id + "</span>)" +
                        "</div>" +
                        "<div class='weui-cell__ft'>" +
                        "<div style='float:right;font-size:15px;margin-left:30px;' onclick='add_friend_request_button(this)' id='refuse-" + json.data[i].id + "'>拒绝</div>" +
                        "<div style='float:right;color:rgb(252, 183, 72);font-size:15px' onclick='add_friend_request_button(this)' id='agree-" + json.data[i].id + "'>接受</div>" +
                        "</div>" +
                        "</div>"
                    )
                }
            } else {

            }
        }
    });
}

function add_friend_request_button(data) {
    let param;
    let id = data.id.split("-");
    console.log(id[1]);
    if (id[0] == "agree") {
        param = 1;
    } else {
        param = 0;
    }

    $.ajax({
        type: 'POST',
        url: 'http://' + ip + '/friends/' + id[1] + '/agree?param=' + param,
        success: function (json) {
            $("#add-friend-ul").html("");
            add_friend_apply();
            $("#friend-content").html("");                                        
            friend_li();
        }
    });


}