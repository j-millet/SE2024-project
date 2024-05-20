document.getElementById('submit-button').addEventListener('click', function() {
    const jsonInput = document.getElementById('json-input').value;
    const operation = document.getElementById('operation-select').value;
    const paramsInput = document.getElementById('params-input').value;

    const params = paramsInput.split(',')
        .map(param => param.trim())
        .filter(param => param.length > 0);

    let json;
    try {
        var input = String.raw`${jsonInput.replace(/"/g,"\\\"").replace(/'/g,String.raw`<sq>`).replace(/(\r\n|\n|\r)/gm, "")}`;
        console.log(JSON.stringify(input));
        json = JSON.parse('"' + input + '"');
        console.log(json);
    } catch (e) {
        alert("Invalid JSON");
        console.error("Invalid JSON");
        console.error(e);
        return;
    }
    console.log(json);

    const requestBody = {
        "json-string": json,
        "params": params
    };

    fetch(`http://localhost:8080/api/${operation}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => response.text())
        .then(text => {
            var outerJSON = JSON.parse(text);
            if ("error" in outerJSON) {
                alert(outerJSON["error"]);
                return;
            }
            document.getElementById('result').textContent = outerJSON["json-string"].replace(/<sq>/g,"'");
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
});