const songName = document.querySelector("#name");
const songUrl = document.querySelector("#url");
const songArtist = document.querySelector("#artist");

songName.onchange = function (e) {
    this.value = e.target.value;
};

songUrl.onchange = function (e) {
    this.value = e.target.value;
};

songArtist.onchange = function (e) {
    this.value = e.target.value;
};

document.querySelector(".name-reset").onclick = () => {
    document.querySelector("#name").value = initName;
};
document.querySelector(".url-reset").onclick = () => {
    document.querySelector("#url").value = initUrl;
};
document.querySelector(".artist-reset").onclick = () => {
    document.querySelector("#artist").value = initArtist;
};

document.querySelector(".submit").onclick = () => {
    const body = {
        name: songName.value.trim(),
        url: songUrl.value.trim(),
        artist: songArtist.value.trim(),
    };
    if (
        songName.value.trim() === "" ||
        songUrl.value.trim() === "" ||
        songArtist.value.trim() === ""
    ) {
        alert("Please fill in all the fields");
        document.querySelector("#name").value = initName;
        document.querySelector("#url").value = initUrl;
        document.querySelector("#artist").value = initArtist;
    } else {
        if (songName.value.trim() === initName) {
            delete body.name;
        }
        if (songUrl.value.trim() === initUrl) {
            delete body.url;
        }
        if (songArtist.value.trim() === initArtist) {
            delete body.artist;
        }
        if (Object.entries(body).length === 0) {
            window.location.href = "/";
        } else {
            fetch(`/api/musics/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(body),
            })
                .then((res) => {
                    if (res.status === 401) {
                        throw new Error(res.status.toString());
                    } else if (res.status === 404) {
                        throw new Error(res.status.toString());
                    }
                    return res.json();
                })
                .then(() => {
                    window.open("/", "_self");
                })
                .catch((err) => {
                    if (err.message === "401") {
                        document.cookie = "token=";
                        window.open("/", "_self");
                    } else if (err.message === "404") {
                        window.open("/", "_self");
                    }
                });
        }
    }
};
