document.getElementById('submit-button').addEventListener('click', function() {
    const jsonInput = document.getElementById('json-input').value;
    const operation = document.getElementById('operation-select').value;
    const paramsInput = document.getElementById('params-input').value;

    const params = paramsInput.split(',')
        .map(param => param.trim())
        .filter(param => param.length > 0);

    let json;
    try {
        json = JSON.parse('"' + String.raw`${jsonInput}` + '"');
    } catch (e) {
        alert("Invalid JSON");
        console.error("Invalid JSON");
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
            if (text.includes("error")) {
                alert("Invalid JSON");
                console.error(text);
                return;
            }
            // extract text from the 2nd "{" character to the first "}" character | druciarstwo fest
            const start = text.indexOf("{");
            const end = text.indexOf("}");
            const innerJson = JSON.parse(text.substring(start + 16, end+1)); // I'm sorry
            document.getElementById('result').textContent = JSON.stringify(innerJson, null, 2);
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
});