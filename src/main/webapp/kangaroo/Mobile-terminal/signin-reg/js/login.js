/*
 * @Author: shn 
 * @Date: 2018-04-11 18:32:06 
 * @Last Modified by: shn
 * @Last Modified time: 2018-04-12 14:44:16
 */
$(function () {
    // 调用 jsModern 的 fullPage 组件方法
    // 使用默认导航按钮
    // 进行沿 y 轴的横向滑动
    $('#fullpage').fullpage({
        controlArrows: false,
        verticalCentered: false,
        css3: true,
        scrollingSpeed: 300,
        callback: [


        ],
        anchors: ['login', 're', 'per'], // 登录/注册/完善信息
    });

    $.fn.fullpage.moveTo('login', 0);
    $.fn.fullpage.setAllowScrolling(false); //禁用滑动切换

    $("#repassword").change(function () {
        if ($("#newpassword").val() != $("#repassword").val()) {
            $('#repassword').css('border', '1px solid #FF0000');
            $('#repassword').attr('placeholder', "密码与上一项不符");
            $('#repassword').val('');
        } else {
            $('#repassword').css('border', '1px solid silver');
        }
    });

    $("#newpassword").change(function () {
        newpasswordlength = $("#newpassword").val().length;
        console.log(newpasswordlength);
        if (newpasswordlength < 6 || newpasswordlength > 15) {
            $('#newpassword').css('border', '1px solid #FF0000');
            $('#newpassword').val('');
        } else {
            $('#newpassword').css('border', '1px solid silver');
        }
    });

    $("#newusername").change(function () {
        var x = true;

        let username = $("#newusername").val();

        // let reg = /[a-zA-Z]/;
        let usernamelength = $("#newusername").val().length;

        if (usernamelength >= 2 && usernamelength < 21) {
            $('#newusername').css('border', '1px solid silver');
        } else {
            $('#newusername').css('border', '1px solid #FF0000');
            $('#newusername').val('');
            x = false;
        }
        let user = $("#newusername").val();;
        let url = "http://" + ip + "/users/repeat?username=" + user;

        if (x) {
            $.ajax({
                url: url,
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                type: 'GET',
                success: function (json) {
                    y = true;
                    var success = $.trim(json.success);
                    if (success == 'false') {
                        $('#newusername').css('border', '1px solid silver');
                        registeredname = true;
                    } else {
                        $('#newusername').css('border', '1px solid #FF0000');
                        $('#newusername').attr('placeholder', "用户名重复");
                        $('#newusername').val('');
                        registeredname = false;
                    }
                },
                error: function () {}
            });
        }

    });

    $("#newusername").focus(function () {
        $("#newusername").attr('placeholder', "*用户名2-20位不能为纯数字");
        $('#newusername').css('border', '1px solid silver');
    });

    $("#repassword").focus(function () {
        $("#repassword").attr('placeholder', "*确认密码 与前一项相同");
        $('#repassword').css('border', '1px solid silver');
    });

    $("#newpassword").focus(function () {
        $('#newpassword').css('border', '1px solid silver');
    });

    $("#newphone").focus(function () {
        $('#newphone').css('border', '1px solid silver');
    });

    $("#newphone").change(function () {
        let phonelength = $("#newphone").val().length;
        if (phonelength == 11) {
            $('#newphone').css('border', '1px solid silver');
        } else {
            $('#newphone').css('border', '1px solid #FF0000');
            $('#newphone').val('');
        }
    }); //判断注册信息是否符合要求

    $("#Login-Button").click(function () {
        let user = $("#user").val();
        let password = $("#password").val();
        let url = "http://" + ip + "/users/login?user=" + user + "&password=" + password;

        $.ajax({
            url: url,
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            type: 'GET',
            success: function (json) {
                var success = $.trim(json.success);
                $.hideLoading();

                if (success == "true") {
                    console.log(success);
                    window.location.href = "../frame/frame.html";
                } else {
                    error = $.trim(json.msg);
                    $('#Message-Perfect').hide();
                    $('#Message-Button').attr('onclick', '');
                    $('#Message-Model').modal('show');
                    $('#Message-Model-Title').text("登录失败");
                    $('#error-Message').text(error);
                }
            },
            error: function () {
                $.hideLoading();
            },
            beforeSend: function () {
                $.showLoading("正在登陆");
            },
        });
    }); //登录按钮

    $("#Re-Button").click(function () {
        let user = $("#newusername").val();
        console.log(user);
        let password = $("#newpassword").val();
        let phone = $("#newphone").val();
        let verification = $("#verification").val();
        let url = "http://" + ip + "/users/reg?name=" + user + "&password=" + password + "&phone=" + phone + "&headpic=default0.jpg";
        console.log(url);
        if (user == "" || password == "" || phone == "") {
            console.log("注册失败");
            $('#Message-Perfect').hide();
            $('#Message-Button').attr('onclick', '');
            $('#Message-Model').modal('show');
            $('#Message-ModelTitle').text("注册失败");
            $('#error-Message').text("请填写所有信息");
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
                        console.log("注册成功");
                        $('#Message-Button').attr('onclick', 'relogin()');
                        $('#Message-Perfect').show();
                        $('#Message-Model').modal('show');
                        $('#Message-Model-Title').text("注册成功");
                        $('#error-Message').text("");
                    } else {
                        console.log("注册失败");
                        $('#Message-Perfect').hide();
                        $('#Message-Button').attr('onclick', '');
                        $('#Message-Model').modal('show');
                        $('#Message-ModelTitle').text("注册失败");
                        $('#error-Message').text(json.msg);
                    }
                },
                error: function () {
                    console.log("注册失败");
                    $('#Message-Perfect').hide();
                    $('#Message-Button').attr('onclick', '');
                    $('#Message-Model').modal('show');
                    $('#Message-ModelTitle').text("注册失败");
                    $('#error-Message').text("未知原因");
                }
            });
        }

    }); //注册按钮

    $('#Message-Perfect').click(function () {
        let user = $("#newusername").val();
        let password = $("#newpassword").val();
        let url = "http://" + ip + "/users/login?user=" + user + "&password=" + password;

        $.ajax({
            url: url,
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            type: 'GET',
            success: function (json) {
                var success = $.trim(json.success);
                if (success == "true") {
                    $.fn.fullpage.moveTo('per', 2);
                    $("#Per-User-Name").text(user);
                    ShowHobby();                    
                } else {
                    error = $.trim(json.msg);
                    $('#Message-Perfect').hide();
                    $('#Message-Button').attr('onclick', 'backicon()');
                    $('#Message-Model').modal('show');
                    $('#Message-Model-Title').text("登录失败");
                    $('#error-Message').text("请重新登录并前往'个人'完善信息");
                }
            },
            error: function () {}
        });


    }); //前往完善信息按钮

    $('#Per-Gender-Button').click(function () {
        $('#Gender-Model').modal('show');
    })

    $('#Man-Button').click(function () {
        $('#Gender').html('男');
    })

    $('#Woman-Button').click(function () {
        $('#Gender').html('女');
    })

    $('#Per-Button').click(function () {
        let gender = $("#Gender").html();
        let area = $("#Area").html();
        let hobbies = createResult();
        let a = $("#Avatar")[0].src;
        let avatar = a.split('/');
        console.log('/' + avatar[4]);
        console.log('hobbies')
        let url = "http://" + ip + "/activity-type/me-put?interest=" + hobbies;

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
                    url = "http://" + ip + "/users/put?sex=" + gender + "&area=" + area + "&headpic=" + avatar[4];

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
                                window.location.href = "../frame/frame.html";
                            } else {
                                $('#Message-Button').attr('onclick', 'skipFrame()');
                                $('#Message-Perfect').hide();
                                $('#Message-Model').modal('show');
                                $('#Message-Model-Title').text("添加失败");
                                $('#error-Message').text("请前往'个人'完善信息");
                            }
                        },
                        error: function () {}
                    });
                } else {
                    $('#Message-Button').attr('onclick', 'skipFrame()');
                    $('#Message-Perfect').hide();
                    $('#Message-Model').modal('show');
                    $('#Message-Model-Title').text("添加失败");
                    $('#error-Message').text("请前往'个人'完善信息");
                }
            },
            error: function () {}
        });



    }) //完善信息确定按钮

    $("#pass").click(function () {
        window.location.href = "../frame/frame.html";
    })
})

