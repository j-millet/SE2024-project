document.getElementById('submit-button').addEventListener('click', function() {
    const jsonInput = document.getElementById('json-input').value;
    const operation = document.getElementById('operation-select').value;
    const paramsInput = document.getElementById('params-input').value;

    function processJsonInput(json) {
        let jsonOutput;
        try {
            let input = String.raw`${json.replace(/"/g, "\\\"").replace(/'/g, String.raw`<sq>`).replace(/(\r\n|\n|\r)/gm, "")}`;
            console.log("Processed input:", JSON.stringify(input));

            jsonOutput = JSON.parse('"' + input + '"');
            console.log("Parsed JSON:", jsonOutput);
        } catch (e) {
            alert("Invalid JSON");
            console.error("Invalid JSON");
            console.error(e);
            return;
        }
        return jsonOutput;
    }

    let params;
    if (paramsInput.charAt(0) === '{') {
        params = [processJsonInput(paramsInput)];
    } else {
        params = paramsInput.split(',')
            .map(param => param.trim())
            .filter(param => param.length > 0);
    }
    console.log(params);

    const requestBody = {
        "json-string": processJsonInput(jsonInput),
        "params": params
    };
    console.log(requestBody);
    fetch(`http://localhost:8080/api/${operation}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => response.text())
        .then(text => {
            let outerJSON = JSON.parse(text);
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

document.getElementById('operation-select').addEventListener('change', function () {
    const operation = document.getElementById('operation-select');
    const paramsInput = document.getElementById('params-input');
    const operationsWithoutParams = ['identity', 'minify', 'full'];

    paramsInput.value = '';

    if (operationsWithoutParams.includes(operation.value)) {
        paramsInput.placeholder = '';
        paramsInput.disabled = true;
        paramsInput.classList.add('disabled');
    } else {
        paramsInput.disabled = false;
        paramsInput.classList.remove('disabled');
        if (operation.value === 'compare') {
            paramsInput.placeholder = 'Enter valid JSON to compare';
        } else {
            paramsInput.placeholder = 'Enter parameters (comma separated)';
        }
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const event = new Event('change');
    document.getElementById('operation-select').dispatchEvent(event);
});