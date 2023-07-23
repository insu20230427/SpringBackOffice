// 서버의 URL을 입력하세요
const serverURL = "http://localhost:8080";
const accessToken = "YOUR_ACCESS_TOKEN";
function showProfile() {
    fetch(`${serverURL}/api/auth/profile`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer YOUR_ACCESS_TOKEN", // 여기에 실제로 발급받은 액세스 토큰을 넣으세요
        },
    })
        .then(response => response.json())
        .then(data => {
            // 프로필 정보를 화면에 표시
            var profileDiv = document.getElementById("profile");
            var profileHTML = `
            <p><strong>Name:</strong> ${data.name}</p>
            <p><strong>Email:</strong> ${data.email}</p>
            <!-- 추가적인 프로필 정보를 필요에 따라 표시할 수 있습니다 -->
        `;
            profileDiv.innerHTML = profileHTML;
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

showProfile();