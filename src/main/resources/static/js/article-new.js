document.getElementById("article-form").addEventListener("submit",async (e) => {
    e.preventDefault();

    const  title =  document.getElementById("title").value.trim();
    const  content = document.getElementById("content").value.trim();

    const  token = localStorage.getItem("accessToken");
    if(!token) {
        window.location.assign("/login");
        return;

    }
    try {
    const  res = await fetch("/api/articles" , {
        method :"POST",
        headers : {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": `Bearer ${token}`

        },
        body: JSON.stringify({title, content})
    });

    if(res.ok) {
        window.location.assign("/articles");
        return;
    }  if (res.status == 401) {
        window.location.assign("/login")
        return;
    }

    const text = await res.text().catch(() => "");
    alert(`작성 실패 (HTTP ${res.status})${text ? `\n${text}` : ""}`);
} catch(err) {
        console.error("[article.new] fetch error", err);
        alert("네트워크 오류 ");
    }


}

);