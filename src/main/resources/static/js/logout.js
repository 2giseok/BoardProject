import { getRefreshToken, clearToken} from "./auth.js";

document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("logout-btn");
    if(!btn) return;

    btn.addEventListener("click", async () => {
        const  rt = getRefreshToken();
        if(rt) {
            try {
                await fetch("/api/auth/logout", {

                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({refreshToken: rt})

                });
            } catch (e) {
                console.warn("로그아웃 실패",  e);
            }

        }
        clearToken();
        alert("로그아웃 성공");
        window.location.assign("/login");

    });


});