const apiUrl = 'http://localhost/mycalcwebapp';

function calc(first, second, op) {
    let data = {first: first, second: second, op: op};

    fetch(apiUrl + '/calc', {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw Error(response.statusText);
        }
    })
    .then(json => {
        document.getElementById("result").innerHTML = json.result;
        document.getElementById("uid").innerHTML = json.uid;
    })
    .catch(error => console.error('Error: ', error));
}

window.addEventListener('load', function(e) {
    document.getElementById('calc')
        .addEventListener('submit', processFormSubmit);
});

function processFormSubmit(e) {
    e.preventDefault();

    let first = document.getElementById('first').value;
    let second = document.getElementById('second').value;
    let op = document.getElementById('op').value;

    calc(first, second, op);

    return false;
}
