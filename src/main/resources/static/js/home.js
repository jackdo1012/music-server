const saveBtn = document.querySelector(".save");

if (saveBtn) {
    saveBtn.onclick = (e) => {
        e.preventDefault();
        const name = document.getElementById("name").value.trim();
        const url = document.getElementById("url").value.trim();
        const artist = document.getElementById("artist").value.trim();
        fetch("/api/musics", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name,
                url,
                artist,
            }),
        })
            .then((res) => {
                document.getElementById("name").value = "";
                document.getElementById("url").value = "";
                document.getElementById("artist").value = "";
                if (res.status === 401) {
                    throw new Error(res.status.toString());
                }
                return res.json();
            })
            .then((data) => {
                location.reload();
            })
            .catch((err) => {
                if (err.message === "401") {
                    document.cookie = "token=";
                    location.reload();
                }
            });
    };
}

const loginBtn = document.querySelector(".login-btn");
const logoutBtn = document.querySelector(".logout-btn");

if (loginBtn) {
    loginBtn.onclick = () => {
        const loginPass = document.querySelector("#loginPassword").value;
        document.cookie = "token=" + loginPass;
        location.reload();
    };
}
if (logoutBtn) {
    logoutBtn.onclick = () => {
        document.cookie = "token=";
        location.reload();
    };
}

window.onload = () => {
    const cookie = () => {
        let cookie = {};
        document.cookie
            .replaceAll(" ", "")
            .split(";")
            .forEach((item) => {
                const [key, value] = item.split("=");
                cookie[key] = value;
            });
        return cookie;
    };
};
