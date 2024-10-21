document.getElementById("createEbike").addEventListener("click", async function() {
    const formData = new FormData(document.getElementById("bikeForm"));

    var object = {};
    formData.forEach(function(value, key){
        object[key] = value;
    });

    const json = JSON.stringify(object);

    fetch("api/register", {
        method: 'POST',
        body: json
    }).then(async (response) => {
        const val = await response.json();
        document.getElementById("createResponse").innerText = JSON.stringify(val).split(":")[1].replace(/\"/g, "").replace("}", "");
    });
});