export function setTokens({ accessToken, refreshToken}) {
    if(accessToken) localStorage.setItem("accessToken", accessToken);
    if( refreshToken) localStorage.setItem("refreshToken", refreshToken);

}
export  function getAccessToken() {
    return localStorage.getItem("accessToken");
}
export  function  getRefreshToken() {
    return localStorage.getItem("refreshToken");
}
export  function  clearToken() {
    localStorage.removeItem("accessToken"); localStorage.removeItem("refreshToken");


}
async function refreshOnce() {
    const  rt = getRefreshToken();
    if(!rt) return false;

const  res = await fetch("/api/auth/refresh", {
    method: "POST",
    headers:{"Content-Type":"application/json","Accept":"application/json"},
    body:JSON.stringify({refreshToken: rt})
});
if(!res.ok) return false;

const data = await res.json();
setTokens(data);
return true;
}
export async function fetchWithAuth(url, options ={}) {
    const headers = {"Accept":"application/json", ...(options.headers || {})};
    const  at = getAccessToken();
    if( at) headers["Authorization"] =`Bearer ${at}`;

    let res =await  fetch(url,{...options,headers});

    if(res.status == 401) {
        const  ok = await  refreshOnce();
        if(ok) {
            const  headers2 = {"Accept":"application/json",...(options.headers || {})};
            const  at2= getAccessToken();
            if(at2)headers2["Authorization"] = `Bearer ${at2}`;
            res =await  fetch(url, {...options,headers: headers2});
        }
    }
    return res;
}