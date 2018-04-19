/*
 * @Author: shn 
 * @Date: 2018-04-11 18:30:27 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 15:16:16
 */

var now = new Date();
var myYear = now.getFullYear();
var myWeek = now.getDay();
var myDay = now.getDate();
var myMonth = now.getMonth() + 1;

var mynyr_s = myYear + "-" + myMonth + "-" + myDay
var mynyr_e = mynyr_s;
$(function () {
    //判断最大人数输入框是否符合要求
    $("#max-people").change(function () {
        if (isNaN($('#max-people').val())) {
            $("#max-people").val('');
            $("#max-people").attr('placeholder', "请输入数字");
        }
    })

    $("#max-people").focus(function () {
        $("#max-people").attr('placeholder', "不超过99999人");
    });

    //判断是否需要设置密码
    $("#check-password").change(function () {
        if ($("#check-password").is(":checked")) {
            $("#input-password").attr('placeholder', "请输入密码");
            $("#input-password").removeAttr('readonly');
        } else {
            $("#input-password").attr('placeholder', "是否设置密码");
            $("#input-password").attr('readonly', "readonly");
        }
    });

    //单击事件绑定
    $("#activity-type-input").click(function () {
        $("#activity-type-icon").trigger("click");
    });

    //选择活动类型模态框弹出
    $("#activity-type-icon").click(function () {
        $("#activity-type-modal").modal('show');
    });

    //获取活动类型名称及id
    var activity_number;
    var activity_type = new Array;
    var activity_pic;
    $.ajax({
        type: 'GET',
        url: 'http://' + ip + '/activity-type',
        success: function (json) {
            activity_number = json.data.length;
            for (let i = 0; i < activity_number; i++) {
                $("ul").append("<li id='" + json.data[i].id + "'onclick='select_li(this)'>" + json.data[i].name + "</li>");
            }
        }
    });

    //活动类型模态框确定按钮
    $("#activity-type-button").click(function () {
        $("#activity-type-modal").modal('hide');
        $("#activity-type-input").val(select_li_name);
    });

    //单击事件绑定
    $("#activity-add-pic").click(function () {
        $("#activity-pic-file").trigger("click");
    });

    //图片单击事件绑定上传图片图标单击事件    
    $("#activity-pic").click(function () {
        $("#activity-pic-file").trigger("click");
    });

    //上传活动宣传图
    $("#activity-pic-file").change(function () {
        var formData = new FormData();
        formData.append("file", document.getElementById("activity-pic-file").files[0]);

        let url = "http://" + ip + "/advertising";
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
                    $.hideLoading();
                    $.toast("上传成功", 1000);
                    activity_pic = json.data.name;
                    $("#activity-pic").attr('src', 'http://' + ip + "/advertisings/" + activity_pic);
                    setTimeout('img_center()', 1000);
                    $("#activity-pic-div").css('display', 'none');

                } else {
                    $.hideLoading();
                    $.toast("上传失败,图片过大", "cancel", 1000);
                }

            },
            error: function () {
                $.hideLoading();                
                console.log("上传失败")
            },
            beforeSend: function () {
                $.showLoading();
            },
        });
    })

    //发布事件
    $("#publish").click(function () {

        if (activity_pic == null) {
            activity_pic = "default0.png";
        }

        let url = "http://" + ip + "/activity?name=" + $("#activity-name").val() +
            "&intro=" + $("#activity-intro").val() +
            "&typeid=" + select_li_id +
            "&s=" + $("#create-start-date-time").val() +
            "&e=" + $("#create-end-date-time").val() +
            "&area=" + $("#area").val() +
            "&max=" + $("#activity-max").val() +
            "&password=" + $("#input-password").val() +
            "&site=" + $("#activity-site").val() +
            "&pic=" + activity_pic;

        if ($("#create-start-date-time").val() == null || $("#create-end-date-time").val() == null || select_li_id == null || $("#activity-intro").val() == null ||
            $("#activity-name").val() == null || $("#area").val() == null || $("#activity-max").val() == null || $("#activity-site").val() == null) {
            $("#activity-publish-modal").modal('show');
            $("#publish-title").html("发布信息不完整");
            $("#activity-publish-button").attr('onclick', 'publish_fail()');
        } else {
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
                        $("#activity-publish-modal").modal('show');
                        $("#publish-title").html("发布成功");
                        $("#activity-publish-button").attr('onclick', 'frame()');
                    } else {
                        $("#activity-publish-modal").modal('show');
                        $("#publish-title").html("发布失败");
                        $("#activity-publish-button").attr('onclick', 'publish_fail()');
                    }
                },
                error: function () {
                    $("#activity-publish-modal").modal('show');
                    $("#publish-title").html("发布失败");
                    $("#activity-publish-button").attr('onclick', 'publish_fail()');
                },
                beforeSend: function () {
                    console.log("正在进行，请稍候");
                },
            });
        }

    })

    $("#back-icon").click(function () {
        window.location.href = "../../frame/frame.html"
    });

    $("#area").cityPicker({
        title:"",
        // showDistrict: false,
        // toolbar: false,
    });


    $("#create-start-date-time").datetimePicker({
        title:"",
        min: mynyr_s,
        onClose: function () {
            mynyr_e = $("#create-end-date-time").val();
            mynyr_e = mynyr_e.substring(0, mynyr_e.length - 6);
            $("#create-end-date-time").removeAttr("disabled");
        },
        // toolbar: false,
    });

    $("#create-end-date-time").datetimePicker({
        title:"",        
        min: mynyr_e,
        // toolbar: false,
        onClose: function () {
            if (mynyr_e < mynyr_s) {
                console.log(mynyr_e)
            }
        },
    });
});

//上传图片自适应居中
var select_li_id;
var select_li_name;

function select_li(objct) {
    select_li_id = objct.id;
    select_li_name = objct.innerHTML;
}

function img_center() {
    let width = $("#activity-pic").width() / 2 + "px";
    let height = $("#activity-pic").height() / 2 + "px";

    console.log(width + ";" + height);

    $("#activity-pic").css('margin-left', '-' + width);
    $("#activity-pic").css('margin-top', '-' + height);
    $("#activity-pic").css('display', 'block');
}

function frame() {
    window.location.href = "../../frame/frame.html";
}

function publish_fail() {
    $("#activity-publish-modal").modal('hide');
}

function date_Format2(formatdate) {
    let i = formatdate.getFullYear() + "-" + (formatdate.getMonth() + 1) + "-" + formatdate.getDate() + " " + formatdate.getHours() + ":" + formatdate.getMinutes()
    return i;
}