/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:46 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 21:24:32
 */

var headpic = "";
var sex = "";
var phone = "";
var area = "";
var gender = "";

var customize_avatar;
var index = 0;
var Avatarsrc;
var $box;
var $li;
var $img;
$(function () {
    //获取信息
    $(".personal-content").css('width', document.documentElement.clientWidth);
    $(".personal-content").css('height', document.documentElement.clientHeight - 60);
    $(".personal-publish-content").css('height', document.documentElement.clientHeight - 220);
    $("#personal-reserve-div").css('width', document.documentElement.clientWidth);
    $("#personal-reserve-div").css('height', document.documentElement.clientHeight);
    $("#personal-history-div").css('width', document.documentElement.clientWidth);
    $("#personal-history-div").css('height', document.documentElement.clientHeight);
    $("#personal-publish-div").css('width', document.documentElement.clientWidth);
    $("#personal-publish-div").css('height', document.documentElement.clientHeight);
    $(".changeone ul").css("width",document.documentElement.clientWidth);

    let url = "http://" + ip + "/users";

    $.ajax({
        url: url,
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        type: 'GET',
        success: function (json) {
            var success = $.trim(json.success);
            if (success == 'true') {
                $("#headpic").attr("src", 'http://' + ip + '/headpics/' + json.data.headpic);
                $("#Gender").text(json.data.sex);
                $("#Area").text(json.data.area);
                gender = json.data.sex;
                headpic = json.data.headpic;
                sex = json.data.sex;
                area = json.data.area;
                phone = json.data.phone;
                $("#name").html(json.data.name);
                $("#uid").html("uid:" + json.data.id);

            } else {

            }
        },
        error: function () {}
    });

    //头像
    $("#Avatar").click(function () {
        $(".my_box").animate({
            'top': '15px',
        }, 300);
    });

    // 获取文本域文字长度并显示
    $('.update-activity-intro').on('keyup', function () {
        var _length = $(this).val().length;
        console.log(_length);
        $("#update-activity-intro-length").html(_length);
    });


    //判断是否需要设置密码
    $("#update-check-password").change(function () {
        if ($("#update-check-password").is(":checked")) {
            $(".update-activity-password").attr('placeholder', "请输入密码");
            $(".update-activity-password").removeAttr('readonly');
        } else {
            $(".update-activity-password").attr('placeholder', "是否设置密码");
            $(".update-activity-password").attr('readonly', "readonly");
        }
    });

});

function update() {
    headpic = $("#headpic")[0].src;
    let avatar = headpic.split('/');

    avatar = encodeURIComponent(avatar[4]); //特殊字符转码
    let url = "http://" + ip + "/users/put?sex=" + gender + "&area=" + area + "&headpic=" + avatar;

    $.ajax({
        url: url,
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        type: 'POST',
        success: function (json) {
            var success = $.trim(json.success);
            if (success == "true") {
                $('#Gender').html(gender);
            } else {

            }
        },
        error: function () {}
    });

}

$("#select_ava").change(function () {

    var formData = new FormData();
    formData.append("file", document.getElementById("select_ava-").files[0]);

    let url = "http://" + ip + "/avatars";
    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        /**
         *必须false才会自动加上正确的Content-Type
         */
        contentType: false,
        /**
         * 必须false才会避开jQuery对 formdata 的默认处理
         * XMLHttpRequest会对 formdata 进行正确的处理
         */
        processData: false,
        success: function (json) {
            console.log(json.success);
            if (json.success == true) {
                $("#Avatarbody ul").append(
                    "<li>" +
                    "<img src=" + "http://" + ip + "/headpics/" + json.data.name + " onclick='user_custom(this)'>" +
                    "</li>"
                )

                let x = $("#Avatarbody").scrollHeight();

                $("#Avatarbody").scrollTop(x);
                $.hideLoading();
                $.toast("上传成功", 1000);

                for (var i = 0; i < $li.length; i++) {
                    $li[i].index = i;
                    $li[i].onclick = function () {
                        $li[index].style.borderRadius = "15%";
                        this.style.borderRadius = "50%";
                        index = this.index;
                    }
                }

            } else {
                $.hideLoading();
                $.toast("上传失败,图片过大", "cancel", 1000);
            }

        },
        error: function () {
            $.hideLoading();
            $.toast("上传失败,图片过大", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        },
    });
})

function user_out() {
    $.ajax({
        type: 'POST',
        url: "http://" + ip + "/users/logout",
        success: function (json) {
            $.hideLoading();
            console.log(json)
        },
        error: function () {
            $.hideLoading();
        },
        beforeSend: function () {
            $.showLoading("正在退出");
        },
        global: false,
    })
    window.location.href = "../signin-reg/login.html"
}

