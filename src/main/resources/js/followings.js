$(document).ready(function() {
    var userId = 1; // 원하는 사용자 ID를 입력하세요

    $.ajax({
        type: "GET",
        url: "/follow/followings/" + userId,
        dataType: "json",
        success: function(response) {
            var followersList = response;

            // 팔로워 목록을 화면에 표시
            var followingsDiv = $("#followings");
            var followingsHTML = "<ul>";
            followersList.forEach(function(following) {
                followingsHTML += "<li>" + following + "</li>";
            });
            followingsHTML += "</ul>";

            followingsDiv.html(followingsHTML);
        },
        error: function(error) {
            console.log("Error:", error);
        }
    });
});