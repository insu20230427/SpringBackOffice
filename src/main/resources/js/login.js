document.getElementById('loginButton').addEventListener('click', function(event) { // 수정된 부분: submit 이벤트 대신 click 이벤트로 변경
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;

    const loginRequest = {
        username: username,
        password: password,
        role: role,
    };

    fetch('/api/auth/login', {
        method: 'GET/POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginRequest),
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to login');
            }
        })
        .then(response => {
            // 로그인 성공 시 액세스 토큰을 헤더에서 가져와서 저장
            const authorizationHeader = response.headers.get('Authorization');
            if (authorizationHeader && authorizationHeader.startsWith('Bearer ')) {
                const accessToken = authorizationHeader.substring(7);
                // 액세스 토큰을 원하는 저장소에 저장 (예: LocalStorage)
                // 여기서는 간단히 콘솔에 출력하는 것으로 대체합니다.
                console.log('Access Token:', accessToken);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            // 로그인 실패 시 사용자에게 오류 메시지 표시
            alert('로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.');
        });
});