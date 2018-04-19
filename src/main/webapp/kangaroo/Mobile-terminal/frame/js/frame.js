/*
 * @Author: shn 
 * @Date: 2018-04-11 18:30:58 
 * @Last Modified by:   shn 
 * @Last Modified time: 2018-04-11 18:30:58 
 */
var frameSwiper = "";
$(function () {

    frameSwiper = new Swiper('#Content', {

        initialSlide: 0,
        speed: 500,
        width: document.documentElement.clientWidth,
        height: document.documentElement.clientHeight - 60,
        on: {
            slideChangeTransitionEnd: function () {
                let i = this.activeIndex;
                if (i == 0) {
                    show_home()
                } else if (i == 1) {
                    show_activity()
                } else if (i == 2) {
                    show_friend()
                } else if (i == 3) {
                    show_personal()
                }
            },
        },
        hashNavigation: {
            watchState: true,
        },
    })

    $("#Content").css("height", document.documentElement.clientHeight - 60)

    $.ajax({
        url: '../home/home.html',
        success: function (html) {
            $(".swiper-home").append(html);
        },
        global: false,
    })

    $.ajax({
        url: '../activity/activity.html',
        success: function (html) {
            $(".swiper-activity").append(html);
        },
        global: false,
    })

    $.ajax({
        url: '../friend/friend.html',
        success: function (html) {
            $(".swiper-friend").append(html);
        },
        global: false,
    })

    $.ajax({
        url: '../personal/personal.html',
        success: function (html) {
            $(".swiper-personal").append(html);
        },
        global: false,
    })
    $("#home").click(function () {
        frameSwiper.slideTo(0, 1000, false); //切换到第一个slide，速度为1秒        
        show_home()
    });

    $("#activity").click(function () {
        frameSwiper.slideTo(1, 1000, false); //切换到第一个slide，速度为1秒        
        show_activity()
    });

    $("#friend").click(function () {
        frameSwiper.slideTo(2, 1000, false); //切换到第一个slide，速度为1秒    
        show_friend()
    });

    $("#personal").click(function () {
        frameSwiper.slideTo(3, 1000, false); //切换到第一个slide，速度为1秒                
        show_personal()
    });

    $("#Add").click(function () {
        $("#AddActivity").css("display", "block");
        $("#AddFriend").css("display", "block");
        $("#CloseButton").css("display", "block");

        $("#Mask").fadeIn(400);
        $("#AddActivity").animate({
            top: '60%',
            opacity: '1'
        }, 250);
        $("#AddActivity").animate({
            top: '65%',
            opacity: '1'
        }, 300);
        $("#CloseButton").animate({
            opacity: '1'
        }, 400);

        setTimeout('Delay()', 40);

    });

    $('#CloseButton').click(function () {
        $("#Mask").fadeOut(400);
        $("#AddActivity").animate({
            top: '88%',
            opacity: '0'
        }, 200);
        $("#CloseButton").animate({
            opacity: '0'
        }, 200);

        setTimeout('Delayclose()', 40);

        setTimeout('dispalyclose()', 200);

    });

    $("#AddActivity").click(function () {
        window.location.href = "../activity/create-activity/create-activity.html"
    });

    $("#AddFriend").click(function () {
        $('#CloseButton').trigger("click");

        $("#frame-add-friend").css("display", "block");
        $(".add-friend-back").css("display", "block");

        $("#frame-add-friend").animate({
            left: '0',
            opacity: '1'
        }, 250);
        $("#frame-main").animate({
            opacity: '0'
        }, 250);

        setTimeout(function () {
            $("#frame-main").css("display", "none");
        }, 250);

        add_friend_apply();

    });
})

function Delay() {
    $("#AddFriend").animate({
        top: '60%',
        opacity: '1'
    }, 250);
    $("#AddFriend").animate({
        top: '65%',
        opacity: '1'
    }, 300);
}

function Delayclose() {
    $("#AddFriend").animate({
        top: '88%',
        opacity: '0'
    }, 200);
}

function dispalyclose() {
    $("#AddActivity").css("display", "none");
    $("#AddFriend").css("display", "none");
    $("#CloseButton").css("display", "none")
}

function show_home() {
    $("#home").attr('src', 'img/home_hover.png');
    $("#home").css('pointer-events', 'none');

    $("#activity").attr('src', 'img/activity.png');
    $("#activity").css('pointer-events', 'auto');

    $("#friend").attr('src', 'img/friend.png');
    $("#friend").css('pointer-events', 'auto');

    $("#personal").attr('src', 'img/personal.png');
    $("#personal").css('pointer-events', 'auto');

    $("#Content").css("background", "rgb(255, 255, 255)");    
    
}

function show_activity() {
    $("#searchBar").css("display", "block")

    $("#home").attr('src', 'img/home.png');
    $("#home").css('pointer-events', 'auto');

    $("#activity").attr('src', 'img/activity_hover.png');
    $("#activity").css('pointer-events', 'none');

    $("#friend").attr('src', 'img/friend.png');
    $("#friend").css('pointer-events', 'auto');

    $("#personal").attr('src', 'img/personal.png');
    $("#personal").css('pointer-events', 'auto');

    $(".mainHeading").css("background", "#e8e8e8")
    $("#Content").css("background", "rgb(255, 255, 255)")
    
}

function show_friend() {
    $("#home").attr('src', 'img/home.png');
    $("#home").css('pointer-events', 'auto');

    $("#activity").attr('src', 'img/activity.png');
    $("#activity").css('pointer-events', 'auto');

    $("#friend").attr('src', 'img/friend_hover.png');
    $("#friend").css('pointer-events', 'none');

    $("#personal").attr('src', 'img/personal.png');
    $("#personal").css('pointer-events', 'auto');

    $(".mainHeading").css("background", "#e8e8e8")
    $("#Content").css("background", "rgb(255, 255, 255)")
    
}

function show_personal() {
    $("#home").attr('src', 'img/home.png');
    $("#home").css('pointer-events', 'auto');

    $("#activity").attr('src', 'img/activity.png');
    $("#activity").css('pointer-events', 'auto');

    $("#friend").attr('src', 'img/friend.png');
    $("#friend").css('pointer-events', 'auto');

    $("#personal").attr('src', 'img/personal_hover.png');
    $("#personal").css('pointer-events', 'none');

    $(".mainHeading").css("background", "rgb(252, 183, 72)")
    $("#Content").css("background", "rgb(236, 236, 236)")
    
}