function back_icon() {
    $.fn.fullpage.moveTo('login', 0);
} //返回登录按钮

function Countdown() {
    var i = 60;
    $("#verification-Button").attr('disabled', true);
    var myinter = setInterval(function () {
        i--;
        $("#verification-Text").text(i);
        if (i == 0) {
            $("#verification-Button").attr('disabled', false);
            $("#verification").focus();
            clearInterval(myinter);
            $("#verification-Text").text("获取验证码");
        }
    }, 1000);
} //获取验证码按钮

function relogin() {
    let user = $("#newusername").val();
    let password = $("#newpassword").val();
    let url = "http://" + ip + "/users/login?user=" + user + "&password=" + password;

    $.ajax({
        url: url,
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        type: 'GET',
        success: function (json) {
            var success = $.trim(json.success);
            if (success == "true") {
                window.location.href = "../frame/frame.html";
            } else {
                error = $.trim(json.msg);
                $('#Message-Perfect').hide();
                $('#Message-Button').attr('onclick', 'backicon()');
                $('#Message-Model').modal('show');
                $('#Message-ModelTitle').text("登录失败");
                $('#error-Message').text(error);
            }
        },
        error: function () {}
    });
} //注册确定按钮

var Avatarsrc;
var index = 0;

$(function () {

    $('body').css('height', $(window).height());
    var $box = document.getElementById('Avatarbody');
    var $li = $box.getElementsByTagName('li');
    var $img = $box.getElementsByTagName('img');


    $("#Avatar").attr('src', 'http://' + ip + '/headpics/default0.jpg');

    $("#Avatar").click(function () {
        $(".my_box").animate({
            'top': '15px',
        }, 300);
    });

    $("#AvatarButton").click(function () {

        if (Avatarsrc != "") {
            $("#Avatar").attr('src', Avatarsrc);
        } else {
            return false;
        }

        headpic = $("#Avatar").attr('src');

    });


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
    })

    $("#AvatarupButton").click(function () {
        $("#chat_inputImage").trigger("click");
    })

    $("#chat_inputImage").change(function () {

        var formData = new FormData();
        formData.append("file", document.getElementById("chat_inputImage").files[0]);

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
                            console.log(this.index);
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


});

function showAvatarModel() {
    $("#AvatarModel").modal('show');
}

function skipFrame() {
    window.location.href = "../frame/frame.html";
}

function user_custom(obj) {
    console.log($(obj).attr("src"));
    Avatarsrc = $(obj).attr("src");
}