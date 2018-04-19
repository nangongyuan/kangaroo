/*
 * @Author: shn 
 * @Date: 2018-04-11 18:31:43 
 * @Last Modified by:   shn 
 * @Last Modified time: 2018-04-11 18:31:43 
 */
var colors = ["#A98DEB", "#E58167", "#ECBA89", "#EBB5C4", "#B2D97B", "#9BD1F2"];
var temp;
var m = new Map();

function ShowHobby() {
    let ScreenWidth = $(document.body).outerWidth(true);
    var z;
    if (ScreenWidth >= 410 && ScreenWidth < 760) {
        z = 3;
    } else if (ScreenWidth >= 760) {
        z = 7;
    } else {
        z = 3;
    }
    $.ajax({
        type: 'GET',
        url: 'http://' + ip + '/activity-type',
        success: function (data) {
            temp = data.data;
            var changeone;
            for (var i = 0; i < temp.length; i++) {
                if (i % z == 0) {
                    ul = document.createElement("ul");
                    document.getElementById("J_List").appendChild(ul);
                }
                colorIndex = Math.floor(Math.random() * 6);
                m.set('' + temp[i].id, colorIndex);
                ul.innerHTML += '<div><span id=activity-hobby-' + temp[i].id + ' onClick="labelClick(this)" style="color:' + colors[colorIndex] + '" title="未选">' + temp[i].name + '</span></div>';
            }
            $.ajax({
                type: 'GET',
                url: 'http://' + ip + '/activity-type/me',
                xhrFields: {
                    withCredentials: true
                },
                crossDomain: true,
                success: function (data, textStatus, jqXHR) {
                    var t = data.data;
                    for (var i = 0; i < t.length; i++) {
                        var tb = document.getElementById("activity-hobby-" + t[i].id);
                        let tb1 = tb.id.split("-")     
                        tb.style.color = '#FFFFFF';
                        tb.style.background = colors[m.get(tb1[2])];
                        tb.title = "已选";
                    }
                }
            });
        }
    });
    
}

function labelClick(e) {
    let e1 = e.id.split("-")
    if ("未选" == e.title) {
        e.style.color = '#FFFFFF';
        e.style.background = colors[m.get(e1[2])];
        e.title = "已选";
    } else {
        e.style.color = colors[m.get(e1[2])];
        e.style.background = '#FFFFFF';
        e.title = "未选";
    }
    let hobbies = createResult();
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
                
            } else {

            }
        },
        error: function () { }
    });
};

function createResult() {
    var str = '';
    for (var i = 0; i < temp.length; i++) {
        var tb = document.getElementById('activity-hobby-'+temp[i].id);
        if (tb.title == '已选') {
            str += temp[i].id + ",";
        }
    }
    
    return str;
};