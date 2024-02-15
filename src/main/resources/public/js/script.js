const script = (() => {
    const API_URL = "/action/movie?name=";
    const RESPONSE_CONTAINER_ID = "getrespmsg";
    const POSTER_ALT_TEXT = "Poster";

    function loadGetMsg() {
        const nameVar = document.getElementById("name").value;
        const xhttp = new XMLHttpRequest();

        xhttp.onload = function () {
            if (this.status === 200) {
                const data = JSON.parse(this.responseText);
                if (data.Response !== "False") {
                    displayMovieDetails(data);
                }
            } else {
                console.error("Request failed with status:", this.status);
            }
        };

        xhttp.onerror = function () {
            console.error("Request failed");
        };

        xhttp.open("GET", API_URL + nameVar);
        xhttp.send();
    }

    function displayMovieDetails(data) {
        const movieArticle = createMovieArticle(data);
        clearResponseContainer();
        appendToResponseContainer(movieArticle);
    }

    function createMovieArticle(data) {
        const movieArticle = document.createElement("article");
        movieArticle.classList.add("movie-details");

        movieArticle.innerHTML = `
            <img class="poster" src="${data.Poster}" alt="${POSTER_ALT_TEXT}">
            <div>
                <h2 class="title">${data.Title}</h2>
                <p class="director">Director: ${data.Director}</p>
                <p class="released">Released: ${data.Released}</p>
                <p class="actors">Actors: ${data.Actors}</p>
                <p class="language">Language: ${data.Language}</p>
                <p class="plot">Plot: ${data.Plot}</p>
            </div>
        `;

        return movieArticle;
    }

    function clearResponseContainer() {
        const responseContainer = document.getElementById(RESPONSE_CONTAINER_ID);
        responseContainer.innerHTML = "";
    }

    function appendToResponseContainer(element) {
        const responseContainer = document.getElementById(RESPONSE_CONTAINER_ID);
        responseContainer.appendChild(element);
    }

    return {
        loadGetMsg
    };
})();
