$(document).ready(function() {
    var userId = 1; // 원하는 사용자 ID를 입력하세요

    $.ajax({
        type: "GET",
        url: "/follow/followers/" + userId,
        dataType: "json",
        success: function(response) {
            var followersList = response;

            // 팔로워 목록을 화면에 표시
            var followersDiv = $("#followers");
            var followersHTML = "<ul>";
            followersList.forEach(function(follower) {
                followersHTML += "<li>" + follower + "</li>";
            });
            followersHTML += "</ul>";

            followersDiv.html(followersHTML);
        },
        error: function(error) {
            console.log("Error:", error);
        }
    });
});