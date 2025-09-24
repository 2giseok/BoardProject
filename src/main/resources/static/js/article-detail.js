const articleId = window.location.pathname.split('/').pop();

fetch(`/api/articles/${articleId}`)
.then(response => {
    if (!response.ok) {
        throw new Error('네트워크 응답이 올바르지 않습니다');
    }
    return response.json();
})
.then( data => {
    document.getElementById('article-title').textContent = data.title;
    document.getElementById('article-content').textContent= data.content;
})
.catch( error => {
    console.error("데이터 로딩중 문제 ", error);
});


    document.getElementById("delete-btn").addEventListener("click",async () => {

        const token = localStorage.getItem("accessToken");
        if(!token) {location.assign("/login"); return;}

        if(!confirm("정말 삭제 하시겠습니까")) return;

        const res= await fetch(`/api/articles/${articleId}`, {
            method: "DELETE",
            headers: { "Authorization" : `Bearer ${token}`}
        });

        if(res.ok) {
            alert("삭제 완료");
            location.assign("/articles");

        }else if ( res.status ==401) {
            location.assign("/login");

        } else {
            alert("삭제 실패");
        }

    })

    document.getElementById("edit-btn").addEventListener("click",async () => {
        const  token = localStorage.getItem("accessToken");
        if(!token) { location.assign("/login"); return;}

        const  currentTitle = document.getElementById("article-title").textContent.trim();
        const currentContent = document.getElementById("article-content").textContent.trim();

        const  newTitle = prompt("새 제목을 입력", currentTitle);
        if(newTitle ==null) {return;}
        const  newContent = prompt("새 내용 입력",currentContent);
        if(newContent==null) return;

        const  res = await fetch(`/api/articles/${articleId}`, {
            method: "PATCH",
            headers: {
                "Content-Type" : "application/json",
                "Authorization": `Bearer ${token}`
            },
            body : JSON.stringify({title: newTitle.trim(), content: newContent.trim()})
        });

        if(res.ok) {
            alert("수정 완료");
            location.reload();

        } else if (res.status ==401) {
            location.assign("/login");
        }
        else {
            const  t=  await  res.text().catch(() => "");
            alert("수정 실패");
        }


});