function user_custom(obj) {
    Avatarsrc = $(obj).attr("src");
}

//历史活动
function personal_history() {
    $("#personal-history-div").css("display", "block");
    $("#personal-history-div").animate({
        left: 0,
        opacity: 1
    }, 200);

    setTimeout(function () {
        $("#personal-content").css("display", "none");
    }, 200);

    $.ajax({
        type: 'GET',
        url: "http://" + ip + "/activity/attended",
        success: function (json) {
            $.hideLoading();
            $(".personal-history-content").html("");
            if (json.success == true) {
                for (let i = 0; i < json.data.length; i++) {
                    eadte = new Date(json.data[i].endTime)
                    edate = date_Format(eadte);
                    $(".personal-history-content").append(
                        "<a class='weui-cell weui-cell_access' href='javascript:;' onclick='show_activity_info(0,this)' id='activity-history-" + json.data[i].id + "'>" +
                        "<div class='weui-cell__bd'>" +
                        json.data[i].name +
                        "(" + json.data[i].area + ")" +
                        "</div>" +
                        "<div class='weui-cell__ft' style='font-size:13px'>" +
                        edate +
                        "</div>" +
                        "</a>"
                    )
                }
            } else {
                $.toast("获取历史记录失败", "cancel", 1000);
            }
        },
        error: function () {
            $.hideLoading();
            $.toast("获取历史记录失败", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        }
    })

}

//预订活动
function personal_reserve() {
    $("#personal-reserve-div").css("display", "block");
    $("#personal-reserve-div").animate({
        left: 0,
        opacity: 1
    }, 200);

    setTimeout(function () {
        $("#personal-content").css("display", "none");
    }, 200);

    $.ajax({
        type: 'GET',
        url: "http://" + ip + "/activity/subscribled",
        success: function (json) {
            $.hideLoading();
            $(".personal-reserve-content").html("");
            if (json.success == true) {
                for (let i = 0; i < json.data.length; i++) {
                    eadte = new Date(json.data[i].startTime)
                    edate = date_Format(eadte);
                    $(".personal-reserve-content").append(
                        "<a class='weui-cell weui-cell_access' href='javascript:;' onclick='show_activity_info(0,this)' id='activity-reserve-" + json.data[i].id + "'>" +
                        "<div class='weui-cell__bd'>" +
                        json.data[i].name +
                        "(" + json.data[i].area + ")" +
                        "</div>" +
                        "<div class='weui-cell__ft' style='font-size:13px'>" +
                        edate +
                        "</div>" +
                        "</a>"
                    )
                }
            } else {
                $.toast("获取预订记录失败", "cancel", 1000);
            }
        },
        error: function () {
            $.hideLoading();
            $.toast("获取预订记录失败", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        }
    })
}

// 发布的活动
function personal_publish() {
    $("#personal-publish-div").css("display", "block");
    $("#personal-publish-div").animate({
        left: 0,
        opacity: 1
    }, 200);

    setTimeout(function () {
        $("#personal-content").css("display", "none");
    }, 200);

    $("#publish-notrun").trigger("click");

}

$(function () {
    $(".personal-history-back").click(function () {
        $("#personal-history-div").animate({
            left: "102%",
            opacity: 0
        }, 200);
        $("#personal-content").css("display", "block");

        setTimeout(function () {
            $("#personal-history-div").css("display", "none");
        }, 200);
    });

    $(".personal-reserve-back").click(function () {
        $("#personal-reserve-div").animate({
            left: "102%",
            opacity: 0
        }, 200);
        $("#personal-content").css("display", "block");

        setTimeout(function () {
            $("#personal-reserve-div").css("display", "none");
        }, 200);
    });

    $(".personal-publish-back").click(function () {
        $("#personal-publish-div").animate({
            left: "102%",
            opacity: 0
        }, 200);
        $("#personal-content").css("display", "block");

        setTimeout(function () {
            $("#personal-publish-div").css("display", "none");
        }, 200);
    });

    $(".personal-history-content").css("height", window.screen.height - 150)

})


// 发布的活动
function load_publish_activity(obj) {
    $(".personal-publish-content").html("");

    let id = obj.id;

    $.ajax({
        type: 'GET',
        url: "http://" + ip + "/activity/me",
        success: function (json) {
            $.hideLoading();

            if (json.success == true) {
                for (let i = 0; i < json.data.length; i++) {
                    if (id == "publish-notrun") {
                        if (json.data[i].status == 1) {
                            eadte = new Date(json.data[i].startTime)
                            edate = date_Format(eadte);
                            $(".personal-publish-content").append(
                                "<a class='weui-cell weui-cell_access' href='javascript:;' onclick='update_activity_info(this)' id='activity-notrun-" + json.data[i].id + "'>" +
                                "<div class='weui-cell__bd'>" +
                                json.data[i].name +
                                "(" + json.data[i].area + ")" +
                                "</div>" +
                                "<div class='weui-cell__ft' style='font-size:13px'>" +
                                edate +
                                "</div>" +
                                "</a>"
                            )
                        }

                    } else if (id == "publish-run") {
                        if (json.data[i].status == 0) {
                            eadte = new Date(json.data[i].startTime)
                            edate = date_Format(eadte);
                            $(".personal-publish-content").append(
                                "<a class='weui-cell weui-cell_access' href='javascript:;' onclick='show_activity_info(0,this)' id='activity-run-" + json.data[i].id + "'>" +
                                "<div class='weui-cell__bd'>" +
                                json.data[i].name +
                                "(" + json.data[i].area + ")" +
                                "</div>" +
                                "<div class='weui-cell__ft' style='font-size:13px'>" +
                                edate +
                                "</div>" +
                                "</a>"
                            )
                        }

                    } else if (id = "publish-end") {
                        if (json.data[i].status == 2) {
                            eadte = new Date(json.data[i].startTime)
                            edate = date_Format(eadte);
                            $(".personal-publish-content").append(
                                "<a class='weui-cell weui-cell_access' href='javascript:;' onclick='show_activity_info(2,this)' id='activity-end-" + json.data[i].id + "'>" +
                                "<div class='weui-cell__bd'>" +
                                json.data[i].name +
                                "(" + json.data[i].area + ")" +
                                "</div>" +
                                "<div class='weui-cell__ft' style='font-size:13px'>" +
                                edate +
                                "</div>" +
                                "</a>"
                            )
                        }
                    }
                }
            } else {
                $.toast("获取活动记录失败", "cancel", 1000);
            }
        },
        error: function () {
            $.hideLoading();
            $.toast("获取活动记录失败", "cancel", 1000);
        },
        beforeSend: function () {
            $.showLoading();
        }
    })
}

var edate = "";
var now = new Date();
var myYear = now.getFullYear();
var myWeek = now.getDay();
var myDay = now.getDate();
var myMonth = now.getMonth() + 1;

var mynyr_s = myYear + "-" + myMonth + "-" + myDay
var mynyr_e = mynyr_s;

function date_Format(date_format) {
    let week;
    let date_format_f = "";
    if (date_format.getDay() == 0) {
        week = "本周日";
    } else if (date_format.getDay() == 1) {
        week = "本周一";
    } else if (date_format.getDay() == 2) {
        week = "本周二";
    } else if (date_format.getDay() == 3) {
        week = "本周三";
    } else if (date_format.getDay() == 4) {
        week = "本周四";
    } else if (date_format.getDay() == 5) {
        week = "本周五";
    } else if (date_format.getDay() == 6) {
        week = "本周六";
    }


    if (date_format.getFullYear() > myYear) {
        date_format_f = date_format_f + date_format.getFullYear() + "年";
    }

    let y;
    let t = is(date_format);

    if (myDay == date_format.getDate() && myMonth == date_format.getMonth() && myYear == date_format.getFullYear()) {
        y = "今日";
    } else if (t) {
        y = week;
    } else {
        y = date_format.getMonth() + 1 + "月" + date_format.getDate() + "日";
    }

    date_format_f = date_format_f + y + "&nbsp;" + date_format.getHours() + ":" + date_format.getMinutes();

    return date_format_f;
}

function is(data) {

    if (myYear == data.getFullYear() && myMonth == data.getMonth()) {
        var d = myWeek;
        if (d == 0) d = 7;
        var d1 = data.getDate();
        var d2 = myDay;
        if (d1 == d2)
            return true;
        else if (d1 > d2 && d1 - d2 <= 7 - d)
            return true;
        else if (d1 < d2 && d2 - d1 <= d - 1)
            return true;
    }

    return false;
}

function load_avatar() {
    $box = document.getElementById('Avatarbody');
    $li = $box.getElementsByTagName('li');
    $img = $box.getElementsByTagName('img');

    for (var i = 0; i < $li.length; i++) {
        $li[i].index = i;
        $img[i].src = 'http://' + ip + '/headpics/default' + i + '.jpg';
        $li[i].onclick = function () {
            $li[index].style.borderRadius = "15%";
            this.style.borderRadius = "50%";
            index = this.index;
        }
    }

    $("#Avatarbody li img").click(function () {
        Avatarsrc = $(this).attr("src");
        console.log(Avatarsrc)
    })
}