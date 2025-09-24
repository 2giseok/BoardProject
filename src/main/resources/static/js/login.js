import {setTokens} from "./auth.js";

document.addEventListener("DOMContentLoaded", () => {
    const  form = document.getElementById("login-form");
    const  msg = document.getElementById("msg");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        msg.textContent ="";

        const username = document.getElementById("username").value.trim();
        const  password = document.getElementById("password").value.trim();
        if(!username||!password) {msg.textContent="아이디 혹은 비밀번호를 입력 하세요"; return;}

        try {
            const  res=  await fetch("/api/auth/login", {
                method :"POST",
                headers : {"Content-Type":"application/json","Accept":"application/json"},
                body: JSON.stringify({username , password})

            });
            if(!res.ok) {msg.textContent=`로그인 실패 ${res.status}`; return; }
            const data   =await res.json();
            if(!data.accessToken) {msg.textContent="토큰 응답 x"; return;}

            setTokens(data);
            location.assign("/articles");

        }catch (err){
            console.error(err);
            msg.textContent = "네트워크 오류 "

        }
    })